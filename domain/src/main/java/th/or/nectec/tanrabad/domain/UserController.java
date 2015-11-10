package th.or.nectec.tanrabad.domain;

import th.or.nectec.tanrabad.entity.User;

/**
 * Created by User on 10/11/2558.
 */
class UserController {
    private UserRepository userRepository;
    private UserPresenter userPresenter;

    public UserController(UserRepository userRepository, UserPresenter userPresenter) {
        this.userRepository = userRepository;
        this.userPresenter = userPresenter;
    }

    public void showUserOf(String userName) {
        User user = userRepository.findUserByName(userName);
        if (user == null) {
            userPresenter.showNotFoundUser();
        } else {
            userPresenter.showUserName(user);
        }
    }
}
