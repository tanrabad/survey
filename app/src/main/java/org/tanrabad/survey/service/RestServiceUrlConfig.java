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

package org.tanrabad.survey.service;

import org.tanrabad.survey.BuildConfig;
import org.tanrabad.survey.entity.User;
import org.tanrabad.survey.presenter.AccountUtils;

public final class RestServiceUrlConfig implements RestServiceUrl {

    private static final RestServiceUrlConfig instance = new RestServiceUrlConfig();

    private RestServiceUrlConfig() {
    }

    public static RestServiceUrlConfig getInstance() {
        return instance;
    }

    @Override
    public void setApiEndPointByUser(User user) {
        if (!AccountUtils.isTrialUser(user)) {
            AbsRestService.setBaseApi(BuildConfig.API_URL);
        } else {
            AbsRestService.setBaseApi(BuildConfig.API_BASE_URL_TEST);
        }
    }
}
