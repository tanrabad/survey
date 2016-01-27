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
import th.or.nectec.tanrabad.entity.lookup.PlaceSubType;
import th.or.nectec.tanrabad.survey.TanrabadApp;
import th.or.nectec.tanrabad.survey.service.json.JsonPlaceSubType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlaceSubTypeRestService extends AbsRestService<PlaceSubType> {

    public static final String PATH = "/placesubtype";

    public PlaceSubTypeRestService() {
        this(BASE_API, new ServiceLastUpdatePreference(TanrabadApp.getInstance(), PATH));
    }

    public PlaceSubTypeRestService(String apiBaseUrl, ServiceLastUpdate serviceLastUpdate) {
        super(apiBaseUrl, serviceLastUpdate);
    }

    @Override
    protected String getPath() {
        return PATH;
    }

    @Override
    protected List<PlaceSubType> jsonToEntityList(String responseBody) {
        ArrayList<PlaceSubType> placeTypes = new ArrayList<>();
        try {
            List<JsonPlaceSubType> jsonPlaceSubTypeList = LoganSquare.parseList(responseBody, JsonPlaceSubType.class);
            for (JsonPlaceSubType jsonPlaceSubType : jsonPlaceSubTypeList) {
                placeTypes.add(jsonPlaceSubType.getEntity());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return placeTypes;
    }
}
