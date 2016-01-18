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
import th.or.nectec.tanrabad.entity.District;
import th.or.nectec.tanrabad.survey.TanrabadApp;
import th.or.nectec.tanrabad.survey.service.json.JsonAmphur;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AmphurRestService extends BaseRestService<District> {

    public static final String PATH = "/amphur?geostd=4326";

    public AmphurRestService() {
        this(BASE_API, new LastUpdatePreference(TanrabadApp.getInstance(), PATH ));
    }

    public AmphurRestService(String apiBaseUrl, LastUpdate lastUpdate) {
        super(apiBaseUrl, lastUpdate);
    }

    protected List<District> jsonToEntityList(String responseBody) {
        ArrayList<District> subdistrictList = new ArrayList<>();
        try {
            List<JsonAmphur> jsonAmphurList = LoganSquare.parseList(responseBody, JsonAmphur.class);
            for (JsonAmphur eachJsonAmphur : jsonAmphurList) {
                subdistrictList.add(eachJsonAmphur.getEntity());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return subdistrictList;
    }

    protected String getPath() {
        return PATH;
    }
}
