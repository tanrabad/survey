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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import th.or.nectec.tanrabad.domain.BuildingRepository;
import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.entity.Place;

public class StubBuildingRepository implements BuildingRepository {

    List<Building> buildings = new ArrayList<>();

    public StubBuildingRepository() {
        List<Building> buildings = new ArrayList<>();

        Building building1 = (new Building(UUID.nameUUIDFromBytes("1xyz".getBytes()), "214/43"));
        building1.setPlace(new Place(UUID.nameUUIDFromBytes("1abc".getBytes()), "บางไผ่"));
        buildings.add(building1);

        Building building2 = (new Building(UUID.nameUUIDFromBytes("2xyz".getBytes()), "214/44"));
        building2.setPlace(new Place(UUID.nameUUIDFromBytes("1abc".getBytes()), "บางไผ่"));
        buildings.add(building2);

        Building building3 = (new Building(UUID.nameUUIDFromBytes("3xyz".getBytes()), "214/45"));
        building3.setPlace(new Place(UUID.nameUUIDFromBytes("1abc".getBytes()), "บางไผ่"));
        buildings.add(building3);

        Building building4 = (new Building(UUID.nameUUIDFromBytes("1opj".getBytes()), "45/1"));
        building4.setPlace(new Place(UUID.nameUUIDFromBytes("1bcd".getBytes()), "บางโพธิ์"));
        buildings.add(building4);

        Building building5 = (new Building(UUID.nameUUIDFromBytes("2opj".getBytes()), "45/2"));
        building5.setPlace(new Place(UUID.nameUUIDFromBytes("1bcd".getBytes()), "บางโพธิ์"));
        buildings.add(building5);

        Building building6 = (new Building(UUID.nameUUIDFromBytes("3opj".getBytes()), "45/3"));
        building6.setPlace(new Place(UUID.nameUUIDFromBytes("1bcd".getBytes()), "บางโพธิ์"));
        buildings.add(building6);

        Building building7 = (new Building(UUID.nameUUIDFromBytes("1hij".getBytes()), "4/1"));
        building7.setPlace(new Place(UUID.nameUUIDFromBytes("3def".getBytes()), "บางไทร"));
        buildings.add(building7);

        Building building8 = (new Building(UUID.nameUUIDFromBytes("2hij".getBytes()), "4/2"));
        building8.setPlace(new Place(UUID.nameUUIDFromBytes("3def".getBytes()), "บางไทร"));
        buildings.add(building8);

        Building building9 = (new Building(UUID.nameUUIDFromBytes("3hij".getBytes()), "4/3"));
        building9.setPlace(new Place(UUID.nameUUIDFromBytes("3def".getBytes()), "บางไทร"));
        buildings.add(building9);
    }


    @Override
    public List<Building> findBuildingInPlace(UUID placeUuid) {
        return buildings;
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
}
