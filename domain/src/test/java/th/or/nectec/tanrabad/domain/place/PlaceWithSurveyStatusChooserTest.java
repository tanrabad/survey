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

package th.or.nectec.tanrabad.domain.place;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import th.or.nectec.tanrabad.domain.UserRepository;
import th.or.nectec.tanrabad.domain.survey.SurveyRepository;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.entity.User;

import java.util.ArrayList;

public class PlaceWithSurveyStatusChooserTest {

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();
    private PlaceWithSurveyStatusChooserPresenter placeWithSurveyStatusChooserPresenter;
    private SurveyRepository surveyRepository;
    private UserRepository userRepository;
    private String username;
    private User user;

    @Before
    public void setup() {
        surveyRepository = context.mock(SurveyRepository.class);
        userRepository = context.mock(UserRepository.class);
        placeWithSurveyStatusChooserPresenter = context.mock(PlaceWithSurveyStatusChooserPresenter.class);
        username = "chn";
        user = User.fromUsername(username);
    }


    @Test
    public void testShowSurveyPlaceList() throws Exception {

        final ArrayList<Place> surveyPlace = new ArrayList<>();
        surveyPlace.add(Place.withName("a"));
        surveyPlace.add(Place.withName("b"));

        context.checking(new Expectations() {
            {
                oneOf(userRepository).findUserByName(with(username));
                will(returnValue(with(user)));
                oneOf(surveyRepository).findByUserIn7Days(with(user));
                will(returnValue(surveyPlace));
                oneOf(placeWithSurveyStatusChooserPresenter).displaySurveyPlaceList(surveyPlace);
            }
        });

        PlaceWithSurveyStatusChooser placeWithSurveyStatusChooser = new PlaceWithSurveyStatusChooser(userRepository, surveyRepository, placeWithSurveyStatusChooserPresenter);
        placeWithSurveyStatusChooser.showSurveyPlaceList(username);
    }

    @Test
    public void testDisplaySurveyPlaceNotFound() throws Exception {
        context.checking(new Expectations() {
            {
                oneOf(userRepository).findUserByName(with(username));
                will(returnValue(user));
                oneOf(surveyRepository).findByUserIn7Days(with(user));
                will(returnValue(null));
                oneOf(placeWithSurveyStatusChooserPresenter).displaySurveyPlacesNotFound();
            }
        });

        PlaceWithSurveyStatusChooser placeWithSurveyStatusChooser = new PlaceWithSurveyStatusChooser(userRepository, surveyRepository, placeWithSurveyStatusChooserPresenter);
        placeWithSurveyStatusChooser.showSurveyPlaceList(username);
    }

    @Test
    public void testAlertUserNotFound() throws Exception {
        context.checking(new Expectations() {
            {
                oneOf(userRepository).findUserByName(with(username));
                will(returnValue(null));
                oneOf(placeWithSurveyStatusChooserPresenter).alertUserNotFound();
            }
        });

        PlaceWithSurveyStatusChooser placeWithSurveyStatusChooser = new PlaceWithSurveyStatusChooser(userRepository, surveyRepository, placeWithSurveyStatusChooserPresenter);
        placeWithSurveyStatusChooser.showSurveyPlaceList(username);
    }
}
