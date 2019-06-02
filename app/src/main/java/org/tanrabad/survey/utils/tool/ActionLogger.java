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

package org.tanrabad.survey.utils.tool;

import org.tanrabad.survey.entity.Building;
import org.tanrabad.survey.entity.Place;
import org.tanrabad.survey.entity.Survey;
import org.tanrabad.survey.entity.User;
import org.tanrabad.survey.service.AbsRestService;

public interface ActionLogger {

    void login(User user);

    void useTorch(int durationSecond);

    void addBuilding(Building building);

    void updateBuilding(Building building);

    void addPlace(Place place);

    void updatePlace(Place place);

    void searchPlace(String query);

    void filterBuilding(String query);

    void firstTimeWithoutInternet();

    void startSurvey(Place place, String type);

    void startSurvey(Survey survey);

    void updateSurvey(Survey survey);

    void finishSurvey(Place place, boolean success);

    void finishSurvey(Survey survey, boolean success);

    void logout(User user);

    void cacheHit(AbsRestService<?> restService);
}
