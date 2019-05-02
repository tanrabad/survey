/*
 * Copyright (c) 2016 NECTEC
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

package org.tanrabad.survey.domain.building;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.tanrabad.survey.domain.place.PlaceRepository;
import org.tanrabad.survey.domain.survey.SurveyBuildingChooser;
import org.tanrabad.survey.domain.survey.SurveyRepository;
import org.tanrabad.survey.domain.user.UserRepository;
import org.tanrabad.survey.entity.Building;
import org.tanrabad.survey.entity.Place;
import org.tanrabad.survey.entity.User;
import org.tanrabad.survey.entity.field.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BuildingWithSurveyStatusChooserTest {

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    private SurveyRepository surveyRepository;
    private PlaceRepository placeRepository;
    private UserRepository userRepository;
    private BuildingWithSurveyStatusListPresenter surveyBuildingPresenter;
    private Place place;
    private User user;
    private String placeUuid;
    private String username;
    private Building building1;
    private Building building2;
    private Building building3;
    private Building building4;
    private Building building5;

    @Before
    public void setUp() {
        surveyRepository = context.mock(SurveyRepository.class);
        placeRepository = context.mock(PlaceRepository.class);
        userRepository = context.mock(UserRepository.class);
        surveyBuildingPresenter = context.mock(BuildingWithSurveyStatusListPresenter.class);

        placeUuid = UUID.nameUUIDFromBytes("1abc".getBytes()).toString();
        username = "ice";

        place = new Place(UUID.fromString(placeUuid), "ทดสอบ");
        place.setLocation(new Location(10, 105.5));
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
        final List<BuildingWithSurveyStatus> buildingsWithSurveyStatuses = new ArrayList<>();
        buildingsWithSurveyStatuses.add(new BuildingWithSurveyStatus(building1, false));
        buildingsWithSurveyStatuses.add(new BuildingWithSurveyStatus(building2, true));
        buildingsWithSurveyStatuses.add(new BuildingWithSurveyStatus(building3, false));

        context.checking(new Expectations() {
            {
                allowing(placeRepository).findByUuid(with(UUID.fromString(placeUuid)));
                will(returnValue(place));

                allowing(userRepository).findByUsername(with(username));
                will(returnValue(user));

                allowing(surveyRepository).findSurveyBuilding(with(place), with(user));
                will(returnValue(buildingsWithSurveyStatuses));

                allowing(surveyBuildingPresenter).displayAllSurveyBuildingList(with(buildingsWithSurveyStatuses));

            }
        });
        SurveyBuildingChooser surveyBuildingHistoryController = new SurveyBuildingChooser(
                userRepository, placeRepository, surveyRepository, surveyBuildingPresenter);
        surveyBuildingHistoryController.displaySurveyBuildingOf(placeUuid, user);
    }

    @Test
    public void testSearchSurveyBuildingListByName() throws Exception {
        final String searchBuildingName = "13";

        final List<BuildingWithSurveyStatus> buildingsWithSurveyStatuses = new ArrayList<>();
        buildingsWithSurveyStatuses.add(new BuildingWithSurveyStatus(building4, false));
        buildingsWithSurveyStatuses.add(new BuildingWithSurveyStatus(building5, false));

        context.checking(new Expectations() {
            {
                allowing(placeRepository).findByUuid(with(UUID.fromString(placeUuid)));
                will(returnValue(place));

                allowing(userRepository).findByUsername(with(username));
                will(returnValue(user));

                allowing(surveyRepository)
                        .findSurveyBuildingByBuildingName(with(place), with(user), with(searchBuildingName));
                will(returnValue(buildingsWithSurveyStatuses));

                allowing(surveyBuildingPresenter).displayAllSurveyBuildingList(with(buildingsWithSurveyStatuses));
            }
        });
        SurveyBuildingChooser surveyBuildingHistoryController = new SurveyBuildingChooser(
                userRepository, placeRepository, surveyRepository, surveyBuildingPresenter);
        surveyBuildingHistoryController.searchSurveyBuildingOfPlaceByName(searchBuildingName, placeUuid, user);
    }


    @Test
    public void testShowBuildingNotSurveyList() throws Exception {
        final List<BuildingWithSurveyStatus> buildingsWithSurveyStatuses = new ArrayList<>();
        buildingsWithSurveyStatuses.add(new BuildingWithSurveyStatus(building1, false));
        buildingsWithSurveyStatuses.add(new BuildingWithSurveyStatus(building2, false));
        buildingsWithSurveyStatuses.add(new BuildingWithSurveyStatus(building3, false));

        context.checking(new Expectations() {
            {
                allowing(placeRepository).findByUuid(with(UUID.fromString(placeUuid)));
                will(returnValue(place));

                allowing(userRepository).findByUsername(with(username));
                will(returnValue(user));

                allowing(surveyRepository).findSurveyBuilding(with(place), with(user));
                will(returnValue(buildingsWithSurveyStatuses));

                allowing(surveyBuildingPresenter).displayAllSurveyBuildingList(with(buildingsWithSurveyStatuses));

            }
        });
        SurveyBuildingChooser surveyBuildingHistoryController = new SurveyBuildingChooser(
                userRepository, placeRepository, surveyRepository, surveyBuildingPresenter);
        surveyBuildingHistoryController.displaySurveyBuildingOf(placeUuid, user);
    }

    @Test
    public void testAlertNoBuildingFound() throws Exception {
        context.checking(new Expectations() {
            {
                allowing(userRepository).findByUsername(with(username));
                will(returnValue(user));

                allowing(placeRepository).findByUuid(with(UUID.fromString(placeUuid)));
                will(returnValue(place));

                allowing(surveyRepository).findSurveyBuilding(with(place), with(user));
                will(returnValue(new ArrayList<>()));

                allowing(surveyBuildingPresenter).alertBuildingsNotFound();
            }
        });
        SurveyBuildingChooser surveyBuildingHistoryController = new SurveyBuildingChooser(
                userRepository, placeRepository, surveyRepository, surveyBuildingPresenter);
        surveyBuildingHistoryController.displaySurveyBuildingOf(placeUuid, user);

    }

    @Test
    public void testNotFoundUser() throws Exception {

        context.checking(new Expectations() {
            {
                allowing(userRepository).findByUsername(with(username));
                will(returnValue(null));
                oneOf(surveyBuildingPresenter).alertUserNotFound();
            }
        });
        SurveyBuildingChooser surveyBuildingChooser = new SurveyBuildingChooser(
                userRepository, placeRepository, surveyRepository, surveyBuildingPresenter);
        surveyBuildingChooser.displaySurveyBuildingOf(placeUuid, user);
    }

    @Test
    public void testNotFoundPlace() throws Exception {

        context.checking(new Expectations() {
            {
                allowing(userRepository).findByUsername(with(username));
                will(returnValue(user));

                allowing(placeRepository).findByUuid(with(UUID.fromString(placeUuid)));
                will(returnValue(null));
                oneOf(surveyBuildingPresenter).alertPlaceNotFound();
            }
        });
        SurveyBuildingChooser surveyBuildingChooser = new SurveyBuildingChooser(
                userRepository, placeRepository, surveyRepository, surveyBuildingPresenter);
        surveyBuildingChooser.displaySurveyBuildingOf(placeUuid, user);
    }
}
