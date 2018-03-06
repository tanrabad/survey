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

package org.tanrabad.survey.base;


import org.junit.rules.ExternalResource;
import org.tanrabad.survey.entity.Organization;
import org.tanrabad.survey.entity.User;
import org.tanrabad.survey.presenter.AccountUtils;


public class SurveyAccountTestRule extends ExternalResource {

    private String username;

    public SurveyAccountTestRule() {
        username = "dpc-user";
    }

    public SurveyAccountTestRule(String username) {
        this.username = username;
    }

    @Override
    protected void before() throws Throwable {
        User user = User.fromUsername(username);
        Organization org = new Organization(100, "DCP");
        org.setHealthRegionCode("dpc-04");
        user.setOrganization(org);
        AccountUtils.setUser(user);
    }

}
