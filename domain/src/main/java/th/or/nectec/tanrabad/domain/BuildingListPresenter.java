package th.or.nectec.tanrabad.domain;

import java.util.List;

public interface BuildingListPresenter {
    void alertBuildingsNotFound();

    void displayAllSurveyBuildingList(List<BuildingWithSurveyStatus> buildingsWithSurveyStatuses);

    void alertUserNotFound();

    void alertPlaceNotFound();
}
