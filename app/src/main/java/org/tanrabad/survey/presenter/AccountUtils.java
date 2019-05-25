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
import org.tanrabad.survey.BuildConfig;
import org.tanrabad.survey.TanrabadApp;
import org.tanrabad.survey.entity.User;
import org.tanrabad.survey.repository.BrokerOrganizationRepository;
import org.tanrabad.survey.repository.BrokerUserRepository;
import org.tanrabad.survey.utils.time.CurrentTimer;
import org.tanrabad.survey.utils.time.JodaCurrentTime;

public final class AccountUtils {

    protected static final long ONE_DAY_IN_MILLS = 86400000L;
    protected static final long REMEMBER_LIMIT = ONE_DAY_IN_MILLS;


    protected static CurrentTimer currentTimer = new JodaCurrentTime();
    private static User user;

    private static UserStore lastLoginUserStore = new PreferenceLastLoginUserStore();
    private static UserStore runtimeUserStore = new RuntimeUserStore();

    public static User getUser() {
        if (AccountUtils.user == null) {
            if (BuildConfig.DEBUG)
                throw new IllegalStateException("user is null, please make sure to set user before call this");
            else
                AccountUtils.user = runtimeUserStore.getUser();
        }
        return AccountUtils.user;
    }

    public static void setUser(User user) {
        if (TanrabadApp.action() != null) {
            TanrabadApp.action().login(user);
        }
        AccountUtils.user = user;
        runtimeUserStore.save(user);
        lastLoginUserStore.save(user);
    }

    public static void login(final User user, final LoginThread.LoginListener loginListener) {
        Thread background = new Thread(new LoginThread(user, loginListener));
        background.start();
    }

    public static User getLastLoginUser() {
        return lastLoginUserStore.getUser();
    }

    public static boolean isTrialUser(User user) {
        if (user != null)
            return user.getUsername().startsWith("trial-");
        else
            return false;
    }

    public static void clear() {
        if (TanrabadApp.action() != null) {
            TanrabadApp.action().logout(user);
        }

        AccountUtils.user = null;
        lastLoginUserStore.clear();
        runtimeUserStore.clear();
    }

    public static void setLastLoginUserStore(UserStore repository) {
        lastLoginUserStore = repository;
    }

    public interface UserStore {
        void save(User user);

        User getUser();

        void clear();
    }

    private static class RuntimeUserStore implements UserStore {
        private static final String PREF_NAME = "user-runtime";
        private static final String KEY_USER = "user";

        @Override
        public void save(User user) {
            if (TanrabadApp.getInstance() == null) return;
            SharedPreferences.Editor editor = getSharedPreferences().edit();
            editor.putString(KEY_USER, user.getUsername());
            editor.apply();
        }

        private SharedPreferences getSharedPreferences() {
            return TanrabadApp.getInstance().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        }

        @Override
        public User getUser() {
            String username = getSharedPreferences().getString(KEY_USER, null);
            if (username == null)
                return null;
            User user = BrokerUserRepository.getInstance().findByUsername(username);
            user.setOrganization(BrokerOrganizationRepository.getInstance().findById(user.getOrganizationId()));
            return user;
        }

        @Override
        public void clear() {
            getSharedPreferences().edit().clear().apply();
        }
    }
}
