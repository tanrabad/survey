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

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import th.or.nectec.tanrabad.survey.TanrabadApp;
import th.or.nectec.tanrabad.survey.service.http.Status;

import java.io.IOException;

import static th.or.nectec.tanrabad.survey.service.http.Header.USER_AGENT;

public abstract class AbsUploadRestService<T> extends AbsRestService<T> implements UploadRestService<T> {

    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json");

    public AbsUploadRestService(String baseApi, ServiceLastUpdate serviceLastUpdate) {
        super(baseApi, serviceLastUpdate);
    }

    @Override
    public boolean post(T data) throws IOException {
        Request request = getPostRequest(data);
        Response response = client.newCall(request).execute();
        if (response.code() == Status.BAD_REQUEST) {
            TanrabadApp.log(entityToJsonString(data));
            throw new RestServiceException.ErrorResponseException(request, response);
        }
        if (isNotSuccess(response)) {
            TanrabadApp.log(entityToJsonString(data));
            TanrabadApp.log(response.body().string());
            throw new RestServiceException(response);
        }
        return response.code() == Status.CREATED;
    }


    private Request getPostRequest(T data) throws IOException {
        return new Request.Builder()
                .post(RequestBody.create(JSON_MEDIA_TYPE, entityToJsonString(data)))
                .addHeader(USER_AGENT, TRB_USER_AGENT)
                .url(baseApi + getPath())
                .build();
    }

    protected abstract String entityToJsonString(T data) throws IOException;

    @Override
    public boolean put(T data) throws IOException {
        Request request = buildPutRequest(data);
        Response response = client.newCall(request).execute();
        if (response.code() == Status.BAD_REQUEST) {
            TanrabadApp.log(entityToJsonString(data));
            throw new RestServiceException.ErrorResponseException(request, response);
        }
        if (isNotSuccess(response)) {
            TanrabadApp.log(entityToJsonString(data));
            TanrabadApp.log(response.body().string());
            throw new RestServiceException(response);
        }
        return true;
    }

    private Request buildPutRequest(T data) throws IOException {
        return new Request.Builder().put(RequestBody.create(JSON_MEDIA_TYPE, entityToJsonString(data)))
                .addHeader(USER_AGENT, TRB_USER_AGENT)
                .url(baseApi + getPath() + "/" + getId(data))
                .build();
    }

    protected abstract String getId(T data);
}
