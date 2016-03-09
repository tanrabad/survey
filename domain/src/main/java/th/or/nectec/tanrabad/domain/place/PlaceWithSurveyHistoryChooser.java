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

package th.or.nectec.tanrabad.domain.place;

import java.util.List;

import th.or.nectec.tanrabad.domain.survey.SurveyRepository;
import th.or.nectec.tanrabad.domain.user.UserRepository;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.entity.User;

public class PlaceWithSurveyHistoryChooser {
    private final UserRepository userRepository;
    private final SurveyRepository surveyRepository;
    private final PlaceWithSurveyHistoryListPresenter placeWithSurveyStatusChooserPresenter;

    public PlaceWithSurveyHistoryChooser(UserRepository userRepository,
                                         SurveyRepository surveyRepository,
                                         PlaceWithSurveyHistoryListPresenter placeWithSurveyStatusChooserPresenter) {

        this.userRepository = userRepository;
        this.surveyRepository = surveyRepository;
        this.placeWithSurveyStatusChooserPresenter = placeWithSurveyStatusChooserPresenter;
    }

    public void showSurveyPlaceList(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            placeWithSurveyStatusChooserPresenter.alertUserNotFound();
            return;
        }

        List<Place> surveyPlaces = surveyRepository.findByUserIn7Days(user);

        if (surveyPlaces != null && !surveyPlaces.isEmpty()) {
            placeWithSurveyStatusChooserPresenter.displaySurveyPlaceList(surveyPlaces);
        } else {
            placeWithSurveyStatusChooserPresenter.displaySurveyPlacesNotFound();
        }
    }
}
