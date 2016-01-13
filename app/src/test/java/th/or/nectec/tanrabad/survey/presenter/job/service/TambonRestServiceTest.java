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
import th.or.nectec.tanrabad.entity.Location;
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
        assertEquals("8402", subdistrict.getAmphurCode());
        assertEquals("กาญจนดิษฐ์", subdistrict.getAmphurName());
        assertEquals("84", subdistrict.getProvinceCode());
        assertEquals("สุราษฎร์ธานี", subdistrict.getProvinceName());
        assertEquals(new Location(9.10229074425328, 99.3916034624674), subdistrict.getBoundary().get(0).getBoundary().get(0));
        assertEquals(new Location(9.10146829966842, 99.3925836929561), subdistrict.getBoundary().get(0).getBoundary().get(1));
        assertEquals(new Location(9.10105544929067, 99.3943237020771), subdistrict.getBoundary().get(0).getBoundary().get(2));
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
        assertEquals("8402", subdistrictHead.getAmphurCode());
        assertEquals("กาญจนดิษฐ์", subdistrictHead.getAmphurName());
        assertEquals("84", subdistrictHead.getProvinceCode());
        assertEquals("สุราษฎร์ธานี", subdistrictHead.getProvinceName());
        assertEquals(new Location(9.10229074425328, 99.3916034624674), subdistrictHead.getBoundary().get(0).getBoundary().get(0));
        assertEquals(new Location(9.10146829966842, 99.3925836929561), subdistrictHead.getBoundary().get(0).getBoundary().get(1));
        assertEquals(new Location(9.10105544929067, 99.3943237020771), subdistrictHead.getBoundary().get(0).getBoundary().get(2));

        Subdistrict subdistrictMiddle = subdistrictList.get(4);
        assertEquals("130204", subdistrictMiddle.getCode());
        assertEquals("คลองสี่", subdistrictMiddle.getName());
        assertEquals("1302", subdistrictMiddle.getAmphurCode());
        assertEquals("คลองหลวง", subdistrictMiddle.getAmphurName());
        assertEquals("13", subdistrictMiddle.getProvinceCode());
        assertEquals("ปทุมธานี", subdistrictMiddle.getProvinceName());
        assertEquals(new Location(14.1843280907996, 100.69871805926), subdistrictMiddle.getBoundary().get(0).getBoundary().get(0));
        assertEquals(new Location(14.1638786856585, 100.698884284134), subdistrictMiddle.getBoundary().get(0).getBoundary().get(1));
        assertEquals(new Location(14.1081959892648, 100.698946272228), subdistrictMiddle.getBoundary().get(0).getBoundary().get(2));

        Subdistrict subdistrictTail = subdistrictList.get(9);
        assertEquals("460706", subdistrictTail.getCode());
        assertEquals("อิตื้อ", subdistrictTail.getName());
        assertEquals("4607", subdistrictTail.getAmphurCode());
        assertEquals("ยางตลาด", subdistrictTail.getAmphurName());
        assertEquals("46", subdistrictTail.getProvinceCode());
        assertEquals("กาฬสินธุ์", subdistrictTail.getProvinceName());
        assertEquals(new Location(16.515040493287, 103.294015187931), subdistrictTail.getBoundary().get(0).getBoundary().get(0));
        assertEquals(new Location(16.5145843971637, 103.29470389408), subdistrictTail.getBoundary().get(0).getBoundary().get(1));
        assertEquals(new Location(16.5126546787587, 103.296825991738), subdistrictTail.getBoundary().get(0).getBoundary().get(2));
    }

    @Test
    public void testGetUrl() throws Exception {
        TambonRestService restService = new TambonRestService(
                localHost(),
                new LastUpdatePreference());
        assertEquals(localHost() + TAMBON, restService.tambonUrl());
    }
}