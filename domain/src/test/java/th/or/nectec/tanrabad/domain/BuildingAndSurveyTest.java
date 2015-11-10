package th.or.nectec.tanrabad.domain;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.UUID;

import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.entity.User;

public class BuildingAndSurveyTest {

    public final String buildingName = "123";
    public final String userName = "ice";
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();
    User user = User.fromUsername(userName);
    Building building = Building.withName(buildingName);
    private BuildingRepository buildingRepository;
    private BuildingPresenter buildingPresenter;
    private UserRepository userRepository;
    private UserPresenter userPresenter;
    private UUID buildingUUID;

    @Before
    public void setUp() throws Exception {
        buildingUUID = UUID.nameUUIDFromBytes("3xyz".getBytes());

        buildingRepository = context.mock(BuildingRepository.class);
        buildingPresenter = context.mock(BuildingPresenter.class);

        userRepository = context.mock(UserRepository.class);
        userPresenter = context.mock(UserPresenter.class);
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
    public void testFoundUser() throws Exception {

        context.checking(new Expectations() {
            {
                allowing(userRepository).findUserByName(userName);
                will(returnValue(user));
                oneOf(userPresenter).showUserName(user);
            }
        });
        UserController userController = new UserController(userRepository, userPresenter);
        userController.showUserOf(userName);
    }

    @Test
    public void testNotFoundUser() throws Exception {

        context.checking(new Expectations() {
            {
                allowing(userRepository).findUserByName(userName);
                will(returnValue(null));
                oneOf(userPresenter).showNotFoundUser();
            }
        });
        UserController userController = new UserController(userRepository, userPresenter);
        userController.showUserOf(userName);
    }

    @Test
    public void testNotFoundBuilding() throws Exception {

        context.checking(new Expectations() {
            {
                allowing(buildingRepository).findBuildingByName(buildingName);
                will(returnValue(null));
                oneOf(buildingPresenter).showNotFoundBuilding();
            }
        });
        BuildingController buildingController = new BuildingController(buildingRepository, buildingPresenter);
        buildingController.showBuildingOf(buildingName);
    }

}
