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

import java.util.ArrayList;
import java.util.List;

import th.or.nectec.tanrabad.domain.UserRepository;
import th.or.nectec.tanrabad.domain.place.PlaceRepository;
import th.or.nectec.tanrabad.domain.place.PlaceWithSurveyStatus;
import th.or.nectec.tanrabad.domain.place.PlaceWithSurveyStatusListPresenter;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.entity.User;

public class SurveyPlaceChooser {

    private UserRepository userRepository;
    private PlaceRepository placeRepository;
    private SurveyRepository surveyRepository;
    private PlaceWithSurveyStatusListPresenter surveyPlacePresenter;

    public SurveyPlaceChooser(UserRepository userRepository, PlaceRepository placeRepository, SurveyRepository surveyRepository, PlaceWithSurveyStatusListPresenter surveyBuildingPresenter) {

        this.userRepository = userRepository;
        this.placeRepository = placeRepository;
        this.surveyRepository = surveyRepository;
        this.surveyPlacePresenter = surveyBuildingPresenter;
    }

    public void displaySurveyBuildingOf(String username) {
        User user = userRepository.findUserByName(username);
        if (user == null) {
            surveyPlacePresenter.alertUserNotFound();
            return;
        }

        List<Place> places = placeRepository.findPlaces();
        if (places == null) {
            surveyPlacePresenter.displayPlacesNotfound();
            return;
        }

        List<Place> surveyPlaces = surveyRepository.findByUserIn7Days(user);

        List<PlaceWithSurveyStatus> buildingsWithSurveyStatuses = new ArrayList<>();

        for (Place eachPlace : places) {
            PlaceWithSurveyStatus buildingWithSurveyStatus = new PlaceWithSurveyStatus(eachPlace, surveyPlaces != null && surveyPlaces.remove(eachPlace));
            buildingsWithSurveyStatuses.add(buildingWithSurveyStatus);
        }

        surveyPlacePresenter.displayAllSurveyPlaceList(buildingsWithSurveyStatuses);
    }

    public void getPlaceListWithPlaceFilter(int selectedID, String username) {
        User user = userRepository.findUserByName(username);
        if (user == null) {
            surveyPlacePresenter.alertUserNotFound();
            return;
        }

        List<Place> places = placeRepository.findPlacesWithPlaceFilter(selectedID);
        if (places == null) {
            surveyPlacePresenter.displayPlacesNotfound();
            return;
        }

        List<Place> surveyPlaces = surveyRepository.findByUserIn7Days(user);

        List<PlaceWithSurveyStatus> buildingsWithSurveyStatuses = new ArrayList<>();

        for (Place eachPlace : places) {
            PlaceWithSurveyStatus buildingWithSurveyStatus = new PlaceWithSurveyStatus(eachPlace, surveyPlaces != null && surveyPlaces.remove(eachPlace));
            buildingsWithSurveyStatuses.add(buildingWithSurveyStatus);
        }

        surveyPlacePresenter.displayAllSurveyPlaceList(buildingsWithSurveyStatuses);
    }
}