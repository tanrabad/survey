package org.tanrabad.survey.presenter;

import org.tanrabad.survey.BuildConfig;
import org.tanrabad.survey.TanrabadApp;
import org.tanrabad.survey.entity.User;
import org.tanrabad.survey.service.AbsRestService;
import org.tanrabad.survey.utils.android.Connection;
import org.tanrabad.survey.utils.android.InternetConnection;

public abstract class LoginController {



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
            AbsRestService.setApiEndPointByUser(repository.getLastLoginUser());
            syncAndClearData();
        }

        setUser(user);
        AbsRestService.setApiEndPointByUser(user);
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



    protected abstract void syncAndClearData();
}
