/*
 * Copyright (c) 2015 NECTEC
 *   National Electronics and Computer Technology Center, Thailand
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package th.or.nectec.tanrabad.domain.building;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import th.or.nectec.tanrabad.domain.UserRepository;
import th.or.nectec.tanrabad.domain.place.PlaceRepository;
import th.or.nectec.tanrabad.domain.survey.SurveyBuildingChooser;
import th.or.nectec.tanrabad.domain.survey.SurveyRepository;
import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.entity.Survey;
import th.or.nectec.tanrabad.entity.User;

public class BuildingWithSurveyStatusChooserTest {

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    private SurveyRepository surveyRepository;
    private PlaceRepository placeRepository;
    private UserRepository userRepository;
    private BuildingWithSurveyStatusListPresenter buildingWithSurveyStatusListPresenter;
    private Place place;
    private User user;
    private String placeUUID;
    private String username;
    private BuildingRepository buildingRepository;
    private Building building1, building2, building3, building4, building5;

    @Before
    public void setUp() {
        surveyRepository = context.mock(SurveyRepository.class);
        placeRepository = context.mock(PlaceRepository.class);
        buildingRepository = context.mock(BuildingRepository.class);
        userRepository = context.mock(UserRepository.class);
        buildingWithSurveyStatusListPresenter = context.mock(BuildingWithSurveyStatusListPresenter.class);

        placeUUID = UUID.nameUUIDFromBytes("1abc".getBytes()).toString();
        username = "ice";

        place = new Place(UUID.fromString(placeUUID), "ทดสอบ");
        user = User.fromUsername(username);

        building1 = Building.withName("123");
        building1.setPlace(place);

        building2 = Building.withName("124");
        building2.setPlace(place);

        building3 = Building.withName("125");
        building3.setPlace(place);

        building4 = Building.withName("131");
        building4.setPlace(place);

        building5 = Building.withName("134");
        building5.setPlace(place);
    }

    @Test
    public void testShowSurveyBuildingList() throws Exception {

        final List<Building> buildings = new ArrayList<>();
        buildings.add(building1);
        buildings.add(building2);
        buildings.add(building3);

        final List<Survey> surveyBuildings = new ArrayList<>();
        surveyBuildings.add(new Survey(user, building2));

        final List<BuildingWithSurveyStatus> buildingsWithSurveyStatuses = new ArrayList<>();
        buildingsWithSurveyStatuses.add(new BuildingWithSurveyStatus(building1, false));
        buildingsWithSurveyStatuses.add(new BuildingWithSurveyStatus(building2, true));
        buildingsWithSurveyStatuses.add(new BuildingWithSurveyStatus(building3, false));

        context.checking(new Expectations() {
            {
                allowing(placeRepository).findPlaceByPlaceUUID(with(UUID.fromString(placeUUID)));
                will(returnValue(place));

                allowing(userRepository).findUserByName(with(username));
                will(returnValue(user));

                allowing(buildingRepository).findBuildingInPlace(with(place.getId()));
                will(returnValue(buildings));

                allowing(surveyRepository).findByPlaceAndUserIn7Days(with(place), with(user));
                will(returnValue(surveyBuildings));

                allowing(buildingWithSurveyStatusListPresenter).displayAllSurveyBuildingList(with(buildingsWithSurveyStatuses));

            }
        });
        SurveyBuildingChooser surveyBuildingHistoryController = new SurveyBuildingChooser(userRepository, placeRepository, buildingRepository, surveyRepository, buildingWithSurveyStatusListPresenter);
        surveyBuildingHistoryController.displaySurveyBuildingOf(placeUUID, username);
    }

    @Test
    public void testSearchSurveyBuildingListByName() throws Exception {
        final String searchBuildingName = "13";

        final List<Building> queryBuildings = new ArrayList<>();
        queryBuildings.add(building4);
        queryBuildings.add(building5);

        final List<BuildingWithSurveyStatus> buildingsWithSurveyStatuses = new ArrayList<>();
        buildingsWithSurveyStatuses.add(new BuildingWithSurveyStatus(building4, false));
        buildingsWithSurveyStatuses.add(new BuildingWithSurveyStatus(building5, false));

        context.checking(new Expectations() {
            {
                allowing(placeRepository).findPlaceByPlaceUUID(with(UUID.fromString(placeUUID)));
                will(returnValue(place));

                allowing(userRepository).findUserByName(with(username));
                will(returnValue(user));

                allowing(buildingRepository).searchBuildingInPlaceByName(with(place.getId()), with(searchBuildingName));
                will(returnValue(queryBuildings));

                allowing(surveyRepository).findByPlaceAndUserIn7Days(with(place), with(user));
                will(returnValue(null));

                allowing(buildingWithSurveyStatusListPresenter).displayAllSurveyBuildingList(with(buildingsWithSurveyStatuses));
            }
        });
        SurveyBuildingChooser surveyBuildingHistoryController = new SurveyBuildingChooser(userRepository, placeRepository, buildingRepository, surveyRepository, buildingWithSurveyStatusListPresenter);
        surveyBuildingHistoryController.searchSurveyBuildingOfPlaceByName(searchBuildingName, placeUUID, username);
    }


    @Test
    public void testShowBuildingNotSurveyList() throws Exception {

        final List<Building> buildings = new ArrayList<>();
        buildings.add(building1);
        buildings.add(building2);
        buildings.add(building3);


        final List<BuildingWithSurveyStatus> buildingsWithSurveyStatuses = new ArrayList<>();
        buildingsWithSurveyStatuses.add(new BuildingWithSurveyStatus(building1, false));
        buildingsWithSurveyStatuses.add(new BuildingWithSurveyStatus(building2, false));
        buildingsWithSurveyStatuses.add(new BuildingWithSurveyStatus(building3, false));

        context.checking(new Expectations() {
            {
                allowing(placeRepository).findPlaceByPlaceUUID(with(UUID.fromString(placeUUID)));
                will(returnValue(place));

                allowing(userRepository).findUserByName(with(username));
                will(returnValue(user));

                allowing(buildingRepository).findBuildingInPlace(with(place.getId()));
                will(returnValue(buildings));

                allowing(surveyRepository).findByPlaceAndUserIn7Days(with(place), with(user));
                will(returnValue(null));

                allowing(buildingWithSurveyStatusListPresenter).displayAllSurveyBuildingList(with(buildingsWithSurveyStatuses));

            }
        });
        SurveyBuildingChooser surveyBuildingHistoryController = new SurveyBuildingChooser(userRepository, placeRepository, buildingRepository, surveyRepository, buildingWithSurveyStatusListPresenter);
        surveyBuildingHistoryController.displaySurveyBuildingOf(placeUUID, username);
    }

    @Test
    public void testAlertNoBuildingFound() throws Exception {
        context.checking(new Expectations() {
            {
                allowing(userRepository).findUserByName(with(username));
                will(returnValue(user));

                allowing(placeRepository).findPlaceByPlaceUUID(with(UUID.fromString(placeUUID)));
                will(returnValue(place));

                allowing(buildingRepository).findBuildingInPlace(with(place.getId()));
                will(returnValue(null));

                never(surveyRepository);

                allowing(buildingWithSurveyStatusListPresenter).alertBuildingsNotFound();

            }
        });
        SurveyBuildingChooser surveyBuildingHistoryController = new SurveyBuildingChooser(userRepository, placeRepository, buildingRepository, surveyRepository, buildingWithSurveyStatusListPresenter);
        surveyBuildingHistoryController.displaySurveyBuildingOf(placeUUID, username);

    }

    @Test
    public void testNotFoundUser() throws Exception {

        context.checking(new Expectations() {
            {
                allowing(userRepository).findUserByName(with(username));
                will(returnValue(null));
                oneOf(buildingWithSurveyStatusListPresenter).alertUserNotFound();
            }
        });
        SurveyBuildingChooser surveyBuildingChooser = new SurveyBuildingChooser(userRepository, placeRepository, buildingRepository, surveyRepository, buildingWithSurveyStatusListPresenter);
        surveyBuildingChooser.displaySurveyBuildingOf(placeUUID, username);
    }

    @Test
    public void testNotFoundPlace() throws Exception {

        context.checking(new Expectations() {
            {
                allowing(userRepository).findUserByName(with(username));
                will(returnValue(user));

                allowing(placeRepository).findPlaceByPlaceUUID(with(UUID.fromString(placeUUID)));
                will(returnValue(null));
                oneOf(buildingWithSurveyStatusListPresenter).alertPlaceNotFound();
            }
        });
        SurveyBuildingChooser surveyBuildingChooser = new SurveyBuildingChooser(userRepository, placeRepository, buildingRepository, surveyRepository, buildingWithSurveyStatusListPresenter);
        surveyBuildingChooser.displaySurveyBuildingOf(placeUUID, username);
    }
}



