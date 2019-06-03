/*
 * Copyright (c) 2016 NECTEC
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

package org.tanrabad.survey.presenter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import org.tanrabad.survey.BuildConfig;
import org.tanrabad.survey.R;
import org.tanrabad.survey.entity.User;
import org.tanrabad.survey.presenter.authen.Authenticator;
import org.tanrabad.survey.presenter.authen.ChromeCustomTabs;
import org.tanrabad.survey.presenter.authen.appauth.AppAuthPresenter;
import org.tanrabad.survey.presenter.authen.appauth.TokenActivity;
import org.tanrabad.survey.repository.BrokerOrganizationRepository;
import org.tanrabad.survey.repository.BrokerUserRepository;
import org.tanrabad.survey.utils.alert.Alert;
import org.tanrabad.survey.utils.android.InternetConnection;
import org.tanrabad.survey.utils.android.TwiceBackPressed;

import static android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN;
import static android.view.animation.AnimationUtils.loadAnimation;

public class LoginActivity extends TanrabadActivity {

    private static final String TAG = "LoginActivity";

    ProgressDialog progressDialog;
    private View trialButton;
    private View authenButton;
    private Authenticator auth;

    private TwiceBackPressed twiceBackPressed;

    @Override protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        twiceBackPressed = new TwiceBackPressed(this);

        auth = new Authenticator(new AppAuthPresenter(this));

        trialButton = findViewById(R.id.trial);
        authenButton = findViewById(R.id.authentication_button);

        trialButton.setOnClickListener(view -> trialLogin());
        authenButton.setOnClickListener(view -> authen());
        authenButton.setOnLongClickListener(this::forceAuthenWithServer);
        startAnimation();
    }

    private void trialLogin() {
        User user = BrokerUserRepository.getInstance().findByUsername(BuildConfig.TRIAL_USER);
        user.setOrganization(
            BrokerOrganizationRepository.getInstance().findById(user.getOrganizationId()));
        doLogin(user);
    }

    private void doLogin(User user) {
        trialButton.setEnabled(false);
        authenButton.setEnabled(false);
        progressDialog = new ProgressDialog(this, R.style.Dialog);
        progressDialog.setMessage(getString(R.string.login_loading));
        progressDialog.setCancelable(false);
        progressDialog.show();
        AccountUtils.login(user, new LoginThread.LoginListener() {
            @Override public void loginFinish() {
                progressDialog.dismiss();
                InitialActivity.open(LoginActivity.this);
                if (useDropInAnimation) {
                    overridePendingTransition(R.anim.drop_in, R.anim.drop_out);
                } else {
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
                finish();
            }

            @Override public void loginFail() {
                progressDialog.dismiss();
                trialButton.setEnabled(true);
                authenButton.setEnabled(true);
                Alert.highLevel().show(R.string.connect_internet_before_login);
            }
        });
    }

    private void authen() {
        User lastLoginUser = AccountUtils.getLastLoginUser();
        if (InternetConnection.isAvailable(this)) {
            if (ChromeCustomTabs.isSupported(this)) {
                auth.request();
            } else {
                ChromeCustomTabs.showInstallPromptDialog(this);
            }
        } else if (lastLoginUser != null && !AccountUtils.isTrialUser(lastLoginUser)) {
            doLogin(lastLoginUser);
        } else {
            Alert.highLevel().show(R.string.connect_internet_before_authen);
        }
    }

    private boolean forceAuthenWithServer(View view) {
        if (InternetConnection.isAvailable(this)) {
            if (ChromeCustomTabs.isSupported(this)) {
                auth.forceRequest();
            } else {
                ChromeCustomTabs.showInstallPromptDialog(this);
            }
        } else {
            Alert.highLevel().show(R.string.connect_internet_before_authen);
        }
        return true;
    }

    private void startAnimation() {
        findViewById(R.id.bg_blue).startAnimation(loadAnimation(this, R.anim.login_bg_blue));
        Animation dropIn = loadAnimation(this, R.anim.logo);
        dropIn.setStartOffset(1200);
        View logoTrb = findViewById(R.id.logo_tanrabad);
        logoTrb.startAnimation(dropIn);
        logoTrb.setOnLongClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SplashScreenActivity.class);
            startActivity(intent);
            return true;
        });
    }

    boolean useDropInAnimation = false;

    @Override protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String username = intent.getStringExtra(TokenActivity.USERNAME);
        useDropInAnimation = intent.getBooleanExtra(TokenActivity.AUTO_LOGIN, false);
        if (username != null) {
            Log.i(TAG, "Login as " + username);
            User user = BrokerUserRepository.getInstance().findByUsername(username);
            doLogin(user);
        }
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        auth.close();
    }

    @Override public void onBackPressed() {
        if (twiceBackPressed.onTwiceBackPressed()) {
            finishAffinity();
        }
    }
}
