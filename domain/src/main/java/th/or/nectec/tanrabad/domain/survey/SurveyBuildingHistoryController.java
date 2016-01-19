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

import th.or.nectec.tanrabad.domain.UserRepository;
import th.or.nectec.tanrabad.domain.place.PlaceRepository;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.entity.Survey;
import th.or.nectec.tanrabad.entity.User;

import java.util.List;
import java.util.UUID;

public class SurveyBuildingHistoryController {
    private UserRepository userRepository;
    private PlaceRepository placeRepository;
    private SurveyRepository surveyRepository;
    private SurveyBuildingPresenter surveyBuildingPresenter;

    public SurveyBuildingHistoryController(UserRepository userRepository, PlaceRepository placeRepository, SurveyRepository surveyRepository, SurveyBuildingPresenter surveyBuildingPresenter) {
        this.userRepository = userRepository;
        this.placeRepository = placeRepository;
        this.surveyRepository = surveyRepository;
        this.surveyBuildingPresenter = surveyBuildingPresenter;
    }


    public void showSurveyBuildingOf(String placeUUID, String username) {
        User user = checkUserExist(username);
        if (user == null) return;

        Place place = checkPlaceExist(placeUUID);
        if (place == null) return;

        List<Survey> buildings = surveyRepository.findByPlaceAndUserIn7Days(place, user);
        if (buildings == null) {
            surveyBuildingPresenter.displaySurveyBuildingsNotFound();
            return;
        }

        surveyBuildingPresenter.displaySurveyBuildingList(buildings);
    }

    private User checkUserExist(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {

            surveyBuildingPresenter.alertUserNotFound();
            return null;
        }
        return user;
    }

    private Place checkPlaceExist(String placeUUID) {
        Place place = placeRepository.findByUUID(UUID.fromString(placeUUID));
        if (place == null) {

            surveyBuildingPresenter.alertPlaceNotFound();
            return null;
        }
        return place;
    }

}
