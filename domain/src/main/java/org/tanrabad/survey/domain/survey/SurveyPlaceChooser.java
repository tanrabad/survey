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

import java.util.ArrayList;
import java.util.List;

import org.tanrabad.survey.domain.place.PlaceRepository;
import org.tanrabad.survey.domain.place.PlaceWithSurveyStatus;
import org.tanrabad.survey.domain.place.PlaceWithSurveyStatusListPresenter;
import org.tanrabad.survey.domain.user.UserRepository;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.entity.User;

public class SurveyPlaceChooser {

    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;
    private final SurveyRepository surveyRepository;
    private final PlaceWithSurveyStatusListPresenter surveyPlacePresenter;

    public SurveyPlaceChooser(UserRepository userRepository,
                              PlaceRepository placeRepository,
                              SurveyRepository surveyRepository,
                              PlaceWithSurveyStatusListPresenter surveyBuildingPresenter) {
        this.userRepository = userRepository;
        this.placeRepository = placeRepository;
        this.surveyRepository = surveyRepository;
        this.surveyPlacePresenter = surveyBuildingPresenter;
    }

    public void displaySurveyedPlaceOf(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            surveyPlacePresenter.alertUserNotFound();
            return;
        }
        List<Place> places = placeRepository.find();
        if (places == null) {
            surveyPlacePresenter.displayPlacesNotfound();
            return;
        }
        List<Place> surveyPlaces = surveyRepository.findByUserIn7Days(user);
        List<PlaceWithSurveyStatus> placeWithSurveyStatusList = new ArrayList<>();
        for (Place eachPlace : places) {
            PlaceWithSurveyStatus place = new PlaceWithSurveyStatus(eachPlace,
                    surveyPlaces != null && surveyPlaces.remove(eachPlace));
            placeWithSurveyStatusList.add(place);
        }
        surveyPlacePresenter.displayAllSurveyPlaceList(placeWithSurveyStatusList);
    }
}
