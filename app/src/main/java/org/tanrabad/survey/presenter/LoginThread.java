package org.tanrabad.survey.presenter;

import android.os.Handler;
import android.os.Message;

import org.tanrabad.survey.TanrabadApp;
import org.tanrabad.survey.entity.User;
import org.tanrabad.survey.job.UploadJobBuilder;
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
        if (isCanLogin(user)) {
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
