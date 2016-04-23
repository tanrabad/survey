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

package org.tanrabad.survey;


import org.junit.Test;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.Assert.assertEquals;

public class OkHttpTest extends WireMockTestBase {

    private OkHttpClient client = new OkHttpClient();

    @Test
    public void testSuccessResponse() throws Exception {
        stubFor(get(urlEqualTo("/success"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("[]")));
        Request build = new Request.Builder()
                .get()
                .url(HTTP_LOCALHOST_8089 + "/success")
                .build();
        Response res = client.newCall(build).execute();

        assertEquals(200, res.code());
        assertEquals(true, res.isSuccessful());
        assertEquals("[]", res.body().string());
    }

    @Test
    public void testUnmodifiedRequest() throws Exception {
        stubFor(get(urlEqualTo("/unmodified"))
                .withHeader(IF_MODIFIED_SINCE, equalTo("Sat, 29 Oct 1994 19:43:31 GMT"))
                .willReturn(aResponse()
                        .withStatus(304)
                        .withHeader(LAST_UPDATE, "Sat, 29 Oct 1994 19:43:31 GMT")
                        .withHeader(CONTENT_LENGTH, "0")
                        .withBody("")));
        Request build = new Request.Builder()
                .get()
                .url(HTTP_LOCALHOST_8089 + "/unmodified")
                .addHeader(IF_MODIFIED_SINCE, "Sat, 29 Oct 1994 19:43:31 GMT")
                .build();
        Response res = client.newCall(build).execute();

        assertEquals(304, res.code());
        assertEquals(false, res.isSuccessful());
        assertEquals("0", res.header(CONTENT_LENGTH));
        assertEquals("Sat, 29 Oct 1994 19:43:31 GMT", res.header(LAST_UPDATE));
        assertEquals("", res.body().string());
    }

    @Test
    public void testError() throws Exception {
        stubFor(get(urlEqualTo("/error"))
                .willReturn(aResponse()
                        .withStatus(400)
                        .withHeader(CONTENT_LENGTH, "0")
                        .withBody("")));
        Request build = new Request.Builder()
                .get()
                .url(HTTP_LOCALHOST_8089 + "/error")
                .build();
        Response res = client.newCall(build).execute();

        assertEquals(400, res.code());
        assertEquals(false, res.isSuccessful());
        assertEquals("0", res.header(CONTENT_LENGTH));
        assertEquals("", res.body().string());
    }
}
