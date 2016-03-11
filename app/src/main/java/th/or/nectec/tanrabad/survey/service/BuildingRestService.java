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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import th.or.nectec.tanrabad.domain.place.PlaceRepository;
import th.or.nectec.tanrabad.domain.user.UserRepository;
import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.survey.TanrabadApp;
import th.or.nectec.tanrabad.survey.repository.BrokerPlaceRepository;
import th.or.nectec.tanrabad.survey.repository.BrokerUserRepository;
import th.or.nectec.tanrabad.survey.service.json.JsonBuilding;

public class BuildingRestService extends AbsUploadRestService<Building> {

    public static final String PATH = "/building";
    private PlaceRepository placeRepository;
    private UserRepository userRepository;

    public BuildingRestService() {
        this(BASE_API, new ServiceLastUpdatePreference(TanrabadApp.getInstance(), PATH),
                BrokerPlaceRepository.getInstance(),
                BrokerUserRepository.getInstance());
    }

    public BuildingRestService(String apiBaseUrl,
                               ServiceLastUpdate serviceLastUpdate,
                               PlaceRepository placeRepository,
                               UserRepository userRepository) {
        super(apiBaseUrl, serviceLastUpdate);
        this.placeRepository = placeRepository;
        this.userRepository = userRepository;
    }

    @Override
    public String getDefaultParams() {
        return new QueryStringBuilder("geostd=4326", getApiFilterParam()).build();
    }

    @Override
    protected String getPath() {
        return PATH;
    }

    @Override
    protected List<Building> jsonToEntityList(String responseBody) throws IOException {
        ArrayList<Building> buildings = new ArrayList<>();
        List<JsonBuilding> jsonBuildings = LoganSquare.parseList(responseBody, JsonBuilding.class);
        for (JsonBuilding eachJsonBuilding : jsonBuildings) {
            Building building = eachJsonBuilding.getEntity(placeRepository, userRepository);
            if (eachJsonBuilding.active) {
                buildings.add(building);
            } else {
                addDeleteData(building);
            }
        }
        return buildings;
    }

    @Override
    protected String entityToJsonString(Building data) {
        try {
            return LoganSquare.serialize(JsonBuilding.parse(data));
        } catch (IOException io) {
            throw new RestServiceException(io);
        }
    }

    @Override
    protected String getId(Building data) {
        return data.getId().toString();
    }
}
