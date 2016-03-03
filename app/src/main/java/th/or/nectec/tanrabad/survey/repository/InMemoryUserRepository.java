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

import java.util.HashMap;
import java.util.List;

import th.or.nectec.tanrabad.domain.UserRepository;
import th.or.nectec.tanrabad.domain.place.PlaceRepositoryException;
import th.or.nectec.tanrabad.entity.User;

public class InMemoryUserRepository implements UserRepository {
    public static InMemoryUserRepository instance;

    HashMap<String, User> userMapping = new HashMap<>();

    protected static InMemoryUserRepository getInstance() {
        if (instance == null) {
            instance = new InMemoryUserRepository();
        }
        return instance;
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
            throw new PlaceRepositoryException();
        }
        userMapping.put(user.getUsername(), user);
        return true;
    }

    @Override
    public boolean update(User user) {
        if (!userMapping.containsKey(user.getUsername())) {
            throw new PlaceRepositoryException();
        }
        userMapping.put(user.getUsername(), user);
        return true;
    }

    @Override
    public void updateOrInsert(List<User> update) {
    }
}
