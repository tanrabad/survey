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
import org.tanrabad.survey.entity.User;
import org.tanrabad.survey.repository.DataManager;
import org.tanrabad.survey.service.RestServiceConfig;
import org.tanrabad.survey.utils.android.Connection;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class LoginControllerTest {

    private final Connection connection = mock(Connection.class);
    private final DataManager dataManager = mock(DataManager.class);
    private final RestServiceConfig restServiceConfig = mock(RestServiceConfig.class);
    private final AccountUtils.LastLoginUserRepo repository = mock(AccountUtils.LastLoginUserRepo.class);
    LoginController loginController = spy(
            new LoginController(connection, repository, dataManager, restServiceConfig));

    @Before
    public void setUp() throws Exception {
        AccountUtils.setLastLoginUserRepo(repository);
    }

    @Test
    public void testLoginSameOrgIdAndConnectedInternetMustSuccess() throws Exception {
        when(connection.isAvailable()).thenReturn(true);
        when(repository.getLastLoginUser()).thenReturn(odpc13User2());

        User user = odpc13User1();
        assertTrue(loginController.login(user));
        verify(loginController).setUser(user);
        verify(dataManager, never()).syncAndClearData();
        verify(restServiceConfig).setApiBaseUrlByUser(user);
    }

    private User odpc13User1() {
        User user = User.fromUsername("user1");
        user.setOrganizationId(13);
        return user;
    }

    private User odpc13User2() {
        User user = User.fromUsername("user2");
        user.setOrganizationId(13);
        return user;
    }

    @Test
    public void testLoginDifferentOrgIdAndConnectedInternetMustSuccess() throws Exception {
        User odpc11Hello = odpc11Hello();
        when(connection.isAvailable()).thenReturn(true);
        when(repository.getLastLoginUser()).thenReturn(odpc11Hello);

        User odpc13User1 = odpc13User1();
        assertTrue(loginController.login(odpc13User1));
        verify(restServiceConfig).setApiBaseUrlByUser(odpc11Hello);
        verify(dataManager).syncAndClearData();
        verify(loginController).setUser(odpc13User1);
        verify(restServiceConfig).setApiBaseUrlByUser(odpc13User1);
    }

    private User odpc11Hello() {
        User user = User.fromUsername("hello");
        user.setOrganizationId(11);
        return user;
    }

    @Test
    public void testIsNewUser() throws Exception {
        when(repository.getLastLoginUser()).thenReturn(odpc13User2());

        assertTrue(loginController.isNewUser(odpc13User1()));
        assertFalse(loginController.isNewUser(odpc13User2()));
    }

    @Test
    public void testShouldUploadNewData() throws Exception {
        when(repository.getLastLoginUser()).thenReturn(odpc13User2());

        assertTrue(loginController.shouldUploadOldUserData(odpc11Hello()));
        assertFalse(loginController.shouldUploadOldUserData(odpc13User1()));
    }

    @Test
    public void testLoginSameUserWithoutInternetMustSuccess() throws Exception {
        User stubUser = odpc13User1();
        when(connection.isAvailable()).thenReturn(false);
        when(repository.getLastLoginUser()).thenReturn(stubUser);

        assertTrue(loginController.login(stubUser));
        verify(restServiceConfig).setApiBaseUrlByUser(stubUser);
    }

    @Test
    public void testLoginNewUserWithoutInternetMustFail() throws Exception {
        when(connection.isAvailable()).thenReturn(false);
        when(repository.getLastLoginUser()).thenReturn(null);

        assertFalse(loginController.login(odpc13User1()));
    }

}
