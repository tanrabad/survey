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

package th.or.nectec.tanrabad.survey.presenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import th.or.nectec.tanrabad.entity.User;
import th.or.nectec.tanrabad.survey.repository.BrokerUserRepository;
import th.or.nectec.tanrabad.survey.utils.time.CurrentTimer;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.when;

public class AccountUtilsTest {

    private User user;

    @Before
    public void setUp() throws Exception {
        user = BrokerUserRepository.getInstance().findByUsername("dpc-user");
    }

    @Test
    public void testGetUser() throws Exception {
        AccountUtils.setUser(user);

        assertEquals(user, AccountUtils.getUser());
    }

    @Test
    public void testGetLastUser() throws Exception {
        AccountUtils.setUser(user);

        assertEquals(user, AccountUtils.getLastLoginUser());
    }

    @Test
    public void testTrialUserMustNotSave() throws Exception {
        AccountUtils.setUser(User.fromUsername("trial-debug"));

        assertNull(AccountUtils.getLastLoginUser());
    }

    @Test
    public void testLastLoginOldThan1Day() throws Exception {
        CurrentTimer currentTimer = Mockito.mock(CurrentTimer.class);
        AccountUtils.currentTimer = currentTimer;
        when(currentTimer.getInMills())
                .thenReturn(AccountUtils.ONE_DAY_IN_MILLS,
                        AccountUtils.ONE_DAY_IN_MILLS * 2);

        AccountUtils.setUser(user);

        assertNull(AccountUtils.getLastLoginUser());
    }

    @Test
    public void testLoginIn24HourCanPassAuthentication() throws Exception {
        CurrentTimer currentTimer = Mockito.mock(CurrentTimer.class);
        AccountUtils.currentTimer = currentTimer;
        when(currentTimer.getInMills())
                .thenReturn(AccountUtils.ONE_DAY_IN_MILLS,
                        AccountUtils.ONE_DAY_IN_MILLS + 43200000L);  //+12 hours

        AccountUtils.setUser(user);

        assertEquals(user, AccountUtils.getLastLoginUser());
    }
}
