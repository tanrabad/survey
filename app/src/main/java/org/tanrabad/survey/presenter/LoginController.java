package org.tanrabad.survey.presenter;

import org.tanrabad.survey.BuildConfig;
import org.tanrabad.survey.TanrabadApp;
import org.tanrabad.survey.entity.User;
import org.tanrabad.survey.service.AbsRestService;
import org.tanrabad.survey.utils.android.Connection;
import org.tanrabad.survey.utils.android.InternetConnection;

public abstract class LoginController {

    public static final String TEST_URL = "http://trb-test.igridproject.info/v1";

    private final Connection connection;
    private final AccountUtils.LastLoginUserRepo repository;

    public LoginController() {
        this(new InternetConnection(TanrabadApp.getInstance()), new PreferenceLastLoginUserRepo());
    }

    public LoginController(Connection connection, AccountUtils.LastLoginUserRepo repository) {
        this.connection = connection;
        this.repository = repository;
    }

    public boolean login(User user) {
        if (!connection.isAvailable() && isNewUser(user))
            return false;

        if (shouldUploadOldUserData(user)) {
            setApiEndPointByUser(repository.getLastLoginUser());
            syncAndClearData();
        }

        setUser(user);
        setApiEndPointByUser(user);
        return true;
    }

    protected boolean isNewUser(User user) {
        return !user.equals(repository.getLastLoginUser());
    }

    protected abstract void setUser(User user);

    protected boolean shouldUploadOldUserData(User user) {
        return repository.getLastLoginUser() != null
                && user.getOrganizationId() != repository.getLastLoginUser().getOrganizationId();
    }

    protected void setApiEndPointByUser(User user) {
        if (!AccountUtils.isTrialUser(user)) {
            AbsRestService.setBaseApi(BuildConfig.API_URL);
        } else {
            AbsRestService.setBaseApi(TEST_URL);
        }
    }

    protected abstract void syncAndClearData();
}
