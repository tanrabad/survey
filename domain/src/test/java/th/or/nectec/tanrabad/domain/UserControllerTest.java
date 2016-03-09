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

package th.or.nectec.tanrabad.domain;


import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import th.or.nectec.tanrabad.domain.user.UserPresenter;
import th.or.nectec.tanrabad.domain.user.UserRepository;
import th.or.nectec.tanrabad.entity.User;

public class UserControllerTest {
    private final String userName = "ice";
    private final User user = User.fromUsername(userName);
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();
    private th.or.nectec.tanrabad.domain.user.UserRepository userRepository;
    private th.or.nectec.tanrabad.domain.user.UserPresenter userPresenter;

    @Before
    public void setUp() throws Exception {
        userRepository = context.mock(UserRepository.class);
        userPresenter = context.mock(UserPresenter.class);
    }

    @Test
    public void testFoundUser() throws Exception {

        context.checking(new Expectations() {
            {
                allowing(userRepository).findByUsername(userName);
                will(returnValue(user));
                oneOf(userPresenter).displayUserName(user);
            }
        });
        th.or.nectec.tanrabad.domain.user.UserController userController = new th.or.nectec.tanrabad.domain.user.UserController(userRepository, userPresenter);
        userController.showUserOf(userName);
    }

    @Test
    public void testNotFoundUser() throws Exception {

        context.checking(new Expectations() {
            {
                allowing(userRepository).findByUsername(userName);
                will(returnValue(null));
                oneOf(userPresenter).displayNotFoundUser();
            }
        });
        th.or.nectec.tanrabad.domain.user.UserController userController = new th.or.nectec.tanrabad.domain.user.UserController(userRepository, userPresenter);
        userController.showUserOf(userName);
    }
}

