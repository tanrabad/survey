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

package org.tanrabad.survey.presenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.tanrabad.survey.entity.User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AccountUtilsTest {

    private final AccountUtils.UserStore repository = Mockito.mock(AccountUtils.UserStore.class);

    @Before
    public void setUp() throws Exception {
        AccountUtils.setLastLoginUserStore(repository);
    }

    @Test
    public void testSetThenGet() throws Exception {
        User user = User.fromUsername("trial-release");
        AccountUtils.setUser(user);

        assertEquals(user, AccountUtils.getUser());
    }

    @Test
    public void testIsTrialUser() throws Exception {
        assertTrue(AccountUtils.isTrialUser(User.fromUsername("trial-release")));
    }

    @Test
    public void testNotTrialUser() throws Exception {
        assertFalse(AccountUtils.isTrialUser(User.fromUsername("dpc-user")));
    }


    @Test
    public void mustSaveUserToLastLoginRepoIfRealUser() throws Exception {
        User user = User.fromUsername("bangkok");

        AccountUtils.setUser(user);

        Mockito.verify(repository).save(user);
    }
}
