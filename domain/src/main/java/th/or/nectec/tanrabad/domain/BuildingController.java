package th.or.nectec.tanrabad.domain;

import th.or.nectec.tanrabad.entity.Building;

public class BuildingController {
    private BuildingRepository buildingRepository;
    private BuildingPresenter buildingPresenter;

    public BuildingController(BuildingRepository buildingRepository, BuildingPresenter buildingPresenter) {
        this.buildingRepository = buildingRepository;
        this.buildingPresenter = buildingPresenter;
    }

    public void showBuildingOf(String buildingName) {
        Building building = buildingRepository.findBuildingByName(buildingName);
        if (building == null) {
            buildingPresenter.showNotFoundBuilding();
        } else {
            buildingPresenter.showBuildingName(building);
        }
    }

    /*public void showBuildingOf(Building buildingName) {

    }*/
}
