package th.or.nectec.tanlabad.domain;

import th.or.nectec.tanrabad.entity.Building;

import java.util.List;

public interface BuildingPresenter {
    void showBuildingList(List<Building> buildings);

    void showNotFoundBuilding();

    void showPleaseSpecityPlace();
}
