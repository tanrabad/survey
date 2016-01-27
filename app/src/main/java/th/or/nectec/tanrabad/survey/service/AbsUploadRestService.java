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

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import th.or.nectec.tanrabad.survey.service.http.Header;
import th.or.nectec.tanrabad.survey.service.http.Status;

import java.io.IOException;

import static th.or.nectec.tanrabad.survey.service.http.Header.USER_AGENT;

public abstract class AbsUploadRestService <T> extends AbsRestService implements UploadRestService<T> {

    public static final String TANRABAD_SURVEY_APP = "tanrabad-survey-app";
    public static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json");

    public AbsUploadRestService(String baseApi, ServiceLastUpdate serviceLastUpdate) {
        super(baseApi, serviceLastUpdate);
    }

    @Override
    public boolean postData(T data) {
        try {
            Request request = getPostRequest(data);
            Response response = client.newCall(request).execute();
            if (response.code() == Status.BAD_REQUEST)
                throw new RestServiceException.ErrorResponseException(response);
            if (isNotSuccess(response))
                throw new RestServiceException(response);
            return response.code() == Status.CREATED;
        } catch (IOException io) {
            throw new RestServiceException(io);
        }
    }

    private Request getPostRequest(T data) {
        return new Request.Builder()
                .post(RequestBody.create(JSON_MEDIA_TYPE, entityToJsonString(data)))
                .addHeader(USER_AGENT, TANRABAD_SURVEY_APP)
                .url(baseApi + getPath())
                .build();
    }

    protected abstract String entityToJsonString(T data);


    @Override
    public boolean put(String dataId, T data) {
        try {
            Request request = makePostRequest(dataId, data);
            Response response = client.newCall(request).execute();
            return response.isSuccessful();
        } catch (IOException io) {
            throw new RestServiceException(io);
        }
    }

    private Request makePostRequest(String dataId, T data) {
        return new Request.Builder().put(RequestBody.create(JSON_MEDIA_TYPE, entityToJsonString(data)))
                .addHeader(USER_AGENT, TANRABAD_SURVEY_APP)
                .url(baseApi + getPath() + "/" + dataId)
                .build();
    }
}
