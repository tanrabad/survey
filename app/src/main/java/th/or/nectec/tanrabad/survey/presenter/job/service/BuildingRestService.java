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

package th.or.nectec.tanrabad.survey.presenter.job.service;

import com.bluelinelabs.logansquare.LoganSquare;
import com.squareup.okhttp.Request;
import th.or.nectec.tanrabad.domain.UserRepository;
import th.or.nectec.tanrabad.domain.place.PlaceRepository;
import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.survey.presenter.job.service.http.Header;
import th.or.nectec.tanrabad.survey.presenter.job.service.jsonentity.JsonBuilding;
import th.or.nectec.tanrabad.survey.repository.InMemoryPlaceRepository;
import th.or.nectec.tanrabad.survey.repository.StubUserRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BuildingRestService extends BaseRestService<Building> {

    LastUpdate lastUpdate;
    String apiBaseUrl;
    private PlaceRepository placeRepository;
    private UserRepository userRepository;

    public BuildingRestService() {
        this(BASE_API, new LastUpdatePreference(), InMemoryPlaceRepository.getInstance(), new StubUserRepository());
    }

    public BuildingRestService(String apiBaseUrl, LastUpdate lastUpdate, PlaceRepository placeRepository, UserRepository userRepository) {
        this.apiBaseUrl = apiBaseUrl;
        this.lastUpdate = lastUpdate;
        this.placeRepository = placeRepository;
        this.userRepository = userRepository;
    }

    protected Request makeRequest() {
        return new Request.Builder()
                .get()
                .url(apiBaseUrl + "/building")
                .header(Header.IF_MODIFIED_SINCE, lastUpdate.get().toDateTimeISO().toString())
                .build();
    }

    protected String buildingUrl() {
        return apiBaseUrl + getPath();
    }

    protected List<Building> toJson(String responseBody) {
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

    protected String getPath() {
        return "/building";
    }
}
