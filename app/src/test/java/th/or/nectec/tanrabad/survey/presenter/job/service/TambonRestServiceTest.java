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

import org.junit.Ignore;
import org.junit.Test;
import th.or.nectec.tanrabad.entity.Subdistrict;
import th.or.nectec.tanrabad.survey.WireMockTestBase;
import th.or.nectec.tanrabad.survey.utils.ResourceFile;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;


public class TambonRestServiceTest extends WireMockTestBase {

    public static final String TAMBON = "/tambon";

    @Test
    @Ignore
    public void testRequest() throws Exception {
        TambonRestService restService = new TambonRestService();

        assertEquals(0, restService.getUpdate().size());
    }

    @Test(expected = RestServiceException.class)
    public void test404Response() throws Exception {
        stubFor(get(urlEqualTo(TAMBON))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withBody("")));

        TambonRestService restService = new TambonRestService(
                localHost(),
                new LastUpdatePreference());
        restService.getUpdate();
    }

    @Test
    public void testNotModifiedResponse() throws Exception {
        stubFor(get(urlEqualTo(TAMBON))
                .willReturn(aResponse()
                        .withStatus(304)
                        .withBody("")));

        TambonRestService restService = new TambonRestService(
                localHost(),
                new LastUpdatePreference());
        List<Subdistrict> buildings = restService.getUpdate();
        assertEquals(0, buildings.size());
    }

    @Test
    public void testSuccessResponse() throws Exception {
        stubFor(get(urlEqualTo(TAMBON))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(ResourceFile.read("tambonList10Item.json"))));
        TambonRestService restService = new TambonRestService(
                localHost(),
                new LastUpdatePreference());
        List<Subdistrict> subdistrictList = restService.getUpdate();
        Subdistrict subdistrict = subdistrictList.get(0);

        assertEquals("840212", subdistrict.getCode());
        assertEquals("ทุ่งรัง", subdistrict.getName());
        assertEquals("8402", subdistrict.getDistrictCode());
        assertEquals(true, subdistrict.getBoundary().get(0) != null);
    }

    @Test
    public void testSuccessResponseMultipleItem() throws Exception {
        stubFor(get(urlEqualTo(TAMBON))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(ResourceFile.read("tambonList10Item.json"))));
        TambonRestService restService = new TambonRestService(
                localHost(),
                new LastUpdatePreference());
        List<Subdistrict> subdistrictList = restService.getUpdate();

        Subdistrict subdistrictHead = subdistrictList.get(0);
        assertEquals("840212", subdistrictHead.getCode());
        assertEquals("ทุ่งรัง", subdistrictHead.getName());
        assertEquals("8402", subdistrictHead.getDistrictCode());
        assertEquals(true, subdistrictHead.getBoundary().get(0) != null);

        Subdistrict subdistrictMiddle = subdistrictList.get(4);
        assertEquals("130204", subdistrictMiddle.getCode());
        assertEquals("คลองสี่", subdistrictMiddle.getName());
        assertEquals("1302", subdistrictMiddle.getDistrictCode());
        assertEquals(true, subdistrictMiddle.getBoundary().get(0) != null);

        Subdistrict subdistrictTail = subdistrictList.get(9);
        assertEquals("460706", subdistrictTail.getCode());
        assertEquals("อิตื้อ", subdistrictTail.getName());
        assertEquals("4607", subdistrictTail.getDistrictCode());
        assertEquals(true, subdistrictTail.getBoundary().get(0) != null);
    }

    @Test
    public void testGetUrl() throws Exception {
        TambonRestService restService = new TambonRestService(
                localHost(),
                new LastUpdatePreference());
        assertEquals(localHost() + TAMBON, restService.tambonUrl());
    }
}