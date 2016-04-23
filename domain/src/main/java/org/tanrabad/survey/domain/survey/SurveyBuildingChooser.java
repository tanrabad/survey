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

import java.util.List;
import java.util.UUID;

import org.tanrabad.survey.domain.building.BuildingWithSurveyStatus;
import org.tanrabad.survey.domain.building.BuildingWithSurveyStatusListPresenter;
import org.tanrabad.survey.domain.place.PlaceRepository;
import org.tanrabad.survey.domain.user.UserRepository;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.entity.User;

public class SurveyBuildingChooser {

    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;
    private final SurveyRepository surveyRepository;
    private final BuildingWithSurveyStatusListPresenter surveyBuildingPresenter;
    private Place place;

    public SurveyBuildingChooser(
            UserRepository userRepository,
            PlaceRepository placeRepository,
            SurveyRepository surveyRepository,
            BuildingWithSurveyStatusListPresenter surveyBuildingPresenter) {
        this.userRepository = userRepository;
        this.placeRepository = placeRepository;
        this.surveyRepository = surveyRepository;
        this.surveyBuildingPresenter = surveyBuildingPresenter;
    }

    public void displaySurveyBuildingOf(String placeUuid, User user) {
        if (!isUserAndPlaceFound(placeUuid, user)) return;

        List<BuildingWithSurveyStatus> buildings = surveyRepository.findSurveyBuilding(place, user);
        displaySurveyBuildingList(buildings);
    }

    private boolean isUserAndPlaceFound(String placeUuid, User user) {
        User queryUser = userRepository.findByUsername(user.getUsername());
        if (queryUser == null) {
            surveyBuildingPresenter.alertUserNotFound();
            return false;
        }

        place = placeRepository.findByUuid(UUID.fromString(placeUuid));
        if (place == null) {
            surveyBuildingPresenter.alertPlaceNotFound();
            return false;
        }
        return true;
    }

    private void displaySurveyBuildingList(List<BuildingWithSurveyStatus> buildings) {
        if (buildings.isEmpty()) {
            surveyBuildingPresenter.alertBuildingsNotFound();
        } else {
            surveyBuildingPresenter.displayAllSurveyBuildingList(buildings);
        }
    }

    public void searchSurveyBuildingOfPlaceByName(String searchBuildingName, String placeUuid, User user) {
        if (!isUserAndPlaceFound(placeUuid, user)) return;

        List<BuildingWithSurveyStatus> buildings = surveyRepository
                .findSurveyBuildingByBuildingName(place, user, searchBuildingName);
        displaySurveyBuildingList(buildings);
    }
}
