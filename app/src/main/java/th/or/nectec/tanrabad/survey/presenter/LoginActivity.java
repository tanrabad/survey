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
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.service.PlaceRestService;
import th.or.nectec.tanrabad.survey.service.ServiceLastUpdatePreference;
import th.or.nectec.tanrabad.survey.utils.alert.Alert;
import th.or.nectec.tanrabad.survey.utils.android.InternetConnection;

public class LoginActivity extends TanrabadActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        View logo = findViewById(R.id.logo_tabrabad);
        findViewById(R.id.authentication_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String placeTimeStamp = new ServiceLastUpdatePreference(LoginActivity.this, PlaceRestService.PATH).get();
                if (!InternetConnection.isAvailable(LoginActivity.this) && TextUtils.isEmpty(placeTimeStamp)) {
                    Alert.highLevel().show(R.string.connect_internet_when_use_for_first_time);
                } else {
                    openInitialActivity();
                    finish();
                }
            }
        });
    }

    private void openInitialActivity() {
        Intent intent = new Intent(this, InitialActivity.class);
        startActivity(intent);
    }

    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}
