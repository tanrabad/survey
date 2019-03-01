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

import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import net.openid.appauth.AppAuthConfiguration;
import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.ClientSecretBasic;
import net.openid.appauth.RegistrationRequest;
import net.openid.appauth.RegistrationResponse;
import net.openid.appauth.ResponseTypeValues;
import net.openid.appauth.browser.AnyBrowserMatcher;
import net.openid.appauth.browser.BrowserMatcher;

import org.tanrabad.survey.R;
import org.tanrabad.survey.presenter.LoginActivity;
import org.tanrabad.survey.presenter.authen.AuthenticatorPresent;

import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public class AppAuthPresenter implements AuthenticatorPresent {

    private static final String TAG = "AppAuthPresenter";
    private AppCompatActivity activity;

    private static final String EXTRA_FAILED = "failed";
    private static final int RC_AUTH = 100;

    private AuthStateManager mAuthStateManager;
    private Configuration mConfiguration;
    private AuthorizationService mAuthService;
    private CountDownLatch mAuthIntentLatch = new CountDownLatch(1);

    private final AtomicReference<String> mClientId = new AtomicReference<>();
    private final AtomicReference<AuthorizationRequest> mAuthRequest = new AtomicReference<>();
    private final AtomicReference<CustomTabsIntent> mAuthIntent = new AtomicReference<>();

    private ExecutorService mExecutor;

    @NonNull
    private BrowserMatcher mBrowserMatcher = AnyBrowserMatcher.INSTANCE;

    public AppAuthPresenter(AppCompatActivity activity) {
        this.activity = activity;

        mExecutor = Executors.newSingleThreadExecutor();

        mAuthStateManager = AuthStateManager.getInstance(activity);
        mConfiguration = Configuration.getInstance(activity);

        if (isLoggedIn()) {
            return;
        }

        if (mConfiguration.hasConfigurationChanged()) {
            // discard any existing authorization state due to the change of configuration
            Log.i(TAG, "Configuration change detected, discarding old state");
            mAuthStateManager.replace(new AuthState());
            mConfiguration.acceptConfiguration();
        }

        mExecutor.execute(() -> initializeConfiguration());
    }

    private boolean isLoggedIn() {
        return mAuthStateManager.getCurrent().isAuthorized()
            && !mConfiguration.hasConfigurationChanged();
    }

    /**
     * Initializes the authorization service configuration if necessary, either from the local
     * static values or by retrieving an OpenID discovery document.
     */
    @WorkerThread
    private void initializeConfiguration() {
        Log.i(TAG, "Initializing AppAuth");
        recreateAuthorizationService();

        if (mAuthStateManager.getCurrent().getAuthorizationServiceConfiguration() != null) {
            // configuration is already created, skip to client initialization
            Log.i(TAG, "auth config already established");
            initializeClient();
            return;
        }

        // if we are not using discovery, build the authorization service configuration directly
        // from the static configuration values.
        if (mConfiguration.getDiscoveryUri() == null) {
            Log.i(TAG, "Creating auth config from res/raw/auth_config.json");
            AuthorizationServiceConfiguration config = new AuthorizationServiceConfiguration(
                mConfiguration.getAuthEndpointUri(),
                mConfiguration.getTokenEndpointUri(),
                mConfiguration.getRegistrationEndpointUri());
            mAuthStateManager.replace(new AuthState(config));
            initializeClient();
            return;
        }

        AuthorizationServiceConfiguration.fetchFromUrl(
            mConfiguration.getDiscoveryUri(),
            (config, ex) -> {
                if (config == null) {
                    Log.i(TAG, "Failed to retrieve discovery document", ex);
                    Toast.makeText(activity,
                        "Failed to retrieve discovery document: " + ex.getMessage(),
                        Toast.LENGTH_LONG).show();
                    return;
                }

                Log.i(TAG, "Discovery document retrieved");
                mAuthStateManager.replace(new AuthState(config));
                mExecutor.submit(() -> initializeClient());
            },
            mConfiguration.getConnectionBuilder());
    }

    /**
     * Initiates a dynamic registration request if a client ID is not provided by the static
     * configuration.
     */
    @WorkerThread
    private void initializeClient() {
        if (mConfiguration.getClientId() != null) {
            Log.i(TAG, "Using static client ID: " + mConfiguration.getClientId());
            // use a statically configured client ID
            mClientId.set(mConfiguration.getClientId());
            activity.runOnUiThread(() -> initializeAuthRequest());
            return;
        }

        RegistrationResponse lastResponse =
            mAuthStateManager.getCurrent().getLastRegistrationResponse();
        if (lastResponse != null) {
            Log.i(TAG, "Using dynamic client ID: " + lastResponse.clientId);
            // already dynamically registered a client ID
            mClientId.set(lastResponse.clientId);
            activity.runOnUiThread(() -> initializeAuthRequest());
            return;
        }

        Log.i(TAG, "Dynamically registering client");
        RegistrationRequest registrationRequest = new RegistrationRequest.Builder(
            mAuthStateManager.getCurrent().getAuthorizationServiceConfiguration(),
            Collections.singletonList(mConfiguration.getRedirectUri()))
            .setTokenEndpointAuthenticationMethod(ClientSecretBasic.NAME)
            .build();

        mAuthService.performRegistrationRequest(
            registrationRequest,
            (response, ex) -> {
                mAuthStateManager.updateAfterRegistration(response, ex);
                if (response == null) {
                    Log.i(TAG, "Failed to dynamically register client", ex);
                    Toast.makeText(activity,
                        "Failed to register client: " + ex.getMessage(),
                        Toast.LENGTH_SHORT
                    ).show();
                    return;
                }

                Log.i(TAG, "Dynamically registered client: " + response.clientId);
                mClientId.set(response.clientId);
                initializeAuthRequest();
            });
    }

    private void recreateAuthorizationService() {
        if (mAuthService != null) {
            Log.i(TAG, "Discarding existing AuthService instance");
            mAuthService.dispose();
        }
        mAuthService = createAuthorizationService();
        mAuthRequest.set(null);
        mAuthIntent.set(null);
    }

    private AuthorizationService createAuthorizationService() {
        Log.i(TAG, "Creating authorization service");
        AppAuthConfiguration.Builder builder = new AppAuthConfiguration.Builder();
        builder.setBrowserMatcher(mBrowserMatcher);
        builder.setConnectionBuilder(mConfiguration.getConnectionBuilder());

        return new AuthorizationService(activity, builder.build());
    }

    @MainThread
    private void initializeAuthRequest() {
        createAuthRequest();
        warmUpBrowser();
    }

    private void createAuthRequest() {
        String loginHint = "ทันระบาดสำรวจ";
        Log.i(TAG, "Creating auth request for login hint: " + loginHint);
        AuthorizationRequest.Builder authRequestBuilder = new AuthorizationRequest.Builder(
            mAuthStateManager.getCurrent().getAuthorizationServiceConfiguration(),
            mClientId.get(),
            ResponseTypeValues.CODE,
            mConfiguration.getRedirectUri())
            .setScope(mConfiguration.getScope());

        if (!TextUtils.isEmpty(loginHint)) {
            authRequestBuilder.setLoginHint(loginHint);
        }

        mAuthRequest.set(authRequestBuilder.build());
    }


    private void warmUpBrowser() {
        mAuthIntentLatch = new CountDownLatch(1);
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "Warming up browser instance for auth request");
                CustomTabsIntent.Builder intentBuilder =
                    mAuthService.createCustomTabsIntentBuilder(mAuthRequest.get().toUri());
                intentBuilder.setToolbarColor(ContextCompat.getColor(activity, R.color.purple_dark));
                mAuthIntent.set(intentBuilder.build());
                mAuthIntentLatch.countDown();
            }
        });
    }

    @Override
    public void close() {
        mExecutor.shutdownNow();
        if (mAuthService != null) {
            mAuthService.dispose();
        }
    }

    @Override
    public void startPage() {
        if (isLoggedIn())
            activity.startActivity(new Intent(activity, TokenActivity.class));
        else
            doAuth();
    }

    @WorkerThread
    private void doAuth() {
        try {
            mAuthIntentLatch.await();
        } catch (InterruptedException ex) {
            Log.w(TAG, "Interrupted while waiting for auth intent");
        }

        Intent completionIntent = new Intent(activity, TokenActivity.class);
        Intent cancelIntent = new Intent(activity, LoginActivity.class);
        cancelIntent.putExtra(EXTRA_FAILED, true);
        cancelIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        mAuthService.performAuthorizationRequest(
            mAuthRequest.get(),
            PendingIntent.getActivity(activity, 0, completionIntent, 0),
            PendingIntent.getActivity(activity, 0, cancelIntent, 0),
            mAuthIntent.get());
    }

}
