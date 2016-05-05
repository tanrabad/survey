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

import org.junit.Test;
import org.tanrabad.survey.BuildConfig;
import org.tanrabad.survey.entity.User;

import static org.junit.Assert.assertEquals;


public class ImpRestServiceConfigTest {


    private RestServiceConfig restServiceConfigImp = ImpRestServiceConfig.getInstance();

    @Test
    public void testTrialUserMustSetTestApiEndpoint() throws Exception {
        restServiceConfigImp.setApiBaseUrlByUser(trialUser());

        assertEquals(BuildConfig.API_BASE_URL_TEST, restServiceConfigImp.getApiBaseUrl());
    }

    private User trialUser() {
        User user = User.fromUsername("trial-debug");
        user.setOrganizationId(999);
        return user;
    }

    @Test
    public void testAuthenUserMustSetApiEndpointByBuildConfig() throws Exception {
        restServiceConfigImp.setApiBaseUrlByUser(odpc13User1());

        assertEquals(BuildConfig.API_BASE_URL, restServiceConfigImp.getApiBaseUrl());
    }

    private User odpc13User1() {
        User user = User.fromUsername("user1");
        user.setOrganizationId(13);
        return user;
    }
}
