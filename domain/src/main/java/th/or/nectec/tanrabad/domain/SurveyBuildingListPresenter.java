package th.or.nectec.tanrabad.domain;

import java.util.List;

public interface SurveyBuildingListPresenter {
    void alertBuildingsNotFound();

    void displayAllSurveyBuildingList(List<SurveyBuilding> surveyBuildingsWithStatus);

    void alertUserNotFound();

    void alertPlaceNotFound();
}
