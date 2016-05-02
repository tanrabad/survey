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
        when(repository.getLastLoginUser()).thenReturn(stubLastLoginSameOrgId());

        User user = stubUser();
        assertTrue(loginController.isCanLogin(user));
        verify(connection).isAvailable();
        assertFalse(loginController.shouldUploadOldUserData(user));
        verify(loginController).setUser(user);
        verify(loginController, never()).syncAndClearData();
        verify(loginController).setApiEndPointByUser(user);
    }

    private User stubUser() {
        User user = User.fromUsername("oh-my-god");
        user.setOrganizationId(10);
        return user;
    }

    private User stubLastLoginSameOrgId() {
        User user = User.fromUsername("hahaha");
        user.setOrganizationId(10);
        return user;
    }

    @Test
    public void testLoginDifferentOrgIdAndConnectedInternetMustSuccess() throws Exception {
        User stubLastLoginUserDifferentOrgId = stubLastLoginUserDifferentOrgId();
        when(connection.isAvailable()).thenReturn(true);
        when(repository.getLastLoginUser()).thenReturn(stubLastLoginUserDifferentOrgId);

        User stubUser = stubUser();
        assertTrue(loginController.isCanLogin(stubUser));
        verify(connection).isAvailable();
        assertTrue(loginController.shouldUploadOldUserData(stubUser));
        verify(loginController).setApiEndPointByUser(stubLastLoginUserDifferentOrgId);
        verify(loginController).syncAndClearData();
        verify(loginController).setUser(stubUser);
        verify(loginController).setApiEndPointByUser(stubUser);
    }

    private User stubLastLoginUserDifferentOrgId() {
        User user = User.fromUsername("hello");
        user.setOrganizationId(11);
        return user;
    }

    @Test
    public void testLoginTrialInsteadAuthenAndConnectedInternetMustSuccess() throws Exception {
        User stubLastLoginUserDifferentOrgId = stubLastLoginUserDifferentOrgId();
        when(connection.isAvailable()).thenReturn(true);
        when(repository.getLastLoginUser()).thenReturn(stubLastLoginUserDifferentOrgId);

        User trialUser = stubTrialUser();
        assertTrue(loginController.isCanLogin(trialUser));
        verify(connection).isAvailable();
        assertTrue(loginController.shouldUploadOldUserData(trialUser));
        verify(loginController).setApiEndPointByUser(stubLastLoginUserDifferentOrgId);
        verify(loginController).syncAndClearData();
        verify(loginController).setUser(trialUser);
        verify(loginController).setApiEndPointByUser(trialUser);
    }

    private User stubTrialUser() {
        User user = User.fromUsername("trial-debug");
        user.setOrganizationId(999);
        return user;
    }

    @Test
    public void testLoginSameUserWithoutInternetMustSuccess() throws Exception {
        User stubUser = stubUser();
        when(connection.isAvailable()).thenReturn(false);
        when(repository.getLastLoginUser()).thenReturn(stubUser);

        assertTrue(loginController.isCanLogin(stubUser));
        verify(connection).isAvailable();
        assertFalse(loginController.isNewUser(stubUser));
        verify(loginController).setApiEndPointByUser(stubUser);
        assertEquals(BuildConfig.API_URL, AbsRestService.getBaseApi());
    }

    @Test
    public void testLoginNewUserWithoutInternetMustFail() throws Exception {
        User stubUser = stubUser();
        when(connection.isAvailable()).thenReturn(false);
        when(repository.getLastLoginUser()).thenReturn(null);

        assertFalse(loginController.isCanLogin(stubUser));
        verify(connection).isAvailable();
        assertTrue(loginController.isNewUser(stubUser));
    }

    @Test
    public void testTrialUserMustSetTestApiEndpoint() throws Exception {
        loginController.setApiEndPointByUser(stubTrialUser());
        assertEquals(LoginController.TEST_URL, AbsRestService.getBaseApi());
    }

    @Test
    public void testAuthenUserMustSetApiEndpointByBuildConfig() throws Exception {
        loginController.setApiEndPointByUser(stubUser());
        assertEquals(BuildConfig.API_URL, AbsRestService.getBaseApi());
    }
}
