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

package org.tanrabad.survey.utils.showcase;

import android.content.Context;
import android.content.SharedPreferences;

public class ShowcasePreference {
    private static final String PREF_NAME = "showcase";
    private static final String NEED_SHOWCASE_OPTION = "need-showcase";
    private Context context;

    public ShowcasePreference(Context context) {
        this.context = context;
    }

    public void save(boolean needShowcase) {
        SharedPreferences.Editor spEditor = getSharedPreferences().edit();
        spEditor.putBoolean(NEED_SHOWCASE_OPTION, needShowcase);
        spEditor.apply();
    }

    private SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public boolean get() {
        return false;
    }
}
