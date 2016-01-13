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
import th.or.nectec.tanrabad.entity.Subdistrict;
import th.or.nectec.tanrabad.survey.presenter.job.service.http.Header;
import th.or.nectec.tanrabad.survey.presenter.job.service.jsonentity.JsonTambon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TambonRestService extends BaseRestService<Subdistrict> {

    LastUpdate lastUpdate;

    public TambonRestService() {
        this(BASE_API, new LastUpdatePreference());
    }

    public TambonRestService(String apiBaseUrl, LastUpdate lastUpdate) {
        this.apiBaseUrl = apiBaseUrl;
        this.lastUpdate = lastUpdate;
    }

    protected Request makeRequest() {
        return new Request.Builder()
                .get()
                .url(tambonUrl())
                .header(Header.IF_MODIFIED_SINCE, lastUpdate.get().toDateTimeISO().toString())
                .build();
    }

    public String tambonUrl() {
        return apiBaseUrl + getPath();
    }

    protected List<Subdistrict> toJson(String responseBody) {
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
        return "/tambon";
    }
}
