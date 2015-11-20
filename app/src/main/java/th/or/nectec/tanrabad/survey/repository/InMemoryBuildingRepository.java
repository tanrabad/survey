/*
 * Copyright (c) 2015  NECTEC
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

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import th.or.nectec.tanrabad.domain.BuildingRepository;
import th.or.nectec.tanrabad.entity.Building;

public class InMemoryBuildingRepository implements BuildingRepository {

    private static InMemoryBuildingRepository instance;
    List<Building> buildings = new ArrayList<>();

    public InMemoryBuildingRepository() {
        StubPlaceRepository stubPlaceRepository = new StubPlaceRepository();
        Building building1 = new Building(generateUUID("1xyz"), "214/43");
        building1.setPlace(stubPlaceRepository.getPalazzettoVillage());
        buildings.add(building1);

        Building building2 = new Building(generateUUID("2xyz"), "214/44");
        building2.setPlace(stubPlaceRepository.getPalazzettoVillage());
        buildings.add(building2);

        Building building3 = new Building(generateUUID("3xyz"), "214/45");
        building3.setPlace(stubPlaceRepository.getPalazzettoVillage());
        buildings.add(building3);

        Building building4 = new Building(generateUUID("1opj"), "ตึก1");
        building4.setPlace(stubPlaceRepository.getBangkokHospital());
        buildings.add(building4);

        Building building5 = new Building(generateUUID("2opj"), "ตึกพักญาติ");
        building5.setPlace(stubPlaceRepository.getBangkokHospital());
        buildings.add(building5);

        Building building6 = new Building(generateUUID("3opj"), "โรงอาหาร");
        building6.setPlace(stubPlaceRepository.getBangkokHospital());
        buildings.add(building6);

        Building building7 = new Building(generateUUID("1hij"), "ศาลาใหญ่");
        building7.setPlace(stubPlaceRepository.getWatpaphukon());
        buildings.add(building7);

        Building building8 = new Building(generateUUID("2hij"), "เมรุ");
        building8.setPlace(stubPlaceRepository.getWatpaphukon());
        buildings.add(building8);

        Building building9 = new Building(generateUUID("3hij"), "ลานหน้าศาลากลาง");
        building9.setPlace(stubPlaceRepository.getWatpaphukon());
        buildings.add(building9);
    }

    public static InMemoryBuildingRepository getInstance() {
        if (instance == null)
            instance = new InMemoryBuildingRepository();
        return instance;
    }

    @NonNull
    private UUID generateUUID(String input) {
        return UUID.nameUUIDFromBytes(input.getBytes());
    }

    @Override
    public List<Building> findBuildingInPlace(UUID placeUuid) {
        ArrayList<Building> newBuildingList = new ArrayList<>();
        for (Building eachBuilding : buildings) {
            if (eachBuilding.getPlace().getId().equals(placeUuid)) {
                newBuildingList.add(eachBuilding);
            }
        }
        return newBuildingList.isEmpty() ? null : newBuildingList;
    }

    @Override
    public Building findBuildingByName(String buildingName) {
        for (Building eachBuilding : buildings) {
            if (eachBuilding.getName().equals(buildingName)) {
                return eachBuilding;
            }
        }
        return null;
    }

    @Override
    public Building findBuildingByUUID(UUID buildingUUID) {
        for (Building eachBuilding : buildings) {
            if (eachBuilding.getId().equals(buildingUUID)) {
                return eachBuilding;
            }
        }
        return null;
    }

    @Override
    public boolean save(Building with) {
        return false;
    }
}
