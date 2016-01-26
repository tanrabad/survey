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
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

public abstract class AbsUploadRestService<T> extends BaseRestService implements UploadRestService<T> {

    public AbsUploadRestService(String baseApi, LastUpdate lastUpdate) {
        super(baseApi, lastUpdate);
    }

    @Override
    public boolean postData(T data) {
        String postString = entityToJsonString(data);
        Request.Builder postBuilder = new Request.Builder()
                .post(RequestBody.create(MediaType.parse("application/json"), postString))
                .addHeader("User-Agent", "tanrabad-survey-app")
                .url(baseApi + getPath());
        Request request = postBuilder.build();
        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 400) {
                ErrorResponse errorResponse = LoganSquare.parse(response.body().string(), ErrorResponse.class);
                throw new ErrorResponseException(errorResponse);
            }
            if (isNotSuccess(response))
                throw new RestServiceException();
            return response.code() == 201;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    protected abstract String entityToJsonString(T data);

}
