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

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import th.or.nectec.tanrabad.domain.building.BuildingDuplicateException;
import th.or.nectec.tanrabad.domain.building.BuildingRepository;
import th.or.nectec.tanrabad.entity.Building;

public class InMemoryBuildingRepository implements BuildingRepository {

    private static InMemoryBuildingRepository instance;
    List<Building> buildings = new ArrayList<>();

    private InMemoryBuildingRepository() {
        InMemoryPlaceRepository inMemoryPlaceRepository = InMemoryPlaceRepository.getInstance();

        Building building1 = new Building(generateUUID("1xyz"), "214/43");
        building1.setPlace(inMemoryPlaceRepository.getPalazzettoVillage());
        buildings.add(building1);

        Building building2 = new Building(generateUUID("2xyz"), "214/44");
        building2.setPlace(inMemoryPlaceRepository.getPalazzettoVillage());
        buildings.add(building2);

        Building building3 = new Building(generateUUID("3xyz"), "214/45");
        building3.setPlace(inMemoryPlaceRepository.getPalazzettoVillage());
        buildings.add(building3);

        Building buildingPR01 = new Building(generateUUID("PR01xyz"), "214/46");
        buildingPR01.setPlace(inMemoryPlaceRepository.getPalazzettoVillage());
        buildings.add(buildingPR01);

        Building buildingPR02 = new Building(generateUUID("PR02xyz"), "214/47");
        buildingPR02.setPlace(inMemoryPlaceRepository.getPalazzettoVillage());
        buildings.add(buildingPR02);

        Building buildingPR03 = new Building(generateUUID("PR03xyz"), "214/48");
        buildingPR03.setPlace(inMemoryPlaceRepository.getPalazzettoVillage());
        buildings.add(buildingPR03);

        Building buildingPR04 = new Building(generateUUID("PR04xyz"), "214/49");
        buildingPR04.setPlace(inMemoryPlaceRepository.getPalazzettoVillage());
        buildings.add(buildingPR04);

        Building buildingPR05 = new Building(generateUUID("PR05xyz"), "214/50");
        buildingPR05.setPlace(inMemoryPlaceRepository.getPalazzettoVillage());
        buildings.add(buildingPR05);

        Building buildingPR06 = new Building(generateUUID("PR06xyz"), "214/51");
        buildingPR06.setPlace(inMemoryPlaceRepository.getPalazzettoVillage());
        buildings.add(buildingPR06);

        Building buildingPR07 = new Building(generateUUID("PR07xyz"), "214/52");
        buildingPR07.setPlace(inMemoryPlaceRepository.getPalazzettoVillage());
        buildings.add(buildingPR07);

        Building buildingPR08 = new Building(generateUUID("PR08xyz"), "214/53");
        buildingPR08.setPlace(inMemoryPlaceRepository.getPalazzettoVillage());
        buildings.add(buildingPR08);

        Building buildingPR09 = new Building(generateUUID("PR09xyz"), "214/54");
        buildingPR09.setPlace(inMemoryPlaceRepository.getPalazzettoVillage());
        buildings.add(buildingPR09);

        Building buildingPR10 = new Building(generateUUID("PR10xyz"), "214/55");
        buildingPR10.setPlace(inMemoryPlaceRepository.getPalazzettoVillage());
        buildings.add(buildingPR10);

        Building building4 = new Building(generateUUID("1opj"), "ตึก1");
        building4.setPlace(inMemoryPlaceRepository.getBangkokHospital());
        buildings.add(building4);

        Building building5 = new Building(generateUUID("2opj"), "ตึกพักญาติ");
        building5.setPlace(inMemoryPlaceRepository.getBangkokHospital());
        buildings.add(building5);

        Building building6 = new Building(generateUUID("3opj"), "โรงอาหาร");
        building6.setPlace(inMemoryPlaceRepository.getBangkokHospital());
        buildings.add(building6);

        Building building7 = new Building(generateUUID("1hij"), "ศาลาใหญ่");
        building7.setPlace(inMemoryPlaceRepository.getWatpaphukon());
        buildings.add(building7);

        Building building8 = new Building(generateUUID("2hij"), "เมรุ");
        building8.setPlace(inMemoryPlaceRepository.getWatpaphukon());
        buildings.add(building8);

        Building building9 = new Building(generateUUID("3hij"), "ลานหน้าศาลากลาง");
        building9.setPlace(inMemoryPlaceRepository.getWatpaphukon());
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
    public boolean save(Building building) {
        if (buildings.contains(building)) {
            throw new BuildingDuplicateException();
        } else {
            buildings.add(building);
        }
        return true;
    }

}
