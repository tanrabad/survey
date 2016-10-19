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

package org.tanrabad.survey.repository;

import org.tanrabad.survey.TanrabadApp;
import org.tanrabad.survey.domain.building.BuildingWithSurveyStatus;
import org.tanrabad.survey.domain.survey.SurveyRepository;
import org.tanrabad.survey.entity.Building;
import org.tanrabad.survey.entity.Place;
import org.tanrabad.survey.entity.Survey;
import org.tanrabad.survey.entity.SurveyDetail;
import org.tanrabad.survey.entity.User;
import org.tanrabad.survey.repository.persistence.DbSurveyRepository;

import java.util.List;
import java.util.UUID;

public final class BrokerSurveyRepository implements SurveyRepository {

    private static final String TAG = "BrokerSurveyRepository";
    private static BrokerSurveyRepository instance;
    private final SurveyRepository cache;
    private final SurveyRepository persistent;

    private BrokerSurveyRepository(SurveyRepository cache, SurveyRepository persistent) {
        this.cache = cache;
        this.persistent = persistent;
    }

    public static BrokerSurveyRepository getInstance() {
        if (instance == null)
            instance = new BrokerSurveyRepository(InMemorySurveyRepository.getInstance(),
                    new DbSurveyRepository(TanrabadApp.getInstance()));
        return instance;
    }

    @Override
    public Survey findRecent(Building building, User user) {
        return persistent.findRecent(building, user);
    }

    @Override
    public List<Survey> findRecent(Place place, User user) {
        return persistent.findRecent(place, user);
    }

    @Override
    public List<BuildingWithSurveyStatus> findSurveyBuilding(Place place, User user) {
        return persistent.findSurveyBuilding(place, user);
    }

    @Override
    public List<BuildingWithSurveyStatus> findSurveyBuildingByBuildingName(
            Place place, User user, String buildingName) {
        return persistent.findSurveyBuildingByBuildingName(place, user, buildingName);
    }

    @Override
    public List<SurveyDetail> findSurveyDetail(UUID surveyId, int containerLocationId) {
        return persistent.findSurveyDetail(surveyId, containerLocationId);
    }

    @Override
    public List<Place> findByUserIn7Days(User user) {
        return persistent.findByUserIn7Days(user);
    }

    @Override
    public boolean save(Survey survey) {
        boolean success = persistent.save(survey);
        if (success)
            cache.save(survey);
        return success;
    }

    @Override
    public boolean update(Survey survey) {
        boolean success = persistent.update(survey);
        if (success)
            cache.update(survey);
        return success;
    }

    @Override
    public boolean delete(Survey survey) {
        boolean success = persistent.delete(survey);
        if (success) {
            cache.delete(survey);
        }
        return success;
    }

    @Override
    public void updateOrInsert(List<Survey> surveys) {
        persistent.updateOrInsert(surveys);
        cache.updateOrInsert(surveys);
    }
}
