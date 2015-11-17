package th.or.nectec.tanrabad.domain;

import th.or.nectec.tanrabad.entity.Building;

class BuildingSaver {
    private final BuildingSavePresenter buildingSavePresenter;
    private final BuildingRepository buildingRepository;
    private final BuildingValidator saveValidator;

    public BuildingSaver(BuildingSavePresenter buildingSavePresenter,
                         BuildingValidator buildingValidator,
                         BuildingRepository buildingRepository) {
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
