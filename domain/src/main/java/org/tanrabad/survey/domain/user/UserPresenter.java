package org.tanrabad.survey.domain.user;

import org.tanrabad.survey.entity.User;

public interface UserPresenter {

    void displayNotFoundUser();

    void displayUserName(User user);
}
