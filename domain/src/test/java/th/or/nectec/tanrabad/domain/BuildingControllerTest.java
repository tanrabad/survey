package th.or.nectec.tanrabad.domain;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.UUID;

import th.or.nectec.tanrabad.entity.Building;

public class BuildingControllerTest {

    public final String buildingName = "123";

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    Building building = Building.withName(buildingName);
    private BuildingRepository buildingRepository;
    private BuildingPresenter buildingPresenter;
    private UUID buildingUUID;

    @Before
    public void setUp() throws Exception {
        buildingUUID = UUID.nameUUIDFromBytes("3xyz".getBytes());

        buildingRepository = context.mock(BuildingRepository.class);
        buildingPresenter = context.mock(BuildingPresenter.class);

    }

    @Test
    public void testFoundBuilding() throws Exception {

        context.checking(new Expectations() {
            {
                allowing(buildingRepository).findBuildingByUUID(buildingUUID);
                will(returnValue(building));
                oneOf(buildingPresenter).displayBuilding(building);
            }
        });
        BuildingController buildingController = new BuildingController(buildingRepository, buildingPresenter);
        buildingController.showBuilding(buildingUUID);
    }


    @Test
    public void testNotFoundBuilding() throws Exception {

        context.checking(new Expectations() {
            {
                allowing(buildingRepository).findBuildingByUUID(buildingUUID);
                will(returnValue(null));
                oneOf(buildingPresenter).alertBuildingNotFound();
            }
        });
        BuildingController buildingController = new BuildingController(buildingRepository, buildingPresenter);
        buildingController.showBuilding(buildingUUID);
    }

}
