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

import th.or.nectec.tanrabad.domain.user.UserRepository;
import th.or.nectec.tanrabad.entity.User;
import th.or.nectec.tanrabad.survey.TanrabadApp;
import th.or.nectec.tanrabad.survey.repository.persistence.DbUserRepository;

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
        User place = cache.findByUsername(username);
        if (place == null) {
            place = persistence.findByUsername(username);
            cache.save(place);
        }
        return place;
    }

    @Override
    public boolean save(User place) {
        boolean success = persistence.save(place);
        if (success) {
            cache.save(place);
        }
        return success;
    }

    @Override
    public boolean update(User place) {
        boolean success = persistence.update(place);
        if (success) {
            cache.update(place);
        }
        return success;
    }

    @Override
    public void updateOrInsert(List<User> users) {

    }
}
