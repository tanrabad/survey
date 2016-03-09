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

import java.util.UUID;

import th.or.nectec.tanrabad.domain.building.BuildingRepository;
import th.or.nectec.tanrabad.domain.user.UserRepository;
import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.entity.Survey;
import th.or.nectec.tanrabad.entity.User;

public class SurveyController {
    private final SurveyRepository surveyRepository;
    private final SurveyPresenter surveyPresenter;

    private final BuildingRepository buildingRepository;
    private final UserRepository userRepository;

    public SurveyController(
            SurveyRepository surveyRepository, BuildingRepository buildingRepository,
            UserRepository userRepository, SurveyPresenter surveyPresenter) {
        this.surveyRepository = surveyRepository;
        this.buildingRepository = buildingRepository;
        this.userRepository = userRepository;
        this.surveyPresenter = surveyPresenter;
    }

    public void checkThisBuildingAndUserCanSurvey(String buildingUuid, String username) {
        User user = checkUserExist(username);
        if (user == null) return;

        Building building = checkBuildingExist(buildingUuid);
        if (building == null) return;

        Survey survey = surveyRepository.findByBuildingAndUserIn7Day(building, user);
        if (survey == null) {
            surveyPresenter.onNewSurvey(building, user);
        } else {
            surveyPresenter.onEditSurvey(survey);
        }
    }

    private User checkUserExist(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            surveyPresenter.alertUserNotFound();
            return null;
        }
        return user;
    }

    private Building checkBuildingExist(String buildingUuid) {
        Building building = buildingRepository.findByUuid(UUID.fromString(buildingUuid));
        if (building == null) {
            surveyPresenter.alertBuildingNotFound();
            return null;
        }
        return building;
    }
}
