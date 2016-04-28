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
import android.os.Handler;
import android.os.Message;

import org.tanrabad.survey.BuildConfig;
import org.tanrabad.survey.TanrabadApp;
import org.tanrabad.survey.entity.User;
import org.tanrabad.survey.job.SetTrialModeAndSelectApiServerJob;
import org.tanrabad.survey.job.UploadJobBuilder;
import org.tanrabad.survey.repository.BrokerUserRepository;
import org.tanrabad.survey.service.AbsRestService;
import org.tanrabad.survey.utils.UserDataManager;
import org.tanrabad.survey.utils.android.InternetConnection;
import org.tanrabad.survey.utils.time.CurrentTimer;
import org.tanrabad.survey.utils.time.JodaCurrentTime;

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
        if (TanrabadApp.action() != null) {
            TanrabadApp.action().login(user);
        }
        AccountUtils.user = user;
        lastLoginUserRepo.userLogin(user);
    }

    public static void login(final User user, final LoginListener loginListener) {
        Thread background = new Thread(new LoginThread(user, loginListener));

        background.start();
    }


    private static boolean shouldUploadOldUserData(User user) {
        if (getLastLoginUser() == null)
            return false;

        return user.getOrganizationId() != getLastLoginUser().getOrganizationId();
    }

    public static User getLastLoginUser() {
        return lastLoginUserRepo.getLastLoginUser();
    }

    private static void setApiEndPointByUser(User user) {
        if (!isTrialUser(user)) {
            AbsRestService.setBaseApi(BuildConfig.API_URL);
        } else {
            AbsRestService.setBaseApi(SetTrialModeAndSelectApiServerJob.TEST_URL);
        }
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

    public interface LoginListener {
        void loginFinish(User user);

        void loginFail();
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

    private static class LoginThread implements Runnable {
        private static final int SUCCESS = 1;
        private static final int FAIL = 0;
        private final User user;
        private final Handler handler;


        public LoginThread(final User user, LoginListener loginListener) {
            this.user = user;
            handler = new LoginHandler(loginListener);
        }

        public void run() {
            if (InternetConnection.isAvailable(TanrabadApp.getInstance())) {
                if (shouldUploadOldUserData(user)) {
                    setApiEndPointByUser(getLastLoginUser());
                    syncAndClearData();
                }
            } else if (!user.equals(getLastLoginUser())) {
                handler.sendEmptyMessage(FAIL);
                return;
            }

            setUser(user);
            setApiEndPointByUser(user);
            handler.sendEmptyMessage(SUCCESS);
        }

        private void syncAndClearData() {
            try {
                UploadJobBuilder uploadJobBuilder = new UploadJobBuilder();
                uploadJobBuilder.placePostDataJob.execute();
                uploadJobBuilder.buildingPostDataJob.execute();
                uploadJobBuilder.surveyPostDataJob.execute();
                uploadJobBuilder.placePutDataJob.execute();
                uploadJobBuilder.buildingPutDataJob.execute();
                uploadJobBuilder.surveyPutDataJob.execute();
                UserDataManager.clearAll(TanrabadApp.getInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class LoginHandler extends Handler {
        private static final int SUCCESS = 1;
        private static final int FAIL = 0;
        private final LoginListener loginListener;

        public LoginHandler(LoginListener loginListener) {
            this.loginListener = loginListener;
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    loginListener.loginFinish(user);
                    break;
                case FAIL:
                    loginListener.loginFail();
                    break;
            }
        }
    }
}
