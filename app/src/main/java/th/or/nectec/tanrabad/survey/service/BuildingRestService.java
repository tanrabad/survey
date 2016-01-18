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

import com.bluelinelabs.logansquare.LoganSquare;
import th.or.nectec.tanrabad.domain.UserRepository;
import th.or.nectec.tanrabad.domain.place.PlaceRepository;
import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.survey.TanrabadApp;
import th.or.nectec.tanrabad.survey.repository.InMemoryPlaceRepository;
import th.or.nectec.tanrabad.survey.repository.StubUserRepository;
import th.or.nectec.tanrabad.survey.service.json.JsonBuilding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BuildingRestService extends BaseRestService<Building> {

    public static final String PATH = "/building?geostd=4326";
    private PlaceRepository placeRepository;
    private UserRepository userRepository;

    public BuildingRestService() {
        this(BASE_API, new LastUpdatePreference(TanrabadApp.getInstance(), PATH), InMemoryPlaceRepository.getInstance(), new StubUserRepository());
    }

    public BuildingRestService(String apiBaseUrl, LastUpdate lastUpdate, PlaceRepository placeRepository, UserRepository userRepository) {
        super(apiBaseUrl, lastUpdate);
        this.placeRepository = placeRepository;
        this.userRepository = userRepository;
    }

    @Override
    protected List<Building> jsonToEntityList(String responseBody) {
        ArrayList<Building> buildings = new ArrayList<>();
        try {
            List<JsonBuilding> jsonBuildings = LoganSquare.parseList(responseBody, JsonBuilding.class);
            for (JsonBuilding eachJsonBuilding : jsonBuildings) {
                buildings.add(eachJsonBuilding.getEntity(placeRepository, userRepository));
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
        return buildings;
    }

    @Override
    protected String getPath() {
        return PATH;
    }
}
