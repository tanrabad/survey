package th.or.nectec.tanrabad.domain;

import th.or.nectec.tanrabad.entity.Building;

public class BuildingSaver {
    private final BuildingSavePresenter buildingSavePresenter;
    private final BuildingRepository buildingRepository;
    private final BuildingValidator saveValidator;

    public BuildingSaver(BuildingRepository buildingRepository,
                         BuildingValidator buildingValidator,
                         BuildingSavePresenter buildingSavePresenter) {
        this.buildingSavePresenter = buildingSavePresenter;
        saveValidator = buildingValidator;
        this.buildingRepository = buildingRepository;
    }

    public void save(Building building) {
        if (saveValidator.validate(building)) {
            if (buildingRepository.save(building))
                buildingSavePresenter.displaySaveSuccess();
        } else {
            buildingSavePresenter.displaySaveFail();
        }
    }
}
