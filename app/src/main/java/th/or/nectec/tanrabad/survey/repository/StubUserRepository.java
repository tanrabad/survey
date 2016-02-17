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

package th.or.nectec.tanrabad.survey.repository;


import th.or.nectec.tanrabad.domain.UserRepository;
import th.or.nectec.tanrabad.entity.User;

import java.util.HashMap;

public class StubUserRepository implements UserRepository {

    public static final String DEV_USERNAME = "dpc-user";
    public static final String DEMO_USERNAME = "dpc-13-beta";
    public static final String RELEASE_USERNAME = "dpc-13";
    HashMap<String, User> userMapping;

    public StubUserRepository() {
        userMapping = new HashMap<>();
        userMapping.put(DEV_USERNAME, getDpcUser());
        userMapping.put(DEMO_USERNAME, getDpc13BetaUser());
        userMapping.put(RELEASE_USERNAME, getDpc13User());
    }

    private User getDpcUser() {
        User dpcUser = new User(DEV_USERNAME);
        dpcUser.setFirstname("ซาร่า");
        dpcUser.setLastname("คิดส์");
        dpcUser.setEmail("sara.k@gmail.com");
        dpcUser.setOrganizationId(1);
        dpcUser.setHealthRegionCode("dpc-13");
        return dpcUser;
    }

    private User getDpc13BetaUser() {
        User dpcUser = new User(DEMO_USERNAME);
        dpcUser.setFirstname("ทดสอบ");
        dpcUser.setLastname("ทดสอบ");
        dpcUser.setEmail("dpc13@gmail.com");
        dpcUser.setOrganizationId(1);
        dpcUser.setHealthRegionCode("dpc-13");
        return dpcUser;
    }

    private User getDpc13User() {
        User dpcUser = new User(RELEASE_USERNAME);
        dpcUser.setFirstname("ทดสอบ");
        dpcUser.setLastname("ทดสอบ");
        dpcUser.setEmail("dpc13@gmail.com");
        dpcUser.setOrganizationId(1);
        dpcUser.setHealthRegionCode("dpc-13");
        return dpcUser;
    }

    @Override
    public User findByUsername(String userName) {
        if (userMapping.containsKey(userName)) {
            return userMapping.get(userName);
        } else {
            return null;
        }
    }
}
