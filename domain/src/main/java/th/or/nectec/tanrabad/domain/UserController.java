package th.or.nectec.tanrabad.domain;

import th.or.nectec.tanrabad.entity.User;

public class UserController {
    private UserRepository userRepository;
    private UserPresenter userPresenter;

    public UserController(UserRepository userRepository, UserPresenter userPresenter) {
        this.userRepository = userRepository;
        this.userPresenter = userPresenter;
    }

    public void showUserOf(String userName) {
        User user = userRepository.findUserByName(userName);
        if (user == null) {
            userPresenter.displayNotFoundUser();
        } else {
            userPresenter.displayUserName(user);
        }
    }
}
