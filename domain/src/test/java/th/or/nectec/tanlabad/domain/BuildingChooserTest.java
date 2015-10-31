package th.or.nectec.tanlabad.domain;


import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import th.or.nectec.tanrabad.Building;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BuildingChooserTest {
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    private UUID placeUuid = UUID.randomUUID();

    @Test
    public void foundBuilding() {
        final BuildingRepository buildingRepository = context.mock(BuildingRepository.class);
        final BuildingPresenter presenter = context.mock(BuildingPresenter.class);

        final List<Building> buildings = new ArrayList<>();
        buildings.add(Building.withName("214/43"));

        context.checking(new Expectations() {
            {
                allowing(buildingRepository).findBuildingInPlace(placeUuid);
                will(returnValue(buildings));

                oneOf(presenter).showBuildingList(buildings);
            }
        });

        BuildingChooser buildingChooser = new BuildingChooser(buildingRepository, presenter);
        buildingChooser.showBuildingOf(placeUuid);
    }

    @Test
    public void notFoundBuilding() {
        final BuildingRepository buildingRepository = context.mock(BuildingRepository.class);
        final BuildingPresenter presenter = context.mock(BuildingPresenter.class);


        context.checking(new Expectations() {
            {
                allowing(buildingRepository).findBuildingInPlace(placeUuid);
                will(returnValue(null));

                oneOf(presenter).showNotFoundBuilding();
            }
        });

        BuildingChooser buildingChooser = new BuildingChooser(buildingRepository, presenter);
        buildingChooser.showBuildingOf(placeUuid);
    }


    private class BuildingChooser {
        private final BuildingRepository buildingRepository;
        private final BuildingPresenter presenter;

        public BuildingChooser(BuildingRepository buildingRepository, BuildingPresenter presenter) {

            this.buildingRepository = buildingRepository;
            this.presenter = presenter;
        }

        public void showBuildingOf(UUID placeUuid) {
            ArrayList<Building> buildingInPlace = buildingRepository.findBuildingInPlace(placeUuid);
            if (buildingInPlace != null)
                presenter.showBuildingList(buildingInPlace);
            else
                presenter.showNotFoundBuilding();
        }
    }
}
