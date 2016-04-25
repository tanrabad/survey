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

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.widget.CheckBox;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import org.tanrabad.survey.BuildConfig;
import org.tanrabad.survey.R;
import org.tanrabad.survey.TanrabadApp;
import org.tanrabad.survey.entity.User;
import org.tanrabad.survey.job.AbsJobRunner;
import org.tanrabad.survey.job.DeleteUserDataJob;
import org.tanrabad.survey.job.SetTrialModeAndSelectApiServerJob;
import org.tanrabad.survey.job.UploadJobRunner;
import org.tanrabad.survey.presenter.authen.AuthenActivity;
import org.tanrabad.survey.repository.BrokerUserRepository;
import org.tanrabad.survey.service.PlaceRestService;
import org.tanrabad.survey.service.ServiceLastUpdatePreference;
import org.tanrabad.survey.service.TrialModePreference;
import org.tanrabad.survey.utils.alert.Alert;
import org.tanrabad.survey.utils.android.InternetConnection;
import org.tanrabad.survey.utils.showcase.ShowcasePreference;

import static android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN;
import static android.view.animation.AnimationUtils.loadAnimation;

public class LoginActivity extends TanrabadActivity {
    private static final int AUTHEN_REQUEST_CODE = 1232;
    private CheckBox needShowcase;
    private ShowcasePreference showcasePreference;
    private TrialModePreference trialModePreference;

    private GoogleApiClient appIndexClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        appIndexClient = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        setupPreferences();
        setupShowcaseOption();

        findViewById(R.id.trial).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                trialLogin();
            }
        });
        findViewById(R.id.authentication_button).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                openAuthenWeb();
            }
        });
        startAnimation();
    }

    private void setupPreferences() {
        trialModePreference = new TrialModePreference(this);
        showcasePreference = new ShowcasePreference(this);
    }

    private void setupShowcaseOption() {
        needShowcase = (CheckBox) findViewById(R.id.need_showcase);
        needShowcase.setChecked(showcasePreference.get());
    }

    private void trialLogin() {
        if (isFirstTime() && !InternetConnection.isAvailable(this)) {
            Alert.highLevel().show(R.string.connect_internet_when_use_for_first_time);
            TanrabadApp.action().firstTimeWithoutInternet();
            return;
        }

        if (!trialModePreference.isUsingTrialMode()) {
            if (!InternetConnection.isAvailable(this)) {
                Alert.highLevel().show(R.string.connect_internet_before_using_trial_mode);
                return;
            }
            AccountUtils.setUser(BrokerUserRepository.getInstance().findByUsername(BuildConfig.TRIAL_USER));
            AbsJobRunner jobRunner = new UploadJobRunner();
            jobRunner.addJob(new DeleteUserDataJob(this));
            jobRunner.addJob(new StartInitialActivityJob(this));
            jobRunner.start();
        } else {
            AccountUtils.setUser(BrokerUserRepository.getInstance().findByUsername(BuildConfig.TRIAL_USER));
            startInitialActivity();
        }
        showcasePreference.save(needShowcase.isChecked());
    }

    private void openAuthenWeb() {
        User lastLoginUser = AccountUtils.getLastLoginUser();
        if (lastLoginUser != null
                && !AccountUtils.isTrialUser(lastLoginUser)) {
            AccountUtils.setUser(lastLoginUser);
            InitialActivity.open(this);
            finish();
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
        View logoTrb = findViewById(R.id.logo_tabrabad);
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

    private boolean isFirstTime() {
        String placeTimeStamp = new ServiceLastUpdatePreference(this, PlaceRestService.PATH).get();
        return TextUtils.isEmpty(placeTimeStamp);
    }

    private void startInitialActivity() {
        InitialActivity.open(LoginActivity.this);
        overridePendingTransition(R.anim.drop_in, R.anim.drop_out);
        finish();
    }

    @Override
    protected void onStop() {
        appIndexClient.disconnect();
        AppIndex.AppIndexApi.end(appIndexClient, getAppIndexAction());
        super.onStop();
    }

    public Action getAppIndexAction() {
        Thing thing = new Thing.Builder()
                .setName("ทันระบาดสำรวจ")
                .setDescription("ประสบการณ์ใหม่ สำหรับการสำรวจลูกน้ำยุง")
                .setUrl(Uri.parse("http://www.tanrabad.org/survey"))
                .build();

        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(thing)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AUTHEN_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (trialModePreference.isUsingTrialMode()) {
                    AbsJobRunner jobRunner = new UploadJobRunner();
                    jobRunner.addJob(new DeleteUserDataJob(this));
                    jobRunner.addJob(new SetTrialModeAndSelectApiServerJob(this, false));
                    jobRunner.addJob(new StartInitialActivityJob(this));
                    jobRunner.start();
                } else {
                    startInitialActivity();
                }
                showcasePreference.save(needShowcase.isChecked());
            } else if (resultCode == AuthenActivity.RESULT_ERROR) {
                Alert.highLevel().show(R.string.authen_error_response);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        appIndexClient.connect();
        AppIndex.AppIndexApi.start(appIndexClient, getAppIndexAction());
    }

}
