package org.tanrabad.survey.presenter;

import org.junit.Before;
import org.junit.Test;
import org.tanrabad.survey.BuildConfig;
import org.tanrabad.survey.entity.User;
import org.tanrabad.survey.service.AbsRestService;
import org.tanrabad.survey.utils.android.Connection;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;

public class LoginControllerTest {

    private final Connection connection = mock(Connection.class);
    private final AccountUtils.LastLoginUserRepo repository = mock(AccountUtils.LastLoginUserRepo.class);
    LoginController loginController = spy(new LoginController(connection, repository) {
        @Override
        protected void setUser(User user) {
        }

        @Override
        protected void syncAndClearData() {
        }
    });

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
        verify(loginController, never()).syncAndClearData();
        verify(loginController).setApiEndPointByUser(user);
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
        verify(loginController).setApiEndPointByUser(odpc11Hello);
        verify(loginController).syncAndClearData();
        verify(loginController).setUser(odpc13User1);
        verify(loginController).setApiEndPointByUser(odpc13User1);
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
        verify(loginController).setApiEndPointByUser(stubUser);
    }

    @Test
    public void testLoginNewUserWithoutInternetMustFail() throws Exception {
        when(connection.isAvailable()).thenReturn(false);
        when(repository.getLastLoginUser()).thenReturn(null);

        assertFalse(loginController.login(odpc13User1()));
    }

    @Test
    public void testTrialUserMustSetTestApiEndpoint() throws Exception {
        loginController.setApiEndPointByUser(trialUser());

        assertEquals(LoginController.TEST_URL, AbsRestService.getBaseApi());
    }

    private User trialUser() {
        User user = User.fromUsername("trial-debug");
        user.setOrganizationId(999);
        return user;
    }

    @Test
    public void testAuthenUserMustSetApiEndpointByBuildConfig() throws Exception {
        loginController.setApiEndPointByUser(odpc13User1());

        assertEquals(BuildConfig.API_URL, AbsRestService.getBaseApi());
    }
}
