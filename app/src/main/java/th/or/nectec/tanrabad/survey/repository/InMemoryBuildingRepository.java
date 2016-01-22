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

import android.support.annotation.NonNull;
import th.or.nectec.tanrabad.domain.building.BuildingRepository;
import th.or.nectec.tanrabad.domain.building.BuildingRepositoryException;
import th.or.nectec.tanrabad.entity.Building;

import java.util.*;

public class InMemoryBuildingRepository implements BuildingRepository {

    private static InMemoryBuildingRepository instance;
    Map<UUID,Building> buildingMap = new HashMap<>();


    private InMemoryBuildingRepository() {
        InMemoryPlaceRepository inMemoryPlaceRepository = InMemoryPlaceRepository.getInstance();

        Building building1 = new Building(generateUUID("1xyz"), "214/43");
        building1.setPlace(inMemoryPlaceRepository.getPalazzettoVillage());

        Building building2 = new Building(generateUUID("2xyz"), "214/44");
        building2.setPlace(inMemoryPlaceRepository.getPalazzettoVillage());

        Building building3 = new Building(generateUUID("3xyz"), "214/45");
        building3.setPlace(inMemoryPlaceRepository.getPalazzettoVillage());

        Building buildingPR01 = new Building(generateUUID("PR01xyz"), "214/46");
        buildingPR01.setPlace(inMemoryPlaceRepository.getPalazzettoVillage());

        Building buildingPR02 = new Building(generateUUID("PR02xyz"), "214/47");
        buildingPR02.setPlace(inMemoryPlaceRepository.getPalazzettoVillage());

        Building buildingPR03 = new Building(generateUUID("PR03xyz"), "214/48");
        buildingPR03.setPlace(inMemoryPlaceRepository.getPalazzettoVillage());

        Building buildingPR04 = new Building(generateUUID("PR04xyz"), "214/49");
        buildingPR04.setPlace(inMemoryPlaceRepository.getPalazzettoVillage());

        Building buildingPR05 = new Building(generateUUID("PR05xyz"), "214/50");
        buildingPR05.setPlace(inMemoryPlaceRepository.getPalazzettoVillage());

        Building buildingPR06 = new Building(generateUUID("PR06xyz"), "214/51");
        buildingPR06.setPlace(inMemoryPlaceRepository.getPalazzettoVillage());

        Building buildingPR07 = new Building(generateUUID("PR07xyz"), "214/52");
        buildingPR07.setPlace(inMemoryPlaceRepository.getPalazzettoVillage());

        Building buildingPR08 = new Building(generateUUID("PR08xyz"), "214/53");
        buildingPR08.setPlace(inMemoryPlaceRepository.getPalazzettoVillage());

        Building buildingPR09 = new Building(generateUUID("PR09xyz"), "214/54");
        buildingPR09.setPlace(inMemoryPlaceRepository.getPalazzettoVillage());

        Building buildingPR10 = new Building(generateUUID("PR10xyz"), "214/55");
        buildingPR10.setPlace(inMemoryPlaceRepository.getPalazzettoVillage());

        Building building4 = new Building(generateUUID("1opj"), "ตึก1");
        building4.setPlace(inMemoryPlaceRepository.getBangkokHospital());

        Building building5 = new Building(generateUUID("2opj"), "ตึกพักญาติ");
        building5.setPlace(inMemoryPlaceRepository.getBangkokHospital());


        Building building6 = new Building(generateUUID("3opj"), "โรงอาหาร");
        building6.setPlace(inMemoryPlaceRepository.getBangkokHospital());


        Building building7 = new Building(generateUUID("1hij"), "ศาลาใหญ่");
        building7.setPlace(inMemoryPlaceRepository.getWatpaphukon());

        Building building8 = new Building(generateUUID("2hij"), "เมรุ");
        building8.setPlace(inMemoryPlaceRepository.getWatpaphukon());

        Building building9 = new Building(generateUUID("3hij"), "ลานหน้าศาลากลาง");
        building9.setPlace(inMemoryPlaceRepository.getWatpaphukon());

        buildingMap.put(building1.getId(), building1);
        buildingMap.put(building2.getId(), building2);
        buildingMap.put(building3.getId(), building3);
        buildingMap.put(building4.getId(), building4);
        buildingMap.put(building5.getId(), building5);
        buildingMap.put(building6.getId(), building6);
        buildingMap.put(building7.getId(), building7);
        buildingMap.put(building8.getId(), building8);
        buildingMap.put(building9.getId(), building9);
        buildingMap.put(buildingPR01.getId(), buildingPR01);
        buildingMap.put(buildingPR02.getId(), buildingPR02);
        buildingMap.put(buildingPR03.getId(), buildingPR03);
        buildingMap.put(buildingPR04.getId(), buildingPR04);
        buildingMap.put(buildingPR05.getId(), buildingPR05);
        buildingMap.put(buildingPR06.getId(), buildingPR06);
        buildingMap.put(buildingPR07.getId(), buildingPR07);
        buildingMap.put(buildingPR08.getId(), buildingPR08);
        buildingMap.put(buildingPR09.getId(), buildingPR09);
        buildingMap.put(buildingPR10.getId(), buildingPR10);


    }

    @NonNull
    private UUID generateUUID(String input) {
        return UUID.nameUUIDFromBytes(input.getBytes());
    }

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
    public List<Building> findByPlaceUUIDAndBuildingName(UUID placeUUID, String buildingName) {
        ArrayList<Building> newBuildingList = new ArrayList<>();
        for (Building eachBuilding : buildingMap.values()) {
            if (eachBuilding.getPlace().getId().equals(placeUUID) && eachBuilding.getName().contains(buildingName)) {
                newBuildingList.add(eachBuilding);
            }
        }
        return newBuildingList.isEmpty() ? null : newBuildingList;
    }

    @Override
    public Building findByUUID(UUID buildingUUID) {
        for (Building eachBuilding : buildingMap.values()) {
            if (eachBuilding.getId().equals(buildingUUID)) {
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
