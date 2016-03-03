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

import java.util.List;
import java.util.UUID;

import th.or.nectec.tanrabad.domain.building.BuildingWithSurveyStatus;
import th.or.nectec.tanrabad.domain.building.BuildingWithSurveyStatusListPresenter;
import th.or.nectec.tanrabad.domain.place.PlaceRepository;
import th.or.nectec.tanrabad.domain.user.UserRepository;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.entity.User;

public class SurveyBuildingChooser {

    private UserRepository userRepository;
    private PlaceRepository placeRepository;
    private SurveyRepository surveyRepository;
    private BuildingWithSurveyStatusListPresenter surveyBuildingPresenter;
    private User user;
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
        this.user = userRepository.findByUsername(user.getUsername());
        if (this.user == null) {
            surveyBuildingPresenter.alertUserNotFound();
            return false;
        }

        place = placeRepository.findByUUID(UUID.fromString(placeUuid));
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
