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

package org.tanrabad.survey.job;

import android.content.Context;

import org.tanrabad.survey.BuildConfig;
import org.tanrabad.survey.service.AbsRestService;

public class SetTrialModeAndSelectApiServerJob implements Job {

    public static final String TEST_URL = "http://trb-test.igridproject.info/v1";
    TrialModePreference trialModePreference;
    private boolean isTrialMode;

    public SetTrialModeAndSelectApiServerJob(Context context, boolean isTrialMode) {
        this.trialModePreference = new TrialModePreference(context);
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
