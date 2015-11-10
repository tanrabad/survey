package th.or.nectec.tanrabad.domain;

import th.or.nectec.tanrabad.entity.User;

/**
 * Created by User on 10/11/2558.
 */
public interface UserRepository {
    User findUserByName(String userName);
}
