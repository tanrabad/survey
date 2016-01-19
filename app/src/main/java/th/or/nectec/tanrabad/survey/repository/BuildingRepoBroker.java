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

package th.or.nectec.tanrabad.survey.repository;


import th.or.nectec.tanrabad.domain.building.BuildingRepository;
import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.survey.TanrabadApp;
import th.or.nectec.tanrabad.survey.repository.persistence.DbBuildingRepository;

import java.util.List;
import java.util.UUID;

public class BuildingRepoBroker implements BuildingRepository
{
    private static BuildingRepoBroker instance;
    private BuildingRepository cache;
    private BuildingRepository database;

    protected BuildingRepoBroker(BuildingRepository database, BuildingRepository cache) {
        this.database = database;
        this.cache = cache;
    }

    public static BuildingRepoBroker getInstance() {
        if (instance == null) {
            instance = new BuildingRepoBroker(InMemoryBuildingRepository.getInstance(),
                    new DbBuildingRepository(TanrabadApp.getInstance()));
        }
        return instance;
    }

    @Override
    public List<Building> findBuildingInPlace(UUID placeUuid) {
        return database.findBuildingInPlace(placeUuid);
    }

    @Override
    public Building findBuildingByUUID(UUID uuid) {
        Building building = cache.findBuildingByUUID(uuid);
        if(building == null){
            building = database.findBuildingByUUID(uuid);
            cache.save(building);
        }
        return building;
    }

    @Override
    public boolean save(Building building) {
        boolean success = database.save(building);
        if(success)
            cache.save(building);
        return success;
    }

    @Override
    public boolean update(Building building) {
        boolean success = database.update(building);
        if(success)
            cache.update(building);
        return success;
    }

    @Override
    public void updateOrInsert(List<Building> buildings) {
        database.updateOrInsert(buildings);
        cache.updateOrInsert(buildings);
    }

    @Override
    public List<Building> searchBuildingInPlaceByName(UUID placeUUID, String buildingName) {
        return database.searchBuildingInPlaceByName(placeUUID, buildingName);
    }
}
