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
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.survey.TanrabadApp;
import th.or.nectec.tanrabad.survey.repository.StubUserRepository;
import th.or.nectec.tanrabad.survey.service.json.JsonPlace;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlaceRestService extends AbsUploadRestService<Place> {

    public static final String PATH = "/place?geostd=4326&hr_code=dpc-04";
    private UserRepository userRepository;

    public PlaceRestService() {
        this(BASE_API, new LastUpdatePreference(TanrabadApp.getInstance(), PATH), new StubUserRepository());
    }

    public PlaceRestService(String apiBaseUrl, LastUpdate lastUpdate, UserRepository userRepository) {
        super(apiBaseUrl, lastUpdate);
        this.userRepository = userRepository;
    }

    @Override
    protected String getPath() {
        return PATH;
    }

    @Override
    protected List<Place> jsonToEntityList(String responseBody) {
        ArrayList<Place> places = new ArrayList<>();
        try {
            List<JsonPlace> jsonPlaces = LoganSquare.parseList(responseBody, JsonPlace.class);
            for (JsonPlace eachJsonPlace : jsonPlaces) {
                places.add(eachJsonPlace.getEntity(userRepository));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return places;
    }

    @Override
    protected String entityToJsonString(Place data) {
        try {
            return LoganSquare.serialize(JsonPlace.parse(data));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RestServiceException();
        }
    }
}