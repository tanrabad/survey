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

import android.content.Context;
import android.content.SharedPreferences;

import th.or.nectec.tanrabad.entity.User;
import th.or.nectec.tanrabad.survey.BuildConfig;
import th.or.nectec.tanrabad.survey.TanrabadApp;
import th.or.nectec.tanrabad.survey.repository.BrokerUserRepository;
import th.or.nectec.tanrabad.survey.utils.time.CurrentTimer;
import th.or.nectec.tanrabad.survey.utils.time.JodaCurrentTime;

public final class AccountUtils {

    protected static final long ONE_DAY_IN_MILLS = 86400000L;
    protected static final long REMEMBER_LIMIT = ONE_DAY_IN_MILLS;

    protected static CurrentTimer currentTimer = new JodaCurrentTime();
    private static User user;

    private static LastLoginUserRepo lastLoginUserRepo = new PreferenceLastLoginUserRepo();

    private AccountUtils() {
    }

    public static boolean canAddOrEditVillage() {
        return getUser().getHealthRegionCode().equals("dpc-13");
    }

    public static User getUser() {
        if (AccountUtils.user == null && BuildConfig.DEBUG) {
            throw new IllegalStateException("user is null, please make sure to set user before call this");
        }
        return AccountUtils.user;
    }

    public static void setUser(User user) {
        AccountUtils.user = user;
        if (TanrabadApp.action() != null)
            TanrabadApp.action().login(AccountUtils.user);

        if (!isTrialUser(user)) {
            lastLoginUserRepo.userLogin(user);
        } else {
            lastLoginUserRepo.clear();
        }
    }

    public static boolean isTrialUser(User user) {
        return user.getUsername().startsWith("trial-");
    }

    public static void clear() {
        AccountUtils.user = null;
        lastLoginUserRepo.clear();
    }

    public static User getLastLoginUser() {
        return lastLoginUserRepo.getLastLoginUser();
    }

    public static void setLastLoginUserRepo(LastLoginUserRepo repository) {
        lastLoginUserRepo = repository;
    }

    public interface LastLoginUserRepo {
        void userLogin(User user);

        User getLastLoginUser();

        void clear();
    }

    private static class PreferenceLastLoginUserRepo implements LastLoginUserRepo {

        private static final String PREF_NAME = "user";
        private static final String KEY_USER = "lastLogin";
        private static final String KEY_TIMESTAMP = "lastLoginTimeStamp";

        @Override
        public void userLogin(User user) {
            SharedPreferences.Editor editor = getUserPreference().edit();
            editor.putString(KEY_USER, user.getUsername());
            editor.putLong(KEY_TIMESTAMP, currentTimer.getInMills());
            editor.apply();
        }

        private static SharedPreferences getUserPreference() {
            return TanrabadApp.getInstance().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        }

        @Override
        public User getLastLoginUser() {
            SharedPreferences userPreference = getUserPreference();
            String username = userPreference.getString(KEY_USER, null);
            long timestampInMills = userPreference.getLong(KEY_TIMESTAMP, 0);
            long currentInMills = currentTimer.getInMills();

            if (currentInMills - timestampInMills < REMEMBER_LIMIT) {
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
}
