package th.or.nectec.tanrabad.domain;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.UUID;

import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.entity.Location;
import th.or.nectec.tanrabad.entity.Place;


public class BuildingSaverTest {
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    private UUID buildingUUID;
    private String buildingName;
    private Place place;
    private Location buildingLocation;

    private BuildingValidator buildingValidator;
    private BuildingSavePresenter buildingSavePresenter;
    private BuildingRepository buildingRepository;
    private Building building;

    @Before
    public void setUp(){
        buildingSavePresenter = context.mock(BuildingSavePresenter.class);
        buildingValidator = context.mock(BuildingValidator.class);
        buildingRepository = context.mock(BuildingRepository.class);

        buildingUUID = UUID.nameUUIDFromBytes("3xyz".getBytes());
        buildingName = "123";
        place = Place.withName("Village A");
        buildingLocation = new Location(51.500152, -0.126236);

        building = new Building(buildingUUID, buildingName);
        building.setLocation(buildingLocation);
    }

    @Test
    public void testHappyPath() throws Exception {

        context.checking(new Expectations() {
            {
                allowing(BuildingSaverTest.this.buildingValidator).validate(with(building));
                will(returnValue(true));
                oneOf(BuildingSaverTest.this.buildingRepository).save(with(building));
                will(returnValue(true));
                oneOf(BuildingSaverTest.this.buildingSavePresenter).displaySaveSuccess();
            }
        });
        BuildingSaver buildingSaver = new BuildingSaver(buildingRepository, buildingValidator, buildingSavePresenter);
        buildingSaver.save(building);
    }

    @Test
    public void testSadPath() throws Exception {

        context.checking(new Expectations() {
            {
                allowing(buildingValidator).validate(building);
                will(returnValue(false));
                never(buildingRepository);
                oneOf(buildingSavePresenter).displaySaveFail();
            }
        });
        BuildingSaver buildingSaver = new BuildingSaver(buildingRepository, buildingValidator, buildingSavePresenter);
        buildingSaver.save(building);
    }

}
