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

import th.or.nectec.tanrabad.entity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface SurveyRepository {
    boolean save(Survey survey);

    Survey findByBuildingAndUserIn7Day(Building building, User user);

    ArrayList<Survey> findByPlaceAndUserIn7Days(Place place, User user);

    List<SurveyDetail> getSurveyDetail(UUID surveyId, int containerLocationID);

    ArrayList<Place> findByUserIn7Days(User user);
}
