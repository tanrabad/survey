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
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import th.or.nectec.tanrabad.domain.UserRepository;
import th.or.nectec.tanrabad.domain.place.PlaceRepository;
import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.survey.presenter.job.service.jsonentity.JsonBuilding;
import th.or.nectec.tanrabad.survey.repository.InMemoryPlaceRepository;
import th.or.nectec.tanrabad.survey.repository.StubUserRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BuildingRestService implements RestService<Building> {

    public static final String BASE_API = "http://tanrabad.igridproject.info/v1";
    private final OkHttpClient client = new OkHttpClient();

    LastUpdate lastUpdate;
    private PlaceRepository placeRepository;
    private UserRepository userRepository;
    String apiBaseUrl;

    public BuildingRestService(){
        this(BASE_API, new LastUpdatePreference(), InMemoryPlaceRepository.getInstance(), new StubUserRepository());
    }

    public BuildingRestService(String apiBaseUrl, LastUpdate lastUpdate, PlaceRepository placeRepository, UserRepository userRepository) {
        this.apiBaseUrl = apiBaseUrl;
        this.lastUpdate = lastUpdate;
        this.placeRepository = placeRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Building> getUpdate() {
        try {
            Request request = makeRequest();
            Response response = client.newCall(request).execute();

            if (isNotModified(response))
                return new ArrayList<>();
            if (isNotSuccess(response))
                throw new RestServiceException();

            return toJson(response.body().string());

        } catch (IOException io) {
            throw new RestServiceException();
        }
    }

    private Request makeRequest() {
        return new Request.Builder()
                .get()
                .url(buildingUrl())
                .header(Header.IF_MODIFIED_SINCE, lastUpdate.get().toDateTimeISO().toString())
                .build();
    }

    private boolean isNotModified(Response response) {
        return response.code() == Status.NOT_MODIFIED;
    }

    private boolean isNotSuccess(Response response) {
        return !response.isSuccessful();
    }

    private List<Building> toJson(String responseBody) {
        ArrayList<Building> buildings = new ArrayList<>();
        try {
            List<JsonBuilding> jsonBuildings = LoganSquare.parseList(responseBody, JsonBuilding.class);
            for(JsonBuilding eachJsonBuilding : jsonBuildings){
                buildings.add(eachJsonBuilding.getEntity(placeRepository, userRepository));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buildings;
    }

    public String buildingUrl() {
        return apiBaseUrl + getPath();
    }

    private String getPath() {
        return "/building";
    }

}
