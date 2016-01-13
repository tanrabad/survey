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
import th.or.nectec.tanrabad.entity.District;
import th.or.nectec.tanrabad.entity.Location;
import th.or.nectec.tanrabad.survey.WireMockTestBase;
import th.or.nectec.tanrabad.survey.utils.ResourceFile;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;


public class AmphurRestServiceTest extends WireMockTestBase {

    public static final String AMPHUR = "/amphur";

    @Test
    @Ignore
    public void testRequest() throws Exception {
        AmphurRestService restService = new AmphurRestService();

        assertEquals(0, restService.getUpdate().size());
    }

    @Test(expected = RestServiceException.class)
    public void test404Response() throws Exception {
        stubFor(get(urlEqualTo(AMPHUR))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withBody("")));

        AmphurRestService restService = new AmphurRestService(
                localHost(),
                new LastUpdatePreference());
        restService.getUpdate();
    }

    @Test
    public void testNotModifiedResponse() throws Exception {
        stubFor(get(urlEqualTo(AMPHUR))
                .willReturn(aResponse()
                        .withStatus(304)
                        .withBody("")));

        AmphurRestService restService = new AmphurRestService(
                localHost(),
                new LastUpdatePreference());
        List<District> buildings = restService.getUpdate();
        assertEquals(0, buildings.size());
    }

    @Test
    public void testSuccessResponse() throws Exception {
        stubFor(get(urlEqualTo(AMPHUR))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(ResourceFile.read("districtInNonthaburi.json"))));
        AmphurRestService restService = new AmphurRestService(
                localHost(),
                new LastUpdatePreference());
        List<District> subdistrictList = restService.getUpdate();
        District district = subdistrictList.get(0);

        assertEquals("1202", district.getCode());
        assertEquals("บางกรวย", district.getName());
        assertEquals("12", district.getProvinceCode());
        assertEquals("นนทบุรี", district.getProvinceName());
        assertEquals(new Location(13.8358835573344, 100.426092280359), district.getBoundary().get(0).getBoundary().get(0));
        assertEquals(new Location(13.8328855111975, 100.428855074237), district.getBoundary().get(0).getBoundary().get(1));
        assertEquals(new Location(13.8279769668251, 100.439485944792), district.getBoundary().get(0).getBoundary().get(2));
    }

    @Test
    public void testSuccessResponseMultipleItem() throws Exception {
        stubFor(get(urlEqualTo(AMPHUR))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(ResourceFile.read("districtInNonthaburi.json"))));
        AmphurRestService restService = new AmphurRestService(
                localHost(),
                new LastUpdatePreference());
        List<District> districtList = restService.getUpdate();

        District districtHead = districtList.get(0);
        assertEquals("1202", districtHead.getCode());
        assertEquals("บางกรวย", districtHead.getName());
        assertEquals("12", districtHead.getProvinceCode());
        assertEquals("นนทบุรี", districtHead.getProvinceName());
        assertEquals(new Location(13.8358835573344, 100.426092280359), districtHead.getBoundary().get(0).getBoundary().get(0));
        assertEquals(new Location(13.8328855111975, 100.428855074237), districtHead.getBoundary().get(0).getBoundary().get(1));
        assertEquals(new Location(13.8279769668251, 100.439485944792), districtHead.getBoundary().get(0).getBoundary().get(2));


        District districtMiddle = districtList.get(2);
        assertEquals("1204", districtMiddle.getCode());
        assertEquals("บางบัวทอง", districtMiddle.getName());
        assertEquals("12", districtMiddle.getProvinceCode());
        assertEquals("นนทบุรี", districtMiddle.getProvinceName());
        assertEquals(new Location(13.9847082843713, 100.396942845676), districtMiddle.getBoundary().get(0).getBoundary().get(0));
        assertEquals(new Location(13.9831409292112, 100.403034873068), districtMiddle.getBoundary().get(0).getBoundary().get(1));
        assertEquals(new Location(13.9853077593442, 100.409743725234), districtMiddle.getBoundary().get(0).getBoundary().get(2));

        District districtTail = districtList.get(5);
        assertEquals("1201", districtTail.getCode());
        assertEquals("เมืองนนทบุรี", districtTail.getName());
        assertEquals("12", districtTail.getProvinceCode());
        assertEquals("นนทบุรี", districtTail.getProvinceName());
        assertEquals(new Location(13.8947189372409, 100.499333246907), districtTail.getBoundary().get(0).getBoundary().get(0));
        assertEquals(new Location(13.8912485098046, 100.505650335683), districtTail.getBoundary().get(0).getBoundary().get(1));
        assertEquals(new Location(13.8801882939396, 100.5466445234), districtTail.getBoundary().get(0).getBoundary().get(2));
    }

    @Test
    public void testGetUrl() throws Exception {
        AmphurRestService restService = new AmphurRestService(
                localHost(),
                new LastUpdatePreference());
        assertEquals(localHost() + AMPHUR, restService.amphurUrl());
    }
}