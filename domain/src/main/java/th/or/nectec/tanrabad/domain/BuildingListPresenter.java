package th.or.nectec.tanrabad.domain;

import java.util.List;

import th.or.nectec.tanrabad.entity.Building;

public interface BuildingListPresenter {
    void alertBuildingsNotFound();

    void displayBuildingsList(List<Building> buildingsWithSurveyStatuses);

    void alertUserNotFound();

    void alertPlaceNotFound();
}
