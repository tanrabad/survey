package th.or.nectec.tanrabad.survey.repository;


import th.or.nectec.tanrabad.domain.UserRepository;
import th.or.nectec.tanrabad.entity.User;

public class StubUserRepository implements UserRepository {

    private final User sara;

    public StubUserRepository() {
        sara = new User("sara");
        sara.setFirstname("ซาร่า");
        sara.setLastname("คิดส์");
        sara.setEmail("sara.k@gmail.com");
        sara.setOrganizationId(1);
    }

    @Override
    public User findUserByName(String userName) {
        if (sara.getUsername().equals(userName)) {
            return sara ;
        } else {
            return null;
        }
    }

}
