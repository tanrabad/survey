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
import th.or.nectec.tanrabad.entity.Subdistrict;
import th.or.nectec.tanrabad.survey.TanrabadApp;
import th.or.nectec.tanrabad.survey.service.json.JsonTambon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TambonRestService extends BaseRestService<Subdistrict> {

    public static final String PATH = "/tambon?geostd=4326";

    public TambonRestService() {
        this(BASE_API, new LastUpdatePreference(TanrabadApp.getInstance(), PATH));
    }

    public TambonRestService(String apiBaseUrl, LastUpdate lastUpdate) {
        super(apiBaseUrl, lastUpdate);
    }

    protected List<Subdistrict> jsonToEntityList(String responseBody) {
        ArrayList<Subdistrict> subdistrictList = new ArrayList<>();
        try {
            List<JsonTambon> jsonTambonList = LoganSquare.parseList(responseBody, JsonTambon.class);
            for (JsonTambon eachJsonTambon : jsonTambonList) {
                subdistrictList.add(eachJsonTambon.getEntity());
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
