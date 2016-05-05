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

package org.tanrabad.survey.service;

import com.bluelinelabs.logansquare.LoganSquare;
import org.tanrabad.survey.TanrabadApp;
import org.tanrabad.survey.entity.lookup.PlaceType;
import org.tanrabad.survey.service.json.JsonPlaceType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlaceTypeRestService extends AbsRestService<PlaceType> {

    private static final String PATH = "/placetype";

    public PlaceTypeRestService() {
        this(ImpRestServiceConfig.getInstance().getApiBaseUrl(),
                new ApiSyncInfoPreference(TanrabadApp.getInstance(), PATH));
    }

    private PlaceTypeRestService(String apiBaseUrl, ServiceLastUpdate serviceLastUpdate) {
        super(apiBaseUrl, serviceLastUpdate);
    }

    @Override
    protected String getPath() {
        return PATH;
    }

    @Override
    protected List<PlaceType> jsonToEntityList(String responseBody) throws IOException {
        ArrayList<PlaceType> placeTypes = new ArrayList<>();
        List<JsonPlaceType> jsonPlaceTypes = LoganSquare.parseList(responseBody, JsonPlaceType.class);
        for (JsonPlaceType jsonPlaceType : jsonPlaceTypes)
            placeTypes.add(jsonPlaceType.getEntity());
        return placeTypes;
    }
}
