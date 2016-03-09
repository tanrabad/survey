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

package th.or.nectec.tanrabad.domain.survey;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import th.or.nectec.tanrabad.domain.place.PlaceRepository;
import th.or.nectec.tanrabad.domain.user.UserRepository;
import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.entity.Survey;
import th.or.nectec.tanrabad.entity.User;

public class SurveyBuildingHistoryControllerTest {

    private static final String USERNAME = "test-user";
    private static final String placeUUID = UUID.nameUUIDFromBytes("1abc".getBytes()).toString();
    private final Place place = new Place(UUID.fromString(placeUUID), "1/1");
    private final User user = User.fromUsername(USERNAME);
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();
    @Mock
    private SurveyRepository surveyRepository;
    @Mock
    private PlaceRepository placeRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private SurveyBuildingPresenter presenter;

    @Test
    public void testShowSurveyBuildingList() throws Exception {
        Building building = Building.withName("test-building-name");
        building.setPlace(place);
        final List<Survey> surveys = new ArrayList<>();
        surveys.add(new Survey(UUID.randomUUID(), user, building));
        context.checking(new Expectations() {
            {
                allowing(placeRepository).findByUuid(with(UUID.fromString(placeUUID)));
                will(returnValue(place));
                allowing(userRepository).findByUsername(with(USERNAME));
                will(returnValue(user));
                allowing(SurveyBuildingHistoryControllerTest.this.surveyRepository)
                        .findByPlaceAndUserIn7Days(with(place), with(user));
                will(returnValue(surveys));
                oneOf(presenter).displaySurveyBuildingList(surveys);
            }
        });
        SurveyBuildingHistoryController controller = new SurveyBuildingHistoryController(
                userRepository, placeRepository, this.surveyRepository, presenter);
        controller.showSurveyBuildingOf(placeUUID, USERNAME);
    }

    @Test
    public void testNotFoundSurveyPlace() throws Exception {
        context.checking(new Expectations() {
            {
                allowing(placeRepository).findByUuid(with(UUID.fromString(placeUUID)));
                will(returnValue(null));
                oneOf(presenter).alertPlaceNotFound();
                allowing(userRepository).findByUsername(with(USERNAME));
                will(returnValue(user));
            }
        });
        SurveyBuildingHistoryController controller = new SurveyBuildingHistoryController(
                userRepository, placeRepository, this.surveyRepository, presenter);
        controller.showSurveyBuildingOf(placeUUID, USERNAME);
    }

    @Test
    public void testNotFoundUser() throws Exception {
        context.checking(new Expectations() {
            {
                allowing(userRepository).findByUsername(with(USERNAME));
                will(returnValue(null));
                oneOf(presenter).alertUserNotFound();
            }
        });
        SurveyBuildingHistoryController controller = new SurveyBuildingHistoryController(
                userRepository, placeRepository, this.surveyRepository, presenter);
        controller.showSurveyBuildingOf(placeUUID, USERNAME);
    }

    @Test
    public void testNotFoundSurvey() throws Exception {
        context.checking(new Expectations() {
            {
                allowing(userRepository).findByUsername(with(USERNAME));
                will(returnValue(user));
                allowing(placeRepository).findByUuid(with(UUID.fromString(placeUUID)));
                will(returnValue(place));
                allowing(SurveyBuildingHistoryControllerTest.this.surveyRepository)
                        .findByPlaceAndUserIn7Days(with(place), with(user));
                will(returnValue(null));
                oneOf(presenter).displaySurveyBuildingsNotFound();
            }
        });
        SurveyBuildingHistoryController controller = new SurveyBuildingHistoryController(
                userRepository, placeRepository, this.surveyRepository, presenter);
        controller.showSurveyBuildingOf(placeUUID, USERNAME);
    }
}



