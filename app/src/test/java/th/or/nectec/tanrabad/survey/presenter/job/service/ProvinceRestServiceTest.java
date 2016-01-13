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
import th.or.nectec.tanrabad.entity.Province;
import th.or.nectec.tanrabad.survey.WireMockTestBase;
import th.or.nectec.tanrabad.survey.utils.ResourceFile;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;


public class ProvinceRestServiceTest extends WireMockTestBase {

    public static final String PROVINCE = "/province";

    @Test
    @Ignore
    public void testRequest() throws Exception {
        ProvinceRestService restService = new ProvinceRestService();

        assertEquals(0, restService.getUpdate().size());
    }

    @Test(expected = RestServiceException.class)
    public void test404Response() throws Exception {
        stubFor(get(urlEqualTo(PROVINCE))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withBody("")));

        ProvinceRestService restService = new ProvinceRestService(
                localHost(),
                new LastUpdatePreference());
        restService.getUpdate();
    }

    @Test
    public void testNotModifiedResponse() throws Exception {
        stubFor(get(urlEqualTo(PROVINCE))
                .willReturn(aResponse()
                        .withStatus(304)
                        .withBody("")));

        ProvinceRestService restService = new ProvinceRestService(
                localHost(),
                new LastUpdatePreference());
        List<Province> buildings = restService.getUpdate();
        assertEquals(0, buildings.size());
    }

    @Test
    public void testSuccessResponse() throws Exception {
        stubFor(get(urlEqualTo(PROVINCE))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(ResourceFile.read("provinceList5Item.json"))));
        ProvinceRestService restService = new ProvinceRestService(
                localHost(),
                new LastUpdatePreference());
        List<Province> subdistrictList = restService.getUpdate();
        Province district = subdistrictList.get(0);

        assertEquals("50", district.getCode());
        assertEquals("เชียงใหม่", district.getName());
        assertEquals(true, district.getBoundary() != null);
    }

    @Test
    public void testSuccessResponseMultipleItem() throws Exception {
        stubFor(get(urlEqualTo(PROVINCE))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(ResourceFile.read("provinceList5Item.json"))));
        ProvinceRestService restService = new ProvinceRestService(
                localHost(),
                new LastUpdatePreference());
        List<Province> provinceList = restService.getUpdate();

        Province provinceHead = provinceList.get(0);
        assertEquals("50", provinceHead.getCode());
        assertEquals("เชียงใหม่", provinceHead.getName());
        assertEquals(true, provinceHead.getBoundary() != null);


        Province provinceMiddle = provinceList.get(2);
        assertEquals("76", provinceMiddle.getCode());
        assertEquals("เพชรบุรี", provinceMiddle.getName());
        assertEquals(true, provinceMiddle.getBoundary() != null);

        Province provinceTail = provinceList.get(4);
        assertEquals("21", provinceTail.getCode());
        assertEquals("ระยอง", provinceTail.getName());
        assertEquals(true, provinceTail.getBoundary() != null);
    }

    @Test
    public void testGetUrl() throws Exception {
        ProvinceRestService restService = new ProvinceRestService(
                localHost(),
                new LastUpdatePreference());
        assertEquals(localHost() + PROVINCE, restService.provinceUrl());
    }
}