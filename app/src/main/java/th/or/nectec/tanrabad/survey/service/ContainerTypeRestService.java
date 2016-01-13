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
import com.squareup.okhttp.Request;
import th.or.nectec.tanrabad.entity.ContainerType;
import th.or.nectec.tanrabad.survey.TanrabadApp;
import th.or.nectec.tanrabad.survey.service.http.Header;
import th.or.nectec.tanrabad.survey.service.jsonentity.JsonContainerType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ContainerTypeRestService extends BaseRestService<ContainerType> {

    public static final String PATH = "/containertype";
    LastUpdate lastUpdate;

    public ContainerTypeRestService() {
        this(BASE_API, new LastUpdatePreference(TanrabadApp.getInstance(), PATH));
    }

    public ContainerTypeRestService(String apiBaseUrl, LastUpdate lastUpdate) {
        super(apiBaseUrl, lastUpdate);
    }

    @Override
    protected Request makeRequest() {
        return new Request.Builder()
                .get()
                .url(provinceUrl())
                .header(Header.IF_MODIFIED_SINCE, lastUpdate.get().toDateTimeISO().toString())
                .build();
    }

    public String provinceUrl() {
        return baseApi + getPath();
    }

    @Override
    protected List<ContainerType> toJson(String responseBody) {
        ArrayList<ContainerType> provinceList = new ArrayList<>();
        try {
            List<JsonContainerType> jsonContainerTypeList = LoganSquare.parseList(responseBody, JsonContainerType.class);
            for (JsonContainerType eachJsonContainerType : jsonContainerTypeList) {
                provinceList.add(eachJsonContainerType.getEntity());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return provinceList;
    }

    @Override
    protected String getPath() {
        return PATH;
    }
}
