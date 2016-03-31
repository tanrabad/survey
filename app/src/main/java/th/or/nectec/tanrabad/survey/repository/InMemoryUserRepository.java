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

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import th.or.nectec.tanrabad.domain.user.UserRepository;
import th.or.nectec.tanrabad.domain.user.UserRepositoryException;
import th.or.nectec.tanrabad.entity.User;

final class InMemoryUserRepository implements UserRepository {

    public static final String TRIAL_DEBUG = "trial-debug";
    public static final String TRIAL_BETA = "trial-beta";
    public static final String TRIAL_RELEASE = "trial-release";
    private static final String DEV_USERNAME = "dpc-user";
    private static final String DEMO_USERNAME = "dpc-13-beta";
    private static final String RELEASE_USERNAME = "dpc-13";
    private static InMemoryUserRepository instance;
    private Map<String, User> userMapping = new ConcurrentHashMap<>();

    private InMemoryUserRepository() {
        userMapping.put(DEV_USERNAME, devUser());
        userMapping.put(DEMO_USERNAME, betaUser());
        userMapping.put(RELEASE_USERNAME, ReleaseUser());
        userMapping.put(TRIAL_DEBUG, trialDebugUser());
        userMapping.put(TRIAL_BETA, trialDebugUser());
        userMapping.put(TRIAL_RELEASE, trialReleaseUser());
    }

    private User devUser() {
        User dev = new User(DEV_USERNAME);
        dev.setFirstname("ซาร่า");
        dev.setLastname("คิดส์");
        dev.setEmail("dev@tanrabad.org");
        dev.setOrganizationId(1);
        dev.setHealthRegionCode("dpc-13");
        dev.setApiFilter("hr_code=dpc-13");
        return dev;
    }

    private User betaUser() {
        User beta = new User(DEMO_USERNAME);
        beta.setFirstname("ทดสอบ");
        beta.setLastname("ทดสอบ");
        beta.setEmail("dpc13@gmail.com");
        beta.setOrganizationId(5);
        beta.setHealthRegionCode("dpc-13");
        beta.setApiFilter("hr_code=dpc-13");
        return beta;
    }

    private User ReleaseUser() {
        User release = new User(RELEASE_USERNAME);
        release.setFirstname("ทดสอบ");
        release.setLastname("ทดสอบ");
        release.setEmail("dpc13@gmail.com");
        release.setOrganizationId(100);
        release.setHealthRegionCode("dpc-13");
        release.setApiFilter("hr_code=dpc-13");
        return release;
    }

    private User trialDebugUser() {
        User release = new User(TRIAL_DEBUG);
        release.setFirstname("ทดสอบ");
        release.setLastname("ทดสอบ");
        release.setEmail("dpc13@gmail.com");
        release.setOrganizationId(100);
        release.setHealthRegionCode("dpc-13");
        release.setApiFilter("hr_code=dpc-13");
        return release;
    }

    private User trialReleaseUser() {
        User release = new User(TRIAL_RELEASE);
        release.setFirstname("ทดสอบ");
        release.setLastname("ทดสอบ");
        release.setEmail("dpc13@gmail.com");
        release.setOrganizationId(100);
        release.setHealthRegionCode("dpc-13");
        release.setApiFilter("hr_code=dpc-13");
        return release;
    }

    protected static InMemoryUserRepository getInstance() {
        if (instance == null) {
            instance = new InMemoryUserRepository();
        }
        return instance;
    }

    private User trialBetaUser() {
        User release = new User(TRIAL_BETA);
        release.setFirstname("ทดสอบ");
        release.setLastname("ทดสอบ");
        release.setEmail("dpc13@gmail.com");
        release.setOrganizationId(5);
        release.setHealthRegionCode("dpc-13");
        release.setApiFilter("hr_code=dpc-13");
        return release;
    }

    @Override
    public User findByUsername(String userName) {
        if (userMapping.containsKey(userName)) {
            return userMapping.get(userName);
        } else {
            return null;
        }
    }

    @Override
    public boolean save(User user) {
        if (userMapping.containsKey(user.getUsername())) {
            throw new UserRepositoryException();
        }
        userMapping.put(user.getUsername(), user);
        return true;
    }

    @Override
    public boolean update(User user) {
        if (!userMapping.containsKey(user.getUsername())) {
            throw new UserRepositoryException();
        }
        userMapping.put(user.getUsername(), user);
        return true;
    }

    @Override
    public boolean delete(User user) {
        if (!userMapping.containsKey(user.getUsername())) {
            throw new UserRepositoryException();
        }
        userMapping.remove(user.getUsername());
        return true;
    }

    @Override
    public void updateOrInsert(List<User> update) {
    }
}
