package th.or.nectec.tanrabad.domain;

import th.or.nectec.tanrabad.entity.Building;

public class BuildingSaver {
    private final BuildingSavePresenter buildingSavePresenter;
    private final BuildingRepository buildingRepository;
    private final BuildingValidator buildingValidator;

    public BuildingSaver(BuildingRepository buildingRepository,
                         BuildingValidator buildingValidator,
                         BuildingSavePresenter buildingSavePresenter) {
        this.buildingSavePresenter = buildingSavePresenter;
        this.buildingValidator = buildingValidator;
        this.buildingRepository = buildingRepository;
        buildingValidator.setBuildingRepository(buildingRepository);
    }

    public void save(Building building) {
        if (buildingValidator.validate(building)) {
            if (buildingRepository.save(building))
                buildingSavePresenter.displaySaveSuccess();
        } else {
            buildingSavePresenter.displaySaveFail();
        }
    }
}
