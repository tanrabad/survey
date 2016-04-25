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

package org.tanrabad.survey.domain.survey;

import java.util.List;
import java.util.UUID;

import org.tanrabad.survey.domain.WritableRepository;
import org.tanrabad.survey.domain.building.BuildingWithSurveyStatus;
import org.tanrabad.survey.entity.Building;
import org.tanrabad.survey.entity.Place;
import org.tanrabad.survey.entity.Survey;
import org.tanrabad.survey.entity.SurveyDetail;
import org.tanrabad.survey.entity.User;

public interface SurveyRepository extends WritableRepository<Survey> {

    Survey findByBuildingAndUserIn7Day(Building building, User user);

    List<Survey> findByPlaceAndUserIn7Days(Place place, User user);

    List<BuildingWithSurveyStatus> findSurveyBuilding(Place place, User user);

    List<BuildingWithSurveyStatus> findSurveyBuildingByBuildingName(Place place, User user, String buildingName);

    List<SurveyDetail> findSurveyDetail(UUID surveyId, int containerLocationId);

    List<Place> findByUserIn7Days(User user);
}
