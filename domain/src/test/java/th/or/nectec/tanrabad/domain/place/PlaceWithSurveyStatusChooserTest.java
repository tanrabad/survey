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

package th.or.nectec.tanrabad.domain.place;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import th.or.nectec.tanrabad.domain.survey.SurveyPlaceChooser;
import th.or.nectec.tanrabad.domain.survey.SurveyRepository;
import th.or.nectec.tanrabad.domain.user.UserRepository;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.entity.User;

public class PlaceWithSurveyStatusChooserTest {

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    private SurveyRepository surveyRepository;
    private PlaceRepository placeRepository;
    private UserRepository userRepository;
    private PlaceWithSurveyStatusListPresenter placeWithSurveyStatusListPresenter;
    private User user;
    private String username;
    private Place place1;
    private Place place2;
    private Place place3;

    @Before
    public void setUp() {
        surveyRepository = context.mock(SurveyRepository.class);
        placeRepository = context.mock(PlaceRepository.class);
        userRepository = context.mock(UserRepository.class);
        placeWithSurveyStatusListPresenter = context.mock(PlaceWithSurveyStatusListPresenter.class);

        username = "ice";
        user = User.fromUsername(username);

        place1 = Place.withName("abc");
        place2 = Place.withName("def");
        place3 = Place.withName("fgh");
    }

    @Test
    public void testShowPlaceBuildingList() throws Exception {

        final List<Place> places = new ArrayList<>();
        places.add(place1);
        places.add(place2);
        places.add(place3);

        final List<Place> surveyPlaces = new ArrayList<>();
        surveyPlaces.add(place2);

        final List<PlaceWithSurveyStatus> placeWithSurveyStatuses = new ArrayList<>();
        placeWithSurveyStatuses.add(new PlaceWithSurveyStatus(place1, false));
        placeWithSurveyStatuses.add(new PlaceWithSurveyStatus(place2, true));
        placeWithSurveyStatuses.add(new PlaceWithSurveyStatus(place3, false));

        context.checking(new Expectations() {
            {
                allowing(userRepository).findByUsername(with(username));
                will(returnValue(user));

                allowing(placeRepository).find();
                will(returnValue(places));

                allowing(surveyRepository).findByUserIn7Days(with(user));
                will(returnValue(surveyPlaces));

                allowing(placeWithSurveyStatusListPresenter).displayAllSurveyPlaceList(with(placeWithSurveyStatuses));
            }
        });
        SurveyPlaceChooser surveyPlaceHistoryController = new SurveyPlaceChooser(
                userRepository, placeRepository, surveyRepository, placeWithSurveyStatusListPresenter);
        surveyPlaceHistoryController.displaySurveyedPlaceOf(username);
    }

    @Test
    public void testShowPlaceNotSurveyList() throws Exception {

        final ArrayList<Place> places = new ArrayList<>();
        places.add(place1);
        places.add(place2);
        places.add(place3);

        final List<PlaceWithSurveyStatus> placeWithSurveyStatuses = new ArrayList<>();
        placeWithSurveyStatuses.add(new PlaceWithSurveyStatus(place1, false));
        placeWithSurveyStatuses.add(new PlaceWithSurveyStatus(place2, false));
        placeWithSurveyStatuses.add(new PlaceWithSurveyStatus(place3, false));

        context.checking(new Expectations() {
            {
                allowing(userRepository).findByUsername(with(username));
                will(returnValue(user));

                allowing(placeRepository).find();
                will(returnValue(places));

                allowing(surveyRepository).findByUserIn7Days(with(user));
                will(returnValue(null));

                allowing(placeWithSurveyStatusListPresenter).displayAllSurveyPlaceList(with(placeWithSurveyStatuses));
            }
        });
        SurveyPlaceChooser surveyPlaceHistoryController = new SurveyPlaceChooser(
                userRepository, placeRepository, surveyRepository, placeWithSurveyStatusListPresenter);
        surveyPlaceHistoryController.displaySurveyedPlaceOf(username);
    }

    @Test
    public void testShowPlacesNotFound() throws Exception {

        context.checking(new Expectations() {
            {
                allowing(userRepository).findByUsername(with(username));
                will(returnValue(user));

                allowing(placeRepository).find();
                will(returnValue(null));

                oneOf(placeWithSurveyStatusListPresenter).displayPlacesNotfound();

                never(surveyRepository);
                never(placeWithSurveyStatusListPresenter);
            }
        });
        SurveyPlaceChooser surveyPlaceHistoryController = new SurveyPlaceChooser(
                userRepository, placeRepository, surveyRepository, placeWithSurveyStatusListPresenter);
        surveyPlaceHistoryController.displaySurveyedPlaceOf(username);
    }


    @Test
    public void testNotFoundUser() throws Exception {

        context.checking(new Expectations() {
            {
                allowing(userRepository).findByUsername(with(username));
                will(returnValue(null));
                oneOf(placeWithSurveyStatusListPresenter).alertUserNotFound();
            }
        });
        SurveyPlaceChooser surveyPlaceHistoryController = new SurveyPlaceChooser(
                userRepository, placeRepository, surveyRepository, placeWithSurveyStatusListPresenter);
        surveyPlaceHistoryController.displaySurveyedPlaceOf(username);
    }

}



