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

import org.tanrabad.survey.BuildConfig;
import org.tanrabad.survey.TanrabadApp;
import org.tanrabad.survey.entity.User;
import org.tanrabad.survey.utils.time.CurrentTimer;
import org.tanrabad.survey.utils.time.JodaCurrentTime;

public final class AccountUtils {

    protected static final long ONE_DAY_IN_MILLS = 86400000L;
    protected static final long REMEMBER_LIMIT = ONE_DAY_IN_MILLS;


    protected static CurrentTimer currentTimer = new JodaCurrentTime();
    private static User user;

    private static LastLoginUserRepo lastLoginUserRepo = new PreferenceLastLoginUserRepo();

    public static User getUser() {
        if (AccountUtils.user == null && BuildConfig.DEBUG) {
            throw new IllegalStateException("user is null, please make sure to set user before call this");
        }
        return AccountUtils.user;
    }

    public static void setUser(User user) {
        if (TanrabadApp.action() != null) {
            TanrabadApp.action().login(user);
        }
        AccountUtils.user = user;
        lastLoginUserRepo.userLogin(user);
    }

    public static void login(final User user, final LoginThread.LoginListener loginListener) {
        Thread background = new Thread(new LoginThread(user, loginListener));
        background.start();
    }

    public static User getLastLoginUser() {
        return lastLoginUserRepo.getLastLoginUser();
    }

    public static boolean isTrialUser(User user) {
        return user.getUsername().startsWith("trial-");
    }

    public static void clear() {
        AccountUtils.user = null;
        lastLoginUserRepo.clear();
    }

    public static void setLastLoginUserRepo(LastLoginUserRepo repository) {
        lastLoginUserRepo = repository;
    }

    public interface LastLoginUserRepo {
        void userLogin(User user);

        User getLastLoginUser();

        void clear();
    }
}
