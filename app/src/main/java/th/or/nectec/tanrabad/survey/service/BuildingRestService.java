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

import okhttp3.Request;
import okhttp3.Response;
import th.or.nectec.tanrabad.domain.place.PlaceRepository;
import th.or.nectec.tanrabad.domain.user.UserRepository;
import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.survey.TanrabadApp;
import th.or.nectec.tanrabad.survey.repository.BrokerPlaceRepository;
import th.or.nectec.tanrabad.survey.repository.BrokerUserRepository;
import th.or.nectec.tanrabad.survey.service.json.JsonBuilding;

import static th.or.nectec.tanrabad.survey.service.http.Header.*;

public class BuildingRestService extends AbsUploadRestService<Building> implements DeleteRestService<Building> {

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
    protected String getPath() {
        return PATH;
    }

    @Override
    public String getDefaultParams() {
        return new QueryStringBuilder("geostd=4326", getApiFilterParam()).build();
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

    @Override
    public boolean delete(Building data) throws IOException {
        Request request = deleteRequest(data);
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            TanrabadApp.log(response.body().string());
            throw new RestServiceException(response);
        }
        return true;
    }

    protected final Request deleteRequest(Building data) {
        Request.Builder requestBuilder = new Request.Builder()
                .delete()
                .url(getUrl().replace(getDefaultParams(), "").concat("/").concat(data.getId().toString()))
                .addHeader(USER_AGENT, TRB_USER_AGENT)
                .addHeader(ACCEPT, "application/json")
                .addHeader(ACCEPT_CHARSET, "utf-8");
        return requestBuilder.build();
    }
}
