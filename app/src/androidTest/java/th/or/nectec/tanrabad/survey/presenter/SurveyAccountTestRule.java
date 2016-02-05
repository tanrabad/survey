package th.or.nectec.tanrabad.survey.presenter;


import org.junit.rules.ExternalResource;
import th.or.nectec.tanrabad.entity.User;

public class SurveyAccountTestRule extends ExternalResource {
    String username;

    public SurveyAccountTestRule() {
        username = "dpc-user";
    }

    public SurveyAccountTestRule(String username) {
        this.username = username;
    }

    @Override
    protected void before() throws Throwable {
        AccountUtils.setUser(User.fromUsername(username));
    }

}
