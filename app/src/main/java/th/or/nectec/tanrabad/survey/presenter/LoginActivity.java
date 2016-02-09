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

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.TanrabadApp;
import th.or.nectec.tanrabad.survey.repository.StubUserRepository;
import th.or.nectec.tanrabad.survey.service.PlaceRestService;
import th.or.nectec.tanrabad.survey.service.ServiceLastUpdatePreference;
import th.or.nectec.tanrabad.survey.utils.alert.Alert;
import th.or.nectec.tanrabad.survey.utils.android.InternetConnection;

import static android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN;
import static android.view.animation.AnimationUtils.loadAnimation;

public class LoginActivity extends TanrabadActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.authentication_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFirstTime() && !InternetConnection.isAvailable(LoginActivity.this)) {
                    Alert.highLevel().show(R.string.connect_internet_when_use_for_first_time);
                    TanrabadApp.action().firstTimeWithoutInternet();
                } else {
                    AccountUtils.setUser(new StubUserRepository().findByUsername("dpc-13-beta"));
                    InitialActivity.open(LoginActivity.this);
                    finish();
                }
            }
        });
        startAnimation();
    }

    private boolean isFirstTime() {
        String placeTimeStamp = new ServiceLastUpdatePreference(LoginActivity.this, PlaceRestService.PATH).get();
        return TextUtils.isEmpty(placeTimeStamp);
    }

    private void startAnimation() {
        findViewById(R.id.bg_blue).startAnimation(loadAnimation(this, R.anim.login_bg_blue));
        Animation dropIn = loadAnimation(this, R.anim.logo);
        dropIn.setStartOffset(1200);
        findViewById(R.id.logo_tabrabad).startAnimation(dropIn);
    }
}
