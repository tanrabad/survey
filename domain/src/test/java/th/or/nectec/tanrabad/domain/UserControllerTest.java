package th.or.nectec.tanrabad.domain;


import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import th.or.nectec.tanrabad.entity.User;

public class UserControllerTest {
    public final String userName = "ice";
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();
    User user = User.fromUsername(userName);

    private UserRepository userRepository;
    private UserPresenter userPresenter;

    @Before
    public void setUp() throws Exception {
        userRepository = context.mock(UserRepository.class);
        userPresenter = context.mock(UserPresenter.class);
    }

    @Test
    public void testFoundUser() throws Exception {

        context.checking(new Expectations() {
            {
                allowing(userRepository).findUserByName(userName);
                will(returnValue(user));
                oneOf(userPresenter).displayUserName(user);
            }
        });
        UserController userController = new UserController(userRepository, userPresenter);
        userController.showUserOf(userName);
    }

    @Test
    public void testNotFoundUser() throws Exception {

        context.checking(new Expectations() {
            {
                allowing(userRepository).findUserByName(userName);
                will(returnValue(null));
                oneOf(userPresenter).displayNotFoundUser();
            }
        });
        UserController userController = new UserController(userRepository, userPresenter);
        userController.showUserOf(userName);
    }
}

