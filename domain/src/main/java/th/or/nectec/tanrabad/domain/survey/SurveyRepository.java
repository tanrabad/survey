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

import java.util.List;
import java.util.UUID;

import th.or.nectec.tanrabad.domain.WritableRepository;
import th.or.nectec.tanrabad.domain.building.BuildingWithSurveyStatus;
import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.entity.Survey;
import th.or.nectec.tanrabad.entity.SurveyDetail;
import th.or.nectec.tanrabad.entity.User;

public interface SurveyRepository extends WritableRepository<Survey> {

    Survey findByBuildingAndUserIn7Day(Building building, User user);

    List<Survey> findByPlaceAndUserIn7Days(Place place, User user);

    List<BuildingWithSurveyStatus> findSurveyBuilding(Place place, User user);

    List<BuildingWithSurveyStatus> findSurveyBuildingByBuildingName(Place place, User user, String buildingName);

    List<SurveyDetail> findSurveyDetail(UUID surveyId, int containerLocationID);

    List<Place> findByUserIn7Days(User user);
}
