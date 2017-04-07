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

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.tanrabad.survey.BuildConfig;
import org.tanrabad.survey.R;
import org.tanrabad.survey.TanrabadApp;
import org.tanrabad.survey.entity.User;
import org.tanrabad.survey.presenter.AccountUtils;
import org.tanrabad.survey.utils.http.FileCertificateAuthority;
import org.tanrabad.survey.utils.http.PageLinks;
import org.tanrabad.survey.utils.http.Status;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.X509TrustManager;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.tanrabad.survey.utils.http.Header.ACCEPT;
import static org.tanrabad.survey.utils.http.Header.ACCEPT_CHARSET;
import static org.tanrabad.survey.utils.http.Header.IF_MODIFIED_SINCE;
import static org.tanrabad.survey.utils.http.Header.LAST_MODIFIED;
import static org.tanrabad.survey.utils.http.Header.LINK;
import static org.tanrabad.survey.utils.http.Header.USER_AGENT;

public abstract class AbsRestService<T> implements RestService<T> {

    protected static final String TRB_USER_AGENT = "TanRabad-SURVEY/" + BuildConfig.VERSION_NAME + " (Android)";

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

        client = TanRabadHttpClient.build();
        deletedData = new ArrayList<>();
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
