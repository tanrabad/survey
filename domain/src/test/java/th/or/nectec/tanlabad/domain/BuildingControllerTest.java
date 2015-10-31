package th.or.nectec.tanlabad.domain;


import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import th.or.nectec.tanrabad.Building;

import java.util.ArrayList;
import java.util.List;

public class BuildingControllerTest {
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    @Test
    public void foundBuilding() {
        final BuildingRepository buildingRepository = context.mock(BuildingRepository.class);
        final BuildingPresenter presenter = context.mock(BuildingPresenter.class);

        final List<Building> buildings = new ArrayList<>();
        buildings.add(Building.withIdAndName(1, "214/43"));

        context.checking(new Expectations() {
            {
                allowing(buildingRepository).findBuildingInPlace(123456);
                will(returnValue(buildings));

                oneOf(presenter).showBuildingList(buildings);
            }
        });

        BuildingController buildingController = new BuildingController(buildingRepository, presenter);
        buildingController.viewBuildingOf(123456);
    }

    @Test
    public void notFoundBuilding() {
        final BuildingRepository buildingRepository = context.mock(BuildingRepository.class);
        final BuildingPresenter presenter = context.mock(BuildingPresenter.class);


        context.checking(new Expectations() {
            {
                allowing(buildingRepository).findBuildingInPlace(123456);
                will(returnValue(null));

                oneOf(presenter).showNotFoundBuilding();
            }
        });

        BuildingController buildingController = new BuildingController(buildingRepository, presenter);
        buildingController.viewBuildingOf(123456);
    }


    private class BuildingController {
        private final BuildingRepository buildingRepository;
        private final BuildingPresenter presenter;

        public BuildingController(BuildingRepository buildingRepository, BuildingPresenter presenter) {

            this.buildingRepository = buildingRepository;
            this.presenter = presenter;
        }

        public void viewBuildingOf(int placeId) {
            ArrayList<Building> buildingInPlace = buildingRepository.findBuildingInPlace(placeId);
            if (buildingInPlace != null)
                presenter.showBuildingList(buildingInPlace);
            else
                presenter.showNotFoundBuilding();

        }
    }
}
