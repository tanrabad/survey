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

import android.content.Context;
import android.content.SharedPreferences;
import org.tanrabad.survey.TanrabadApp;
import org.tanrabad.survey.entity.User;
import org.tanrabad.survey.repository.BrokerUserRepository;

public class PreferenceLastLoginUserStore implements AccountUtils.UserStore {

    private static final String PREF_NAME = "user";
    private static final String KEY_USER = "lastLogin";
    private static final String KEY_TIMESTAMP = "lastLoginTimeStamp";

    @Override
    public void save(User user) {
        SharedPreferences.Editor editor = getUserPreference().edit();
        editor.putString(KEY_USER, user.getUsername());
        editor.putLong(KEY_TIMESTAMP, AccountUtils.currentTimer.getInMills());
        editor.apply();
    }

    private static SharedPreferences getUserPreference() {
        return TanrabadApp.getInstance().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public User getUser() {
        SharedPreferences userPreference = getUserPreference();
        String username = userPreference.getString(KEY_USER, null);
        long timestampInMills = userPreference.getLong(KEY_TIMESTAMP, 0);
        long currentInMills = AccountUtils.currentTimer.getInMills();

        if (currentInMills - timestampInMills < AccountUtils.REMEMBER_LIMIT) {
            return BrokerUserRepository.getInstance().findByUsername(username);
        } else {
            return null;
        }
    }

    @Override
    public void clear() {
        getUserPreference().edit().clear().apply();
    }
}
