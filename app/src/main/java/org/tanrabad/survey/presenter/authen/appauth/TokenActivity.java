/*
 * Copyright (c) 2018 NECTEC
 *   National Electronics and Computer Technology Center, Thailand
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tanrabad.survey.presenter.authen.appauth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import net.openid.appauth.AppAuthConfiguration;
import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceDiscovery;
import net.openid.appauth.ClientAuthentication;
import net.openid.appauth.ClientSecretBasic;
import net.openid.appauth.ClientSecretPost;
import net.openid.appauth.TokenRequest;
import okio.Okio;
import org.tanrabad.survey.BuildConfig;
import org.tanrabad.survey.R;
import org.tanrabad.survey.TanrabadApp;
import org.tanrabad.survey.presenter.LoginActivity;
import org.tanrabad.survey.presenter.TanrabadActivity;
import org.tanrabad.survey.presenter.authen.Authenticator;
import org.tanrabad.survey.presenter.authen.UserProfile;

public class TokenActivity extends TanrabadActivity {
    private static final String TAG = "TokenActivity";
    public static final String AUTH_ACTION_AUTO_LOGIN = "org.tanrabad.auth.action.AUTHEN";

    public static final String USERNAME = "username";
    public static final String AUTO_LOGIN = "auto_login";

    private AuthorizationService mAuthService;
    private AuthStateManager mStateManager;
    private UserProfileManager mUserManager;
    private UserStateManager mUserStateManager;
    private final AtomicReference<UserProfile> mUserInfoJson = new AtomicReference<>();
    private ExecutorService mExecutor;
    private Button authenButton;
    private View logoutButton;
    private AppAuthPresenter auth;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token);

        mStateManager = AuthStateManager.getInstance(this);
        mUserManager = UserProfileManager.getInstance(this);
        mExecutor = Executors.newSingleThreadExecutor();

        Configuration config = Configuration.getInstance(this);
        mAuthService = new AuthorizationService(this,
            new AppAuthConfiguration.Builder().setConnectionBuilder(config.getConnectionBuilder())
                .build());

        mUserInfoJson.compareAndSet(null, mUserManager.getProfile());

        logoutButton = findViewById(R.id.logout);
        logoutButton.setVisibility(View.GONE);
        logoutButton.setOnClickListener(v -> logout());

        authenButton = findViewById(R.id.authentication_button);
        authenButton.setVisibility(View.GONE);
        authenButton.setOnClickListener(v -> {
            if (mUserStateManager != null && mUserStateManager.isRequireAction()) {
                mUserInfoJson.set(null); //Clear for re-check user profile
                checkAuthorize();
                return;
            }
            UserProfile profile = mUserInfoJson.get();
            Authenticator auth = new Authenticator(null);
            auth.setAuthenWith(profile);

            //Send back username to LoginActivity for fill-in app login process
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra(USERNAME, profile.userName);
            intent.putExtra(AUTO_LOGIN, AUTH_ACTION_AUTO_LOGIN.equals(getIntent().getAction()));
            //Fellow line can't re-order
            finish();
            startActivity(intent);
        });
        checkAuthorize();

        auth = new AppAuthPresenter(this);
    }

    @Override protected void onStop() {
        super.onStop();
        auth.close();
    }

    @Override protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mUserInfoJson.set(null); //Clear for re-check user profile
        checkAuthorize();
    }

    private void checkAuthorize() {
        if (mExecutor.isShutdown()) {
            mExecutor = Executors.newSingleThreadExecutor();
        }
        // the stored AuthState is incomplete, so check if we are currently receiving the result of
        // the authorization flow from the browser.
        AuthorizationResponse response = AuthorizationResponse.fromIntent(getIntent());
        AuthorizationException ex = AuthorizationException.fromIntent(getIntent());

        AuthState state = mStateManager.getCurrent();
        if (state.isAuthorized() && response == null) {
            Log.i(TAG, "Authorized state");
            UserProfile profile = mUserInfoJson.get();
            if (profile == null) {
                Log.i(TAG, "Request User's profile with fresh token");
                ClientAuthentication clientAuth =
                    new ClientSecretPost(BuildConfig.TRB_AUTHEN_CLIENT_SECRET);
                state.performActionWithFreshTokens(mAuthService, clientAuth, this::fetchUserInfo);
            } else {
                Log.i(TAG, "Use stored User's Profile");
                displayAuthorized();
            }
            return;
        }

        if (response != null || ex != null) {
            mStateManager.updateAfterAuthorization(response, ex);
        }
        if (response != null && response.authorizationCode != null) {
            // authorization code exchange is required
            mStateManager.updateAfterAuthorization(response, ex);
            exchangeAuthorizationCode(response);
        } else if (ex != null) {
            displayNotAuthorized("Authorization flow failed: " + ex.getMessage());
            TanrabadApp.log(ex);
        } else {
            displayNotAuthorized("No authorization state retained - reauthorization required");
            TanrabadApp.log(new IllegalStateException("No authorization state retained"));
            logout();
        }
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        mAuthService.dispose();
        mExecutor.shutdownNow();
    }

    @MainThread private void displayNotAuthorized(String explanation) {
        ((TextView) findViewById(R.id.username)).setText(R.string.please_authen);
        Toast.makeText(this, explanation, Toast.LENGTH_SHORT).show();
        restartToLoginActivity();
    }

    private void restartToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @MainThread private void displayAuthorized() {
        Log.i(TAG, "Display Authorized");
        authenButton.setVisibility(View.VISIBLE);
        logoutButton.setVisibility(View.VISIBLE);
        UserProfile userInfo = mUserInfoJson.get();
        if (userInfo != null) {
            ((TextView) findViewById(R.id.username)).setText(userInfo.userName);
            ((TextView) findViewById(R.id.user_fullname)).setText(userInfo.name);
            String orgName = userInfo.orgName;
            if (!TextUtils.isEmpty(orgName)) {
                ((TextView) findViewById(R.id.organization)).setText(userInfo.orgName);
            } else {
                ((TextView) findViewById(R.id.organization)).setText("ไม่ระบุหน่วยงาน");
            }
            mUserStateManager = new UserStateManager(userInfo);
            TextView status = findViewById(R.id.status);
            status.setText(mUserStateManager.getUserStateRes());
            if (mUserStateManager.isRequireAction()) {
                status.setTextColor(ContextCompat.getColor(this, R.color.black));
                mUserStateManager.performRequireAction(this);
                authenButton.setText(R.string.check_status);
            } else {
                status.setTextColor(ContextCompat.getColor(this, R.color.without_larvae));
                authenButton.setText(R.string.login);
                String action = getIntent().getAction();
                if (AUTH_ACTION_AUTO_LOGIN.equals(action)) {
                    authenButton.performClick(); //automatic login to app
                }
            }
        } else {
            Toast.makeText(this, "เกิดข้อผิดพลาด ไม่สามารถดึงข้อมูลผู้ใช้ได้", Toast.LENGTH_SHORT)
                .show();
            restartToLoginActivity();
        }
    }

    @MainThread
    private void exchangeAuthorizationCode(AuthorizationResponse authorizationResponse) {
        Log.i(TAG, "Request to Exchange token");
        final TokenRequest request = authorizationResponse.createTokenExchangeRequest();
        performTokenRequest(request, (tokenResponse, authException) -> {
            mStateManager.updateAfterTokenResponse(tokenResponse, authException);
            if (!mStateManager.getCurrent().isAuthorized()) {
                final String message =
                    "Authorization Code exchange failed - " + ((authException != null)
                        ? authException.error : "");
                Log.e(TAG, message);
                runOnUiThread(() -> displayNotAuthorized(message));
            } else {
                mStateManager.getCurrent()
                    .performActionWithFreshTokens(mAuthService, this::fetchUserInfo);
            }
        });
    }

    @MainThread private void performTokenRequest(TokenRequest request,
        AuthorizationService.TokenResponseCallback callback) {
        ClientAuthentication client = new ClientSecretBasic(BuildConfig.TRB_AUTHEN_CLIENT_SECRET);
        mAuthService.performTokenRequest(request, client, callback);
    }

    @MainThread private void fetchUserInfo(final String accessToken, String idToken,
        AuthorizationException ex) {
        Log.d(TAG, String.format("fetchUserInfo with accessToken=%s idToken=%s", accessToken, idToken));
        if (ex != null) {
            Log.e(TAG, "Token refresh failed when fetching user info");
            TanrabadApp.log(ex);
            mUserInfoJson.set(null);
            runOnUiThread(() -> displayNotAuthorized("ไม่สามารถแลกเปลี่ยนข้อมูลกับ Server ได้"));
            return;
        }

        AuthorizationServiceDiscovery discovery =
            mStateManager.getCurrent().getAuthorizationServiceConfiguration().discoveryDoc;

        final URL userInfoEndpoint;
        try {
            Uri userinfoEndpoint = discovery.getUserinfoEndpoint();
            Log.i(TAG, "Request user profile at " + userinfoEndpoint);
            userInfoEndpoint = new URL(userinfoEndpoint.toString());
        } catch (MalformedURLException urlEx) {
            Log.e(TAG, "Failed to construct user info endpoint URL", urlEx);
            TanrabadApp.log(urlEx);
            mUserInfoJson.set(null);
            runOnUiThread(() -> displayNotAuthorized("ไม่สามารถดึงข้อมูลผู้ใช้ได้"));
            return;
        }

        mExecutor.submit(() -> {
            try {
                HttpURLConnection conn = (HttpURLConnection) userInfoEndpoint.openConnection();
                conn.setRequestProperty("Authorization", "Bearer " + accessToken);
                conn.setInstanceFollowRedirects(false);
                String response = Okio.buffer(Okio.source(conn.getInputStream()))
                    .readString(Charset.forName("UTF-8"));
                mUserInfoJson.set(UserProfile.fromJson(response));
                mUserManager.save(mUserInfoJson.get());
            } catch (IOException ioEx) {
                Log.e(TAG, "Network error when querying userinfo endpoint", ioEx);
                TokenActivity.this.showSnackbar("Fetching user info failed");
            }
            runOnUiThread(this::displayAuthorized);
        });
    }

    @MainThread private void showSnackbar(String message) {
        Snackbar.make(findViewById(R.id.coordinator), message, Snackbar.LENGTH_SHORT).show();
    }

    @MainThread private void logout() {
        // discard the authorization and token state, but retain the configuration and
        // dynamic client registration (if applicable), to save from retrieving them again.
        auth.logout();
        finish();
    }
}
