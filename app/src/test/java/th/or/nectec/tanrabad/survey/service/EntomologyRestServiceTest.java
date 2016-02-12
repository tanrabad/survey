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

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import th.or.nectec.tanrabad.survey.WireMockTestBase;
import th.or.nectec.tanrabad.survey.service.http.Header;
import th.or.nectec.tanrabad.survey.service.json.JsonEntomology;
import th.or.nectec.tanrabad.survey.utils.ResourceFile;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;


public class EntomologyRestServiceTest extends WireMockTestBase {

    public static final String MON_30_NOV_2015_17_00_00_GMT = "Mon, 30 Nov 2015 17:00:00 GMT";
    ServiceLastUpdate lastUpdate = Mockito.mock(ServiceLastUpdate.class);
    EntomologyRestService restService;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        restService = new EntomologyRestService(
                localHost(),
                lastUpdate);
    }

    @Test
    public void testGetUrl() throws Exception {
        assertEquals(localHost() + EntomologyRestService.PATH + "?" + restService.getDefaultParams(), restService.getUrl());
    }

    @Test(expected = RestServiceException.class)
    public void test404Response() throws Exception {
        stubFor(get(urlPathEqualTo(EntomologyRestService.PATH))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withBody("")));

        restService.getUpdate();
    }

    @Test
    public void testNotModifiedResponse() throws Exception {
        Mockito.when(lastUpdate.get()).thenReturn(MON_30_NOV_2015_17_00_00_GMT);
        stubFor(get(urlPathEqualTo(EntomologyRestService.PATH))
                .willReturn(aResponse()
                        .withStatus(304)
                        .withBody("")));

        List<JsonEntomology> buildings = restService.getUpdate();

        assertEquals(0, buildings.size());
        verify(getRequestedFor(urlPathEqualTo(EntomologyRestService.PATH))
                .withHeader(Header.IF_MODIFIED_SINCE, equalTo(MON_30_NOV_2015_17_00_00_GMT)));
    }

    @Test
    public void testSuccessResponseMultipleItem() throws Exception {
        stubFor(get(urlPathEqualTo(EntomologyRestService.PATH))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(Header.LAST_MODIFIED, MON_30_NOV_2015_17_00_00_GMT)
                        .withBody(ResourceFile.read("entomologyList3Item.json"))));

        List<JsonEntomology> jsonEntomologyList = restService.getUpdate();

        assertEquals(3, jsonEntomologyList.size());
        JsonEntomology jsonEntomology1 = jsonEntomologyList.get(0);
        assertEquals("6e79ca31-d0da-fc50-64d2-ac403dfff644", jsonEntomology1.placeID.toString());
        assertEquals("หมู่ 5 บ้านท่าน้ำ", jsonEntomology1.placeName);
        assertEquals(54.00, jsonEntomology1.hiValue, 0);
        assertEquals(25.00, jsonEntomology1.ciValue, 0);
        assertEquals(125.00, jsonEntomology1.biValue, 0);
        assertEquals("แจกัน", jsonEntomology1.keyContainerIn.get(0).containerName);
        assertEquals("ภาชนะที่ไม่ใช้", jsonEntomology1.keyContainerOut.get(0).containerName);

        JsonEntomology jsonEntomology2 = jsonEntomologyList.get(1);
        assertEquals("a9956d82-3c3a-5bfa-ae0b-2c0e2b786810", jsonEntomology2.placeID.toString());
        assertEquals("หมู่ 9 บ้านวัดแดง", jsonEntomology2.placeName);
        assertEquals(75.0, jsonEntomology2.hiValue, 0);
        assertEquals(22.0, jsonEntomology2.ciValue, 0);
        assertEquals(425.0, jsonEntomology2.biValue, 0);
        assertEquals("อื่น ๆ (ที่ใช้ประโยชน์)", jsonEntomology2.keyContainerIn.get(0).containerName);
        assertEquals("แจกัน", jsonEntomology2.keyContainerOut.get(0).containerName);

        JsonEntomology jsonEntomology3 = jsonEntomologyList.get(2);
        assertEquals("86df6a0f-4368-c972-d4a1-15574868d085", jsonEntomology3.placeID.toString());
        assertEquals("หมู่ 1 บ้านท่าลาน", jsonEntomology3.placeName);
        assertEquals(66.0, jsonEntomology3.hiValue, 0);
        assertEquals(6.0, jsonEntomology3.ciValue, 0);
        assertEquals(66.0, jsonEntomology3.biValue, 0);
        assertEquals("แจกัน,น้ำใช้", jsonEntomology3.keyContainerIn.get(0).containerName);
        assertEquals("แจกัน", jsonEntomology3.keyContainerOut.get(0).containerName);
    }


}