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

package th.or.nectec.tanrabad.survey.repository;

import th.or.nectec.tanrabad.domain.survey.SurveyRepository;
import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.entity.Survey;
import th.or.nectec.tanrabad.entity.User;

import java.util.ArrayList;

public class InMemorySurveyRepository implements SurveyRepository {

    private static InMemorySurveyRepository instance;
    ArrayList<Survey> surveys;

    private InMemorySurveyRepository() {
        this.surveys = new ArrayList<>();
    }

    public static InMemorySurveyRepository getInstance() {
        if (instance == null)
            instance = new InMemorySurveyRepository();
        return instance;
    }

    @Override
    public boolean save(Survey survey) {
        if (surveys.contains(survey)) {
            surveys.set(surveys.indexOf(survey), survey);
        } else {
            surveys.add(survey);
        }

        return true;
    }

    @Override
    public Survey findByBuildingAndUserIn7Day(Building building, User user) {
        for (Survey eachSurvey : surveys) {
            if (eachSurvey.getSurveyBuilding().equals(building) && eachSurvey.getUser().equals(user)) {
                return eachSurvey;
            }
        }
        return null;
    }

    @Override
    public ArrayList<Survey> findByPlaceAndUserIn7Days(Place place, User user) {
        ArrayList<Survey> surveyBuilding = new ArrayList<>();
        for (Survey eachSurvey : surveys) {
            if (eachSurvey.getSurveyBuilding().getPlace().equals(place) && eachSurvey.getUser().equals(user)) {
                surveyBuilding.add(eachSurvey);
            }
        }
        return surveyBuilding.isEmpty() ? null : surveyBuilding;
    }

    @Override
    public ArrayList<Place> findByUserIn7Days(User user) {
        ArrayList<Place> surveyPlaces = new ArrayList<>();
        for (Survey eachSurvey : surveys) {
            if (eachSurvey.getUser().equals(user)) {
                Building surveyBuilding = eachSurvey.getSurveyBuilding();
                if(!surveyPlaces.contains(surveyBuilding.getPlace())){
                    surveyPlaces.add(surveyBuilding.getPlace());
                }
            }
        }
        return surveyPlaces.isEmpty() ? null : surveyPlaces;
    }
}
