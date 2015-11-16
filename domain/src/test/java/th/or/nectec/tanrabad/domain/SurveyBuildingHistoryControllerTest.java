package th.or.nectec.tanrabad.domain;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.entity.User;

public class SurveyBuildingHistoryControllerTest {

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    private SurveyRepository surveyRepository;
    private PlaceRepository placeRepository;
    private UserRepository userRepository;
    private SurveyBuildingPresenter surveyBuildingPresenter;
    private Place place;
    private User user;
    private String placeUUID;
    private String username;

    @Before
    public void setUp(){
        surveyRepository = context.mock(SurveyRepository.class);
        placeRepository = context.mock(PlaceRepository.class);
        userRepository = context.mock(UserRepository.class);
        surveyBuildingPresenter = context.mock(SurveyBuildingPresenter.class);

        placeUUID = UUID.nameUUIDFromBytes("1abc".getBytes()).toString();
        username = "ice";

        place = new Place(UUID.fromString(placeUUID), "1/1");
        user = User.fromUsername(username);

        Building building1 = Building.withName("123");
        building1.setPlace(place);

        final List<Building> surveyBuildings = new ArrayList<Building>();
        surveyBuildings.add(building1);
    }

    @Test
    public void testShowSurveyBuildingList() throws Exception {

        final List<Building> surveyBuildings = new ArrayList<Building>();

        context.checking(new Expectations() {
            {
                allowing(placeRepository).findPlaceByPlaceUUID(with(UUID.fromString(placeUUID)));
                will(returnValue(place));

                allowing(userRepository).findUserByName(with(username));
                will(returnValue(user));

                allowing(SurveyBuildingHistoryControllerTest.this.surveyRepository).findByPlaceAndUserIn7Days(with(place), with(user));

                will(returnValue(surveyBuildings));
                oneOf(surveyBuildingPresenter).displaySurveyBuildingList(surveyBuildings);
            }
        });
        SurveyBuildingHistoryController surveyBuildingHistoryController = new SurveyBuildingHistoryController(userRepository, placeRepository, this.surveyRepository, surveyBuildingPresenter);
        surveyBuildingHistoryController.showSurveyBuildingOf(placeUUID.toString(), username);
    }

    @Test
    public void testNotFoundSurveyPlace() throws Exception {

        context.checking(new Expectations() {
            {
                allowing(placeRepository).findPlaceByPlaceUUID(with(UUID.fromString(placeUUID)));
                will(returnValue(null));
                oneOf(surveyBuildingPresenter).alertPlaceNotFound();

                allowing(userRepository).findUserByName(with(username));
                will(returnValue(user));
            }
        });
        SurveyBuildingHistoryController surveyBuildingHistoryController = new SurveyBuildingHistoryController(userRepository, placeRepository, this.surveyRepository, surveyBuildingPresenter);
        surveyBuildingHistoryController.showSurveyBuildingOf(placeUUID.toString(), username);
    }

    @Test
    public void testNotFoundUser() throws Exception {

        context.checking(new Expectations() {
            {
                allowing(userRepository).findUserByName(with(username));
                will(returnValue(null));
                oneOf(surveyBuildingPresenter).alertUserNotFound();
            }
        });
        SurveyBuildingHistoryController surveyBuildingHistoryController = new SurveyBuildingHistoryController(userRepository, placeRepository, this.surveyRepository, surveyBuildingPresenter);
        surveyBuildingHistoryController.showSurveyBuildingOf(placeUUID.toString(), username);
    }

}



