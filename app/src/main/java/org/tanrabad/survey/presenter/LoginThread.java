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

import android.os.Handler;
import android.os.Message;

import org.tanrabad.survey.TanrabadApp;
import org.tanrabad.survey.entity.User;
import org.tanrabad.survey.job.UploadJobRunner;
import org.tanrabad.survey.utils.UserDataManager;

class LoginThread extends LoginController implements Runnable {
    private static final int SUCCESS = 1;
    private static final int FAIL = 0;
    private final User user;
    private final Handler handler;


    public LoginThread(final User user, LoginListener loginListener) {
        this.user = user;
        handler = new LoginHandler(loginListener);
    }

    @Override
    public void run() {
        if (login(user)) {
            handler.sendEmptyMessage(SUCCESS);
        } else {
            handler.sendEmptyMessage(FAIL);
        }
    }

    @Override
    protected void setUser(User user) {
        AccountUtils.setUser(user);
    }

    @Override
    protected void syncAndClearData() {
        try {
            UploadJobRunner.Builder builder = new UploadJobRunner.Builder();
            builder.placePostDataJob.execute();
            builder.buildingPostDataJob.execute();
            builder.surveyPostDataJob.execute();
            builder.placePutDataJob.execute();
            builder.buildingPutDataJob.execute();
            builder.surveyPutDataJob.execute();
            UserDataManager.clearAll(TanrabadApp.getInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public interface LoginListener {
        void loginFinish();

        void loginFail();
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
                    loginListener.loginFinish();
                    break;
                case FAIL:
                    loginListener.loginFail();
                    break;
            }
        }
    }
}
