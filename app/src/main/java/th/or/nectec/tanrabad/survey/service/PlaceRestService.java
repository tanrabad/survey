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
import th.or.nectec.tanrabad.domain.place.PlaceSubTypeRepository;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.survey.TanrabadApp;
import th.or.nectec.tanrabad.survey.repository.BrokerPlaceSubTypeRepository;
import th.or.nectec.tanrabad.survey.repository.StubUserRepository;
import th.or.nectec.tanrabad.survey.service.json.JsonPlace;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlaceRestService extends AbsUploadRestService<Place> {

    public static final String PATH = "/place";
    private UserRepository userRepository;
    private PlaceSubTypeRepository placeSubTypeRepository;

    public PlaceRestService() {
        this(BASE_API, new ServiceLastUpdatePreference(TanrabadApp.getInstance(), PATH),
                new StubUserRepository(), BrokerPlaceSubTypeRepository.getInstance());
    }

    public PlaceRestService(String apiBaseUrl, ServiceLastUpdate serviceLastUpdate, UserRepository userRepository,
                            PlaceSubTypeRepository placeSubTypeRepository) {
        super(apiBaseUrl, serviceLastUpdate);
        this.userRepository = userRepository;
        this.placeSubTypeRepository = placeSubTypeRepository;
    }

    @Override
    public String getDefaultParams() {
        return "geostd=4326&" + getHealthRegionCodeParam();
    }

    @Override
    protected String getPath() {
        return PATH;
    }

    @Override
    protected List<Place> jsonToEntityList(String responseBody) throws IOException {
        ArrayList<Place> places = new ArrayList<>();
        List<JsonPlace> jsonPlaces = LoganSquare.parseList(responseBody, JsonPlace.class);
        for (JsonPlace eachJsonPlace : jsonPlaces)
            places.add(eachJsonPlace.getEntity(placeSubTypeRepository));
        return places;
    }

    @Override
    protected String entityToJsonString(Place data) {
        try {
            return LoganSquare.serialize(JsonPlace.parse(data));
        } catch (IOException io) {
            throw new RestServiceException(io);
        }
    }

    @Override
    protected String getId(Place data) {
        return data.getId().toString();
    }
}