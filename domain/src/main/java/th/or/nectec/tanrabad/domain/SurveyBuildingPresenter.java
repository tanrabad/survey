package th.or.nectec.tanrabad.domain;

import java.util.List;

import th.or.nectec.tanrabad.entity.Building;

public interface SurveyBuildingPresenter {
    void alertUserNotFound();

    void alertPlaceNotFound();

    void alertSurveyBuildingsNotFound();

    void displaySurveyBuildingList(List<Building> buildings);
}
