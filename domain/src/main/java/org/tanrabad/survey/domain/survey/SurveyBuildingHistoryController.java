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

package org.tanrabad.survey.domain.survey;

import org.tanrabad.survey.domain.place.PlaceRepository;
import org.tanrabad.survey.domain.user.UserRepository;
import org.tanrabad.survey.entity.Place;
import org.tanrabad.survey.entity.Survey;
import org.tanrabad.survey.entity.User;

import java.util.List;
import java.util.UUID;

public class SurveyBuildingHistoryController {
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;
    private final SurveyRepository surveyRepository;
    private final SurveyBuildingPresenter surveyBuildingPresenter;

    public SurveyBuildingHistoryController(UserRepository userRepository,
                                           PlaceRepository placeRepository,
                                           SurveyRepository surveyRepository,
                                           SurveyBuildingPresenter surveyBuildingPresenter) {
        this.userRepository = userRepository;
        this.placeRepository = placeRepository;
        this.surveyRepository = surveyRepository;
        this.surveyBuildingPresenter = surveyBuildingPresenter;
    }

    public void showSurveyBuildingOf(String placeUuid, String username) {
        User user = checkUserExist(username);
        if (user == null) return;
        Place place = checkPlaceExist(placeUuid);
        if (place == null) return;
        List<Survey> surveys = surveyRepository.findRecent(place, user);
        if (surveys != null && !surveys.isEmpty()) {
            surveyBuildingPresenter.displaySurveyBuildingList(surveys);
        } else {
            surveyBuildingPresenter.displaySurveyBuildingsNotFound();
        }
    }

    private User checkUserExist(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            surveyBuildingPresenter.alertUserNotFound();
            return null;
        }
        return user;
    }

    private Place checkPlaceExist(String placeUuid) {
        Place place = placeRepository.findByUuid(UUID.fromString(placeUuid));
        if (place == null) {

            surveyBuildingPresenter.alertPlaceNotFound();
            return null;
        }
        return place;
    }

}
