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
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import th.or.nectec.tanrabad.survey.service.http.Header;
import th.or.nectec.tanrabad.survey.service.http.Status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseRestService<T> implements RestService<T> {

    public static final String BASE_API = "http://tanrabad.igridproject.info/v1";
    protected static final DateTimeFormatter RFC1123_FORMATTER =
            DateTimeFormat.forPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'");
    private final OkHttpClient client = new OkHttpClient();
    protected LastUpdate lastUpdate;
    protected String baseApi;
    private String nextUrl = "";

    public BaseRestService(String baseApi, LastUpdate lastUpdate) {
        this.baseApi = baseApi;
        this.lastUpdate = lastUpdate;
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
                throw new RestServiceException();
            if (!hasNextRequest())
                lastUpdate.save(getLastModified(response));

            return jsonToEntityList(response.body().string());

        } catch (IOException io) {
            throw new RestServiceException();
        }
    }

    @Override
    public boolean hasNextRequest() {
        return nextUrl != null;
    }

    private void getNextRequest(Response response) {
        String linkHeader = response.headers().get("Link");
        if (linkHeader != null && !linkHeader.isEmpty()) {
            PageLinks pageLinks = new PageLinks(linkHeader);
            nextUrl = pageLinks.getNext().replace(baseApi + getPath(), "");
        } else {
            nextUrl = null;
        }
    }

    private DateTime getLastModified(Response response) {
        return RFC1123_FORMATTER.parseDateTime(response.header(Header.LAST_MODIFIED));
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
                .url(baseApi + getPath() + nextUrl);
        headerIfModifiedSince(requestBuilder);
        return requestBuilder.build();
    }

    private void headerIfModifiedSince(Request.Builder requestBuilder) {
        DateTime lastUpdate = this.lastUpdate.get();
        if (lastUpdate != null)
            requestBuilder.addHeader(Header.IF_MODIFIED_SINCE, RFC1123_FORMATTER.print(lastUpdate));
    }

    protected abstract String getPath();

    protected abstract List<T> jsonToEntityList(String responseBody);
}
