package th.or.nectec.tanrabad.domain;

import th.or.nectec.tanrabad.entity.Building;

public interface BuildingValidator {
    boolean validate(Building building);

    void setBuildingRepository(BuildingRepository buildingRepository);
}
