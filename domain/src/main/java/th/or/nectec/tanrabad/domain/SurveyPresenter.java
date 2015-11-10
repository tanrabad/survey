package th.or.nectec.tanrabad.domain;

import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.entity.Survey;
import th.or.nectec.tanrabad.entity.User;

public interface SurveyPresenter {
    void loadSurveySuccess(Survey survey);

    void startNewSurvey(Building building, User user);
}
