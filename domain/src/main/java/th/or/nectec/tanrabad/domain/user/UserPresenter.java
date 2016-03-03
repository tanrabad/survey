package th.or.nectec.tanrabad.domain.user;

import th.or.nectec.tanrabad.entity.User;

public interface UserPresenter {

    void displayNotFoundUser();

    void displayUserName(User user);
}
