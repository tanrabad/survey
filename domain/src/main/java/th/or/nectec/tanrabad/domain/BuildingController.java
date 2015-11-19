package th.or.nectec.tanrabad.domain;

import java.util.UUID;

import th.or.nectec.tanrabad.entity.Building;

public class BuildingController {
    private BuildingRepository buildingRepository;
    private BuildingPresenter buildingPresenter;

    public BuildingController(BuildingRepository buildingRepository, BuildingPresenter buildingPresenter) {
        this.buildingRepository = buildingRepository;
        this.buildingPresenter = buildingPresenter;
    }

    public void showBuilding(UUID buildingUUID) {
        Building buildingByUUID = buildingRepository.findBuildingByUUID(buildingUUID);
        if (buildingByUUID == null) {
            buildingPresenter.alertBuildingNotFound();
        } else {
            buildingPresenter.displayBuilding(buildingByUUID);
        }
    }


}
