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
import th.or.nectec.tanrabad.domain.building.BuildingRepository;
import th.or.nectec.tanrabad.domain.building.BuildingWithSurveyStatus;
import th.or.nectec.tanrabad.domain.building.BuildingWithSurveyStatusListPresenter;
import th.or.nectec.tanrabad.domain.place.PlaceRepository;
import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.entity.Survey;
import th.or.nectec.tanrabad.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SurveyBuildingChooser {

    private UserRepository userRepository;
    private PlaceRepository placeRepository;
    private BuildingRepository buildingRepository;
    private SurveyRepository surveyRepository;
    private BuildingWithSurveyStatusListPresenter surveyBuildingPresenter;
    private User user;
    private Place place;

    public SurveyBuildingChooser(UserRepository userRepository, PlaceRepository placeRepository, BuildingRepository buildingRepository, SurveyRepository surveyRepository, BuildingWithSurveyStatusListPresenter surveyBuildingPresenter) {

        this.userRepository = userRepository;
        this.placeRepository = placeRepository;
        this.buildingRepository = buildingRepository;
        this.surveyRepository = surveyRepository;
        this.surveyBuildingPresenter = surveyBuildingPresenter;
    }

    public void displaySurveyBuildingOf(String placeUUID, String username) {
        if (!isUserAndPlaceFound(placeUUID, username)) return;

        List<Building> buildings = buildingRepository.findByPlaceUUID(place.getId());

        checkBuildingAreFoundAndUpdateBuildingSurveyStatus(buildings);
    }

    private boolean isUserAndPlaceFound(String placeUUID, String username) {
        user = userRepository.findByUsername(username);
        if (user == null) {
            surveyBuildingPresenter.alertUserNotFound();
            return false;
        }

        place = placeRepository.findByUUID(UUID.fromString(placeUUID));
        if (place == null) {
            surveyBuildingPresenter.alertPlaceNotFound();
            return false;
        }
        return true;
    }

    private void checkBuildingAreFoundAndUpdateBuildingSurveyStatus(List<Building> buildings) {
        if (buildings == null) {
            surveyBuildingPresenter.alertBuildingsNotFound();
            return;
        }

        List<Survey> surveys = surveyRepository.findByPlaceAndUserIn7Days(place, user);
        List<BuildingWithSurveyStatus> buildingsWithSurveyStatuses = new ArrayList<>();
        for (Building eachBuilding : buildings) {
            BuildingWithSurveyStatus buildingWithSurveyStatus = new BuildingWithSurveyStatus(eachBuilding, surveys != null && isBuildingSurveyed(surveys, eachBuilding));
            buildingsWithSurveyStatuses.add(buildingWithSurveyStatus);
        }
        surveyBuildingPresenter.displayAllSurveyBuildingList(buildingsWithSurveyStatuses);
    }

    private boolean isBuildingSurveyed(List<Survey> surveys, Building eachBuilding) {
        for (Survey eachSurvey : surveys) {
            if (eachSurvey.getSurveyBuilding().equals(eachBuilding)) {
                return true;
            }
        }
        return false;
    }

    public void searchSurveyBuildingOfPlaceByName(String searchBuildingName, String placeUUID, String username) {
        if (!isUserAndPlaceFound(placeUUID, username)) return;

        List<Building> buildings = buildingRepository.findByPlaceUUIDAndBuildingName(place.getId(), searchBuildingName);

        checkBuildingAreFoundAndUpdateBuildingSurveyStatus(buildings);
    }
}
