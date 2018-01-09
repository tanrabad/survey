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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;

import org.tanrabad.survey.BuildConfig;
import org.tanrabad.survey.R;
import org.tanrabad.survey.entity.User;
import org.tanrabad.survey.presenter.authen.AuthenActivity;
import org.tanrabad.survey.repository.BrokerUserRepository;
import org.tanrabad.survey.utils.alert.Alert;
import org.tanrabad.survey.utils.android.InternetConnection;

import static android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN;
import static android.view.animation.AnimationUtils.loadAnimation;

public class LoginActivity extends TanrabadActivity {
    private static final int AUTHEN_REQUEST_CODE = 1232;
    ProgressDialog progressDialog;
    private View trialButton;
    private View authenButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        trialButton = findViewById(R.id.trial);
        authenButton = findViewById(R.id.authentication_button);

        trialButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                trialLogin();
            }
        });
        authenButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                openAuthenWeb();
            }
        });
        startAnimation();
    }

    private void trialLogin() {
        User user = BrokerUserRepository.getInstance().findByUsername(BuildConfig.TRIAL_USER);
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
            @Override
            public void loginFinish() {
                progressDialog.dismiss();
                InitialActivity.open(LoginActivity.this);
                overridePendingTransition(R.anim.drop_in, R.anim.drop_out);
                finish();
            }

            @Override
            public void loginFail() {
                progressDialog.dismiss();
                trialButton.setEnabled(true);
                authenButton.setEnabled(true);
                Alert.highLevel().show(R.string.connect_internet_before_login);
            }
        });
    }

    private void openAuthenWeb() {
        User lastLoginUser = AccountUtils.getLastLoginUser();
        if (lastLoginUser != null && !AccountUtils.isTrialUser(lastLoginUser)) {
            doLogin(lastLoginUser);
        } else if (InternetConnection.isAvailable(this)) {
            Intent intent = new Intent(this, AuthenActivity.class);
            startActivityForResult(intent, AUTHEN_REQUEST_CODE);
        } else {
            Alert.highLevel().show(R.string.connect_internet_before_authen);
        }
    }

    private void startAnimation() {
        findViewById(R.id.bg_blue).startAnimation(loadAnimation(this, R.anim.login_bg_blue));
        Animation dropIn = loadAnimation(this, R.anim.logo);
        dropIn.setStartOffset(1200);
        View logoTrb = findViewById(R.id.logo_tanrabad);
        logoTrb.startAnimation(dropIn);
        logoTrb.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SplashScreenActivity.class);
                startActivity(intent);
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AUTHEN_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                User user = BrokerUserRepository.getInstance().findByUsername(
                    data.getStringExtra(AuthenActivity.USERNAME));
                doLogin(user);
            } else if (resultCode == AuthenActivity.RESULT_ERROR) {
                Alert.highLevel().show(R.string.authen_error_response);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
