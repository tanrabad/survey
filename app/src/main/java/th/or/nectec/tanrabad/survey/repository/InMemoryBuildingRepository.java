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
import th.or.nectec.tanrabad.domain.building.BuildingRepositoryException;
import th.or.nectec.tanrabad.entity.Building;

import java.util.*;

public class InMemoryBuildingRepository implements BuildingRepository {

    private static InMemoryBuildingRepository instance;
    Map<UUID, Building> buildingMap = new HashMap<>();

    protected static InMemoryBuildingRepository getInstance() {
        if (instance == null)
            instance = new InMemoryBuildingRepository();
        return instance;
    }

    @Override
    public List<Building> findByPlaceUUID(UUID placeUuid) {

        ArrayList<Building> newBuildingList = new ArrayList<>();
        for (Building eachBuilding : buildingMap.values()) {
            if (eachBuilding.getPlace().getId().equals(placeUuid)) {
                newBuildingList.add(eachBuilding);
            }
        }
        return newBuildingList.isEmpty() ? null : newBuildingList;
    }

    @Override
    public List<Building> findByPlaceUUIDAndBuildingName(UUID placeUuid, String buildingName) {
        ArrayList<Building> newBuildingList = new ArrayList<>();
        for (Building eachBuilding : buildingMap.values()) {
            if (eachBuilding.getPlace().getId().equals(placeUuid) && eachBuilding.getName().contains(buildingName)) {
                newBuildingList.add(eachBuilding);
            }
        }
        return newBuildingList.isEmpty() ? null : newBuildingList;
    }

    @Override
    public Building findByUUID(UUID buildingUuid) {
        for (Building eachBuilding : buildingMap.values()) {
            if (eachBuilding.getId().equals(buildingUuid)) {
                return eachBuilding;
            }
        }
        return null;
    }

    @Override
    public boolean save(Building building) {
        if (buildingMap.containsKey(building.getId())) {
            throw new BuildingRepositoryException();
        }
        buildingMap.put(building.getId(), building);
        return true;
    }

    @Override
    public boolean update(Building building) {
        if (!buildingMap.containsKey(building.getId())) {
            throw new BuildingRepositoryException();
        }
        buildingMap.put(building.getId(), building);
        return true;
    }

    @Override
    public void updateOrInsert(List<Building> buildings) {
        for (Building building : buildings) {
            try {
                update(building);
            } catch (BuildingRepositoryException iob) {
                save(building);
            }
        }
    }
}
