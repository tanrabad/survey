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

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import th.or.nectec.tanrabad.entity.Building;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BuildingRestService implements RestService<Building> {

    public static final String BASE_API = "http://tanrabad.igridproject.info/v1";
    private final OkHttpClient client = new OkHttpClient();

    LastUpdate lastUpdate;
    String apiBaseUrl;

    public BuildingRestService(){
        this(BASE_API, new LastUpdatePreference());
    }

    public BuildingRestService(String apiBaseUrl, LastUpdate lastUpdate) {
        this.apiBaseUrl = apiBaseUrl;
        this.lastUpdate = lastUpdate;
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
        return new ArrayList<>();
    }

    public String buildingUrl() {
        return apiBaseUrl + getPath();
    }

    private String getPath() {
        return "/building";
    }

}
