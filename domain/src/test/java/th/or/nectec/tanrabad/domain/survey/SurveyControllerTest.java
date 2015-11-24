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
import th.or.nectec.tanrabad.domain.building.BuildingRepository;
import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.entity.Survey;
import th.or.nectec.tanrabad.entity.User;

import java.util.UUID;

public class SurveyControllerTest {
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();
    private SurveyRepository surveyRepository;
    private SurveyPresenter surveyPresenter;
    private Building building;
    private User user;
    private String buildingUUID;
    private String username;
    private UserRepository userRepository;
    private BuildingRepository buildingRepository;

    @Before
    public void setUp() throws Exception {
        surveyRepository = context.mock(SurveyRepository.class);
        buildingRepository = context.mock(BuildingRepository.class);
        userRepository = context.mock(UserRepository.class);
        surveyPresenter = context.mock(SurveyPresenter.class);

        buildingUUID = UUID.nameUUIDFromBytes("2xyz".getBytes()).toString();
        username = "chncs23";

        building = new Building(UUID.fromString(buildingUUID), "33/5");
        user = User.fromUsername(username);
    }

    @Test
    public void testLoadSurveySuccess() throws Exception {

        final Survey surveys = new Survey(user, building);

        context.checking(new Expectations() {
            {
                allowing(buildingRepository).findBuildingByUUID(with(UUID.fromString(buildingUUID)));
                will(returnValue(building));
                allowing(userRepository).findUserByName(with(username));
                will(returnValue(user));
                allowing(surveyRepository).findByBuildingAndUserIn7Day(with(building), with(user));
                will(returnValue(surveys));
                oneOf(surveyPresenter).onEditSurvey(with(surveys));
            }
        });

        SurveyController surveyController = new SurveyController(surveyRepository, buildingRepository, userRepository, surveyPresenter);
        surveyController.checkThisBuildingAndUserCanSurvey(buildingUUID, username);
    }

    @Test
    public void testStartNewSurvey() throws Exception {
        context.checking(new Expectations() {
            {
                allowing(buildingRepository).findBuildingByUUID(with(UUID.fromString(buildingUUID)));
                will(returnValue(building));
                allowing(userRepository).findUserByName(with(username));
                will(returnValue(user));
                allowing(surveyRepository).findByBuildingAndUserIn7Day(with(building), with(user));
                will(returnValue(null));
                oneOf(surveyPresenter).onNewSurvey(with(building), with(user));
            }
        });

        SurveyController surveyController = new SurveyController(surveyRepository, buildingRepository, userRepository, surveyPresenter);
        surveyController.checkThisBuildingAndUserCanSurvey(buildingUUID, username);
    }

    @Test
    public void testNotFoundUser() throws Exception {
        final String notExistUsername = "chncs24";
        context.checking(new Expectations() {
            {
                allowing(userRepository).findUserByName(with(notExistUsername));
                will(returnValue(null));
                never(buildingRepository);
                never(surveyRepository);
                oneOf(surveyPresenter).alertUserNotFound();
            }
        });

        SurveyController surveyController = new SurveyController(surveyRepository, buildingRepository, userRepository, surveyPresenter);
        surveyController.checkThisBuildingAndUserCanSurvey(buildingUUID, notExistUsername);
    }

    @Test
    public void testNotFoundBuilding() throws Exception {
        final UUID notExistBuildingUUID = UUID.nameUUIDFromBytes("2xyk".getBytes());
        context.checking(new Expectations() {
            {
                allowing(userRepository).findUserByName(with(username));
                will(returnValue(user));
                allowing(buildingRepository).findBuildingByUUID(with(notExistBuildingUUID));
                will(returnValue(null));
                never(surveyRepository);
                oneOf(surveyPresenter).alertBuildingNotFound();
            }
        });

        SurveyController surveyController = new SurveyController(surveyRepository, buildingRepository, userRepository, surveyPresenter);
        surveyController.checkThisBuildingAndUserCanSurvey(notExistBuildingUUID.toString(), username);
    }
}
