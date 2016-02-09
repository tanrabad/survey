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

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import th.or.nectec.tanrabad.entity.User;
import th.or.nectec.tanrabad.survey.presenter.AccountUtils;
import th.or.nectec.tanrabad.survey.BuildConfig;
import th.or.nectec.tanrabad.survey.service.http.Status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static th.or.nectec.tanrabad.survey.service.http.Header.*;

public abstract class AbsRestService <T> implements RestService<T> {

    public static final String BASE_API = BuildConfig.API_URL;
    protected final OkHttpClient client = new OkHttpClient();
    protected ServiceLastUpdate serviceLastUpdate;
    protected String baseApi;
    private User user;
    private String nextUrl = null;

    public AbsRestService(String baseApi, ServiceLastUpdate serviceLastUpdate) {
        this(baseApi, serviceLastUpdate, AccountUtils.getUser());
    }

    public AbsRestService(String baseApi, ServiceLastUpdate serviceLastUpdate, User user) {
        this.baseApi = baseApi;
        this.serviceLastUpdate = serviceLastUpdate;
        this.user = user;
    }

    protected String getHealthRegionCodeParam() {
        return "hr_code=" + user.getHealthRegionCode();
    }

    @Override
    public List<T> getUpdate() {
        try {
            Request request = makeRequest();
            Response response = client.newCall(request).execute();
            getNextRequest(response);

            if (isNotModified(response))
                return new ArrayList<>();
            if (isNotSuccess(response))
                throw new RestServiceException(response);
            if (!hasNextRequest())
                serviceLastUpdate.save(response.header(LAST_MODIFIED));

            return jsonToEntityList(response.body().string());
        } catch (IOException io) {
            io.printStackTrace();
            throw new RestServiceException(io);
        }
    }

    @Override
    public boolean hasNextRequest() {
        return nextUrl != null;
    }

    private void getNextRequest(Response response) {
        String linkHeader = response.header(LINK);
        if (linkHeader != null && !linkHeader.isEmpty()) {
            PageLinks pageLinks = new PageLinks(linkHeader);
            nextUrl = pageLinks.getNext();
        } else {
            nextUrl = null;
        }
    }

    public boolean isNotSuccess(Response response) {
        return !response.isSuccessful();
    }

    public boolean isNotModified(Response response) {
        return response.code() == Status.NOT_MODIFIED;
    }

    protected final Request makeRequest() {
        Request.Builder requestBuilder = new Request.Builder()
                .get()
                .url(getUrl())
                .addHeader(ACCEPT, "application/json")
                .addHeader(ACCEPT_CHARSET, "utf-8");
        headerIfModifiedSince(requestBuilder);
        return requestBuilder.build();
    }

    public String getUrl() {
        if (nextUrl != null && !nextUrl.isEmpty()) {
            return nextUrl;
        }
        String url = baseApi + getPath();
        if (getDefaultParams() != null)
            url += "?" + getDefaultParams();
        return url;
    }

    public String getDefaultParams() {
        return null;
    }

    protected abstract String getPath();

    private void headerIfModifiedSince(Request.Builder requestBuilder) {
        String lastUpdate = this.serviceLastUpdate.get();
        if (lastUpdate != null)
            requestBuilder.addHeader(IF_MODIFIED_SINCE, lastUpdate);
    }

    protected abstract List<T> jsonToEntityList(String responseBody) throws IOException;
}
