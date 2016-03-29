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

package th.or.nectec.tanrabad.survey.presenter;

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

import th.or.nectec.tanrabad.survey.BuildConfig;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.TanrabadApp;
import th.or.nectec.tanrabad.survey.job.AbsJobRunner;
import th.or.nectec.tanrabad.survey.job.Job;
import th.or.nectec.tanrabad.survey.job.SyncJobRunner;
import th.or.nectec.tanrabad.survey.presenter.authen.AuthenActivity;
import th.or.nectec.tanrabad.survey.repository.BrokerUserRepository;
import th.or.nectec.tanrabad.survey.service.AbsRestService;
import th.or.nectec.tanrabad.survey.service.PlaceRestService;
import th.or.nectec.tanrabad.survey.service.ServiceLastUpdatePreference;
import th.or.nectec.tanrabad.survey.service.TrialModePreference;
import th.or.nectec.tanrabad.survey.utils.ClearDataManager;
import th.or.nectec.tanrabad.survey.utils.alert.Alert;
import th.or.nectec.tanrabad.survey.utils.android.InternetConnection;
import th.or.nectec.tanrabad.survey.utils.showcase.ShowcasePreference;

import static android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN;
import static android.view.animation.AnimationUtils.loadAnimation;

public class LoginActivity extends TanrabadActivity {
    public static final String TEST_URL = "http://trb-test.igridproject.info/v1";
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
        findViewById(R.id.about).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                openAboutActivity();
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
            AbsJobRunner jobRunner = new SyncJobRunner();
            jobRunner.addJob(new DeleteJob());
            jobRunner.addJob(new SetTrialModeAndSelectApiServerJob(true));
            jobRunner.addJob(new StartInitialActivityJob());
            jobRunner.start();
        } else {
            AccountUtils.setUser(BrokerUserRepository.getInstance().findByUsername(BuildConfig.TRIAL_USER));
            startInitialActivity();
        }
        showcasePreference.save(needShowcase.isChecked());
    }

    private boolean isFirstTime() {
        String placeTimeStamp = new ServiceLastUpdatePreference(this, PlaceRestService.PATH).get();
        return TextUtils.isEmpty(placeTimeStamp);
    }

    private void startInitialActivity() {
        InitialActivity.open(LoginActivity.this);
        finish();
    }

    private void openAuthenWeb() {
        if (AccountUtils.getLastLoginUser() != null
                && !AccountUtils.isTrialUser(AccountUtils.getLastLoginUser())) {
            InitialActivity.open(this);
            finish();
        } else if (InternetConnection.isAvailable(this)) {
            Intent intent = new Intent(this, AuthenActivity.class);
            startActivityForResult(intent, AUTHEN_REQUEST_CODE);
        } else {
            Alert.highLevel().show(R.string.connect_internet_before_authen);
        }
    }

    private void openAboutActivity() {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    private void startAnimation() {
        findViewById(R.id.bg_blue).startAnimation(loadAnimation(this, R.anim.login_bg_blue));
        Animation dropIn = loadAnimation(this, R.anim.logo);
        dropIn.setStartOffset(1200);
        findViewById(R.id.logo_tabrabad).startAnimation(dropIn);
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
        if (requestCode == AUTHEN_REQUEST_CODE && resultCode == RESULT_OK) {
            if (trialModePreference.isUsingTrialMode()) {
                AbsJobRunner jobRunner = new SyncJobRunner();
                jobRunner.addJob(new DeleteJob());
                jobRunner.addJob(new SetTrialModeAndSelectApiServerJob(false));
                jobRunner.addJob(new StartInitialActivityJob());
                jobRunner.start();
            } else {
                startInitialActivity();
            }
            showcasePreference.save(needShowcase.isChecked());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        appIndexClient.connect();
        AppIndex.AppIndexApi.start(appIndexClient, getAppIndexAction());
    }

    class DeleteJob implements Job {

        @Override
        public int id() {
            return 77777;
        }

        @Override
        public void execute() throws Exception {
            ClearDataManager.clearAll(LoginActivity.this);
        }
    }

    class SetTrialModeAndSelectApiServerJob implements Job {

        private boolean isTrialMode;

        public SetTrialModeAndSelectApiServerJob(boolean isTrialMode) {
            this.isTrialMode = isTrialMode;
        }

        @Override
        public int id() {
            return 88888;
        }

        @Override
        public void execute() throws Exception {
            trialModePreference.setUsingTrialMode(isTrialMode);
            if (isTrialMode) {
                AbsRestService.setBaseApi(TEST_URL);
            } else {
                AbsRestService.setBaseApi(BuildConfig.API_URL);
            }
        }
    }

    class StartInitialActivityJob implements Job {

        @Override
        public int id() {
            return 99999;
        }

        @Override
        public void execute() throws Exception {
            startInitialActivity();
        }
    }
}
