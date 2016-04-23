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

package org.tanrabad.survey.service;

import android.content.Context;
import android.content.SharedPreferences;

public class TrialModePreference {

    private static final String PREF_NAME = "trial-mode";
    private static final String TRIAL_MODE = "trial-mode-pref";
    private final Context context;

    public TrialModePreference(Context context) {
        this.context = context;
    }

    public static void clear(Context context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit().clear().apply();
    }

    public boolean isUsingTrialMode() {
        return getSharedPreferences().getBoolean(TRIAL_MODE, false);
    }

    public void setUsingTrialMode(boolean isTrialMode) {
        SharedPreferences.Editor spEditor = getSharedPreferences().edit();
        spEditor.putBoolean(TRIAL_MODE, isTrialMode);
        spEditor.apply();
    }

    private SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
}
