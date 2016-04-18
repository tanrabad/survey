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

import android.text.TextUtils;
import android.util.Patterns;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import th.or.nectec.tanrabad.entity.User;
import th.or.nectec.tanrabad.survey.BuildConfig;
import th.or.nectec.tanrabad.survey.presenter.AccountUtils;
import th.or.nectec.tanrabad.survey.service.http.Status;

import static java.util.concurrent.TimeUnit.SECONDS;
import static th.or.nectec.tanrabad.survey.service.http.Header.*;

public abstract class AbsRestService<T> implements RestService<T> {

    protected static final String TRB_USER_AGENT = "tanrabad-survey-app";

    private static final int READ_WRITE_TIMEOUT = 10; //second
    private static final int CONNECT_TIMEOUT = 5; //second
    protected static String BASE_API = BuildConfig.API_URL;
    protected final OkHttpClient client;
    protected String baseApi;
    private ServiceLastUpdate serviceLastUpdate;
    private User user;
    private String nextUrl = null;
    private List<T> deletedData;

    AbsRestService(String baseApi, ServiceLastUpdate serviceLastUpdate) {
        this(baseApi, serviceLastUpdate, AccountUtils.getUser());
    }

    private AbsRestService(String baseApi, ServiceLastUpdate serviceLastUpdate, User user) {
        this.serviceLastUpdate = serviceLastUpdate;
        this.user = user;
        this.baseApi = baseApi;

        client = new OkHttpClient.Builder()
                .readTimeout(READ_WRITE_TIMEOUT, SECONDS)
                .writeTimeout(READ_WRITE_TIMEOUT, SECONDS)
                .connectTimeout(CONNECT_TIMEOUT, SECONDS)
                .build();
        deletedData = new ArrayList<>();
    }

    public static String getBaseApi() {
        return BASE_API;
    }

    public static void setBaseApi(String baseApi) {
        if (TextUtils.isEmpty(baseApi))
            return;

        if (!Patterns.WEB_URL.matcher(baseApi).matches())
            return;

        BASE_API = baseApi;
    }

    String getApiFilterParam() {
        return user.getApiFilter();
    }

    @Override
    public List<T> getUpdate() throws IOException {
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
    }

    @Override
    public List<T> getDelete() {
        return deletedData;
    }

    @Override
    public boolean hasNextRequest() {
        return nextUrl != null;
    }

    protected final Request makeRequest() {
        Request.Builder requestBuilder = new Request.Builder()
                .get()
                .url(getUrl())
                .addHeader(USER_AGENT, TRB_USER_AGENT)
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
        if (getQueryString() != null)
            url += getQueryString();
        return url;
    }

    protected abstract String getPath();

    String getQueryString() {
        return null;
    }

    private void headerIfModifiedSince(Request.Builder requestBuilder) {
        String lastUpdate = this.serviceLastUpdate.get();
        if (lastUpdate != null)
            requestBuilder.addHeader(IF_MODIFIED_SINCE, lastUpdate);
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

    private boolean isNotModified(Response response) {
        return response.code() == Status.NOT_MODIFIED;
    }

    boolean isNotSuccess(Response response) {
        return !response.isSuccessful();
    }

    protected abstract List<T> jsonToEntityList(String responseBody) throws IOException;

    public User getUser() {
        return user;
    }

    public void addDeleteData(T data) {
        deletedData.add(data);
    }
}
