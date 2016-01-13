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

package th.or.nectec.tanrabad.survey.service;

import android.support.annotation.NonNull;
import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.survey.repository.InMemoryPlaceRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StubBuildingRestService implements RestService<Building> {

    private static List<Building> buildings = new ArrayList<>();

    static {
        InMemoryPlaceRepository inMemoryPlaceRepository = InMemoryPlaceRepository.getInstance();

        Building building1 = new Building(UUID.randomUUID(), "213/11");
        building1.setPlace(inMemoryPlaceRepository.getPalazzettoVillage());
        buildings.add(building1);

        Building building2 = new Building(UUID.randomUUID(), "213/12");
        building2.setPlace(inMemoryPlaceRepository.getPalazzettoVillage());
        buildings.add(building2);

        Building building3 = new Building(generateUUID("1xyz"), "214/43");
        building3.setPlace(inMemoryPlaceRepository.getBangkokHospital());
        buildings.add(building3);
    }

    @NonNull
    private static UUID generateUUID(String input) {
        return UUID.nameUUIDFromBytes(input.getBytes());
    }

    @Override
    public List<Building> getUpdate() {
        return buildings;
    }
}
