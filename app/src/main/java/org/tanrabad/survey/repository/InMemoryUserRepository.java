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

package org.tanrabad.survey.repository;

import org.tanrabad.survey.domain.user.UserRepository;
import org.tanrabad.survey.domain.user.UserRepositoryException;
import org.tanrabad.survey.entity.User;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

final class InMemoryUserRepository implements UserRepository {

    private static InMemoryUserRepository instance;
    private Map<String, User> userMapping = new ConcurrentHashMap<>();

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
        userMapping.put(user.getUsername(), user);
        return true;
    }

    @Override
    public boolean update(User user) {
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
