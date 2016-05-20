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

import org.tanrabad.survey.TanrabadApp;
import org.tanrabad.survey.entity.User;
import org.tanrabad.survey.repository.AppDataManager;
import org.tanrabad.survey.repository.DataManager;
import org.tanrabad.survey.service.ImpRestServiceConfig;
import org.tanrabad.survey.service.RestServiceConfig;
import org.tanrabad.survey.utils.android.Connection;
import org.tanrabad.survey.utils.android.InternetConnection;

public class LoginController {

    private final DataManager dataManager;
    private final RestServiceConfig restServiceConfig;
    private final Connection connection;
    private final AccountUtils.UserStore previousLoginUserStore;

    public LoginController() {
        this(new InternetConnection(TanrabadApp.getInstance()),
                new PreferenceLastLoginUserStore(),
                new AppDataManager(),
                ImpRestServiceConfig.getInstance());
    }

    public LoginController(Connection connection,
                           AccountUtils.UserStore previousLoginUserStore,
                           DataManager dataManager,
                           RestServiceConfig restServiceConfig) {
        this.connection = connection;
        this.previousLoginUserStore = previousLoginUserStore;
        this.dataManager = dataManager;
        this.restServiceConfig = restServiceConfig;
    }

    public boolean login(User user) {
        if (!connection.isAvailable() && isNewUser(user))
            return false;

        if (shouldUploadOldUserData(user)) {
            restServiceConfig.setApiBaseUrlByUser(previousLoginUserStore.getUser());
            dataManager.syncAndClearData();
        }

        setUser(user);
        restServiceConfig.setApiBaseUrlByUser(user);
        return true;
    }

    protected boolean isNewUser(User user) {
        return !user.equals(previousLoginUserStore.getUser());
    }

    protected void setUser(User user) {
        AccountUtils.setUser(user);
    }

    protected boolean shouldUploadOldUserData(User user) {
        return previousLoginUserStore.getUser() != null
                && user.getOrganizationId() != previousLoginUserStore.getUser().getOrganizationId();
    }
}
