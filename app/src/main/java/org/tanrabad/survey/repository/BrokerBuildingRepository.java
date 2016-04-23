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
import org.tanrabad.survey.repository.persistence.DbBuildingRepository;
import org.tanrabad.survey.domain.building.BuildingRepository;
import th.or.nectec.tanrabad.entity.Building;

import java.util.List;
import java.util.UUID;

public final class BrokerBuildingRepository implements BuildingRepository {
    private static BrokerBuildingRepository instance;
    private BuildingRepository cache;
    private BuildingRepository database;

    private BrokerBuildingRepository(BuildingRepository cache, BuildingRepository database) {
        this.cache = cache;
        this.database = database;
    }

    public static BrokerBuildingRepository getInstance() {
        if (instance == null) {
            instance = new BrokerBuildingRepository(InMemoryBuildingRepository.getInstance(),
                    new DbBuildingRepository(TanrabadApp.getInstance()));
        }
        return instance;
    }

    @Override
    public List<Building> findByPlaceUuid(UUID placeUuid) {
        return database.findByPlaceUuid(placeUuid);
    }

    @Override
    public List<Building> findByPlaceUuidAndBuildingName(UUID placeUuid, String buildingName) {
        return database.findByPlaceUuidAndBuildingName(placeUuid, buildingName);
    }

    @Override
    public Building findByUuid(UUID uuid) {
        Building building = cache.findByUuid(uuid);
        if (building == null) {
            building = database.findByUuid(uuid);
            cache.save(building);
        }
        return building;
    }

    @Override
    public boolean save(Building building) {
        boolean success = database.save(building);
        if (success)
            cache.save(building);
        return success;
    }

    @Override
    public boolean update(Building building) {
        boolean success = database.update(building);
        if (success)
            cache.update(building);
        return success;
    }

    @Override
    public boolean delete(Building building) {
        boolean success = database.delete(building);
        if (success)
            cache.delete(building);
        return success;
    }

    @Override
    public void updateOrInsert(List<Building> buildings) {
        database.updateOrInsert(buildings);
        cache.updateOrInsert(buildings);
    }
}
