package th.or.nectec.tanrabad.domain;

import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.entity.Survey;
import th.or.nectec.tanrabad.entity.User;

public interface SurveyPresenter {
    void onEditSurvey(Survey survey);

    void onNewSurvey(Building building, User user);

    void alertUserNotFound();

    void alertBuildingNotFound();

    void alertPlaceNotFound();
}
