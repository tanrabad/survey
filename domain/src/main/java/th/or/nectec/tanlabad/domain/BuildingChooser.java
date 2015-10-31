package th.or.nectec.tanlabad.domain;

import th.or.nectec.tanrabad.entity.Building;

import java.util.ArrayList;
import java.util.UUID;

public class BuildingChooser {
    private final BuildingRepository buildingRepository;
    private final BuildingPresenter presenter;

    public BuildingChooser(BuildingRepository buildingRepository, BuildingPresenter presenter) {

        this.buildingRepository = buildingRepository;
        this.presenter = presenter;
    }

    public void showBuildingOf(UUID placeUuid) {
        if (placeUuid == null) {
            presenter.showPleaseSpecityPlace();
            return;
        }

        ArrayList<Building> buildingInPlace = buildingRepository.findBuildingInPlace(placeUuid);
        if (buildingInPlace != null)
            presenter.showBuildingList(buildingInPlace);
        else
            presenter.showNotFoundBuilding();
    }
}
