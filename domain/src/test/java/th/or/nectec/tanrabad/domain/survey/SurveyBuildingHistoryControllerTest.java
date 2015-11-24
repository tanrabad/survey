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

package th.or.nectec.tanrabad.domain.survey;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import th.or.nectec.tanrabad.domain.UserRepository;
import th.or.nectec.tanrabad.domain.place.PlaceRepository;
import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.entity.Survey;
import th.or.nectec.tanrabad.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    }

    @Test
    public void testShowSurveyBuildingList() throws Exception {

        Building building1 = Building.withName("123");
        building1.setPlace(place);

        final List<Survey> surveys = new ArrayList<>();
        Survey survey1 = new Survey(user, building1);
        surveys.add(survey1);

        context.checking(new Expectations() {
            {
                allowing(placeRepository).findPlaceByPlaceUUID(with(UUID.fromString(placeUUID)));
                will(returnValue(place));

                allowing(userRepository).findUserByName(with(username));
                will(returnValue(user));

                allowing(SurveyBuildingHistoryControllerTest.this.surveyRepository).findByPlaceAndUserIn7Days(with(place), with(user));
                will(returnValue(surveys));
                oneOf(surveyBuildingPresenter).displaySurveyBuildingList(surveys);
            }
        });
        SurveyBuildingHistoryController surveyBuildingHistoryController = new SurveyBuildingHistoryController(userRepository, placeRepository, this.surveyRepository, surveyBuildingPresenter);
        surveyBuildingHistoryController.showSurveyBuildingOf(placeUUID, username);
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
        surveyBuildingHistoryController.showSurveyBuildingOf(placeUUID, username);
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
        surveyBuildingHistoryController.showSurveyBuildingOf(placeUUID, username);
    }

}



