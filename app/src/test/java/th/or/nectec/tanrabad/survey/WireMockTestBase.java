/*
 * Copyright (c) 2015 NECTEC
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

package th.or.nectec.tanrabad.survey;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

import org.junit.Before;
import org.junit.ClassRule;

import th.or.nectec.tanrabad.entity.User;
import th.or.nectec.tanrabad.survey.presenter.AccountUtils;

public class WireMockTestBase {

    static final String IF_MODIFIED_SINCE = "if-Modified-Since";
    static final String LAST_UPDATE = "Last-Update";
    static final String CONTENT_LENGTH = "Content-Length";

    private static final int HTTP_PORT = 8089;
    static final String HTTP_LOCALHOST_8089 = "http://localhost:" + HTTP_PORT;

    @ClassRule
    public static WireMockRule wireMockRule = new WireMockRule(HTTP_PORT);

    @Before
    public void setUp() throws Exception {
        AccountUtils.setUser(stubUser());
    }

    protected User stubUser() {
        User user = new User("dpc-user");
        user.setHealthRegionCode("dpc-4");
        return user;
    }

    protected String localHost() {
        return HTTP_LOCALHOST_8089;
    }
}
