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

import th.or.nectec.tanrabad.domain.user.UserRepository;
import th.or.nectec.tanrabad.entity.User;
import th.or.nectec.tanrabad.survey.TanrabadApp;
import th.or.nectec.tanrabad.survey.repository.persistence.DbUserRepository;

import java.util.List;

public class BrokerUserRepository implements UserRepository {

    private static BrokerUserRepository instance;
    private UserRepository cache;
    private UserRepository persistence;

    protected BrokerUserRepository(UserRepository cache, UserRepository persistence) {
        this.cache = cache;
        this.persistence = persistence;
    }

    public static BrokerUserRepository getInstance() {
        if (instance == null)
            instance = new BrokerUserRepository(InMemoryUserRepository.getInstance(),
                    new DbUserRepository(TanrabadApp.getInstance()));
        return instance;
    }

    @Override
    public User findByUsername(String username) {
        User user = cache.findByUsername(username);
        if (user == null) {
            user = persistence.findByUsername(username);
            if (user != null) cache.save(user);
        }
        return user;
    }

    @Override
    public boolean save(User user) {
        boolean success = persistence.save(user);
        if (success) {
            cache.save(user);
        }
        return success;
    }

    @Override
    public boolean update(User user) {
        boolean success = persistence.update(user);
        if (success) {
            cache.update(user);
        }
        return success;
    }

    @Override
    public void updateOrInsert(List<User> users) {

    }
}
