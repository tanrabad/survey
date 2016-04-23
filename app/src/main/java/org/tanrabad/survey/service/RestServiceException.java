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

import com.bluelinelabs.logansquare.LoganSquare;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class RestServiceException extends RuntimeException {

    public RestServiceException(Response response) {
        super("request not success [" + response.code() + "] toString=" + response.toString());
    }

    public RestServiceException(Request request, Response response) {
        super("request not success [" + response.code() + "]"
                + request.toString()
                + " Request Body = " + request.body()
                + response.toString());
    }

    public RestServiceException(Throwable throwable) {
        super(throwable);
    }

    public static class ErrorResponseException extends RestServiceException {
        private ErrorResponse errorResponse;

        public ErrorResponseException(Response response) throws IOException {
            super(response);
            errorResponse = LoganSquare.parse(response.body().string(), ErrorResponse.class);
        }

        public ErrorResponseException(Request request, Response response) {
            super(request, response);
        }

        public ErrorResponse getErrorResponse() {
            return errorResponse;
        }
    }
}
