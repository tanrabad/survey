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

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.tanrabad.survey.WireMockTestBase;
import org.tanrabad.survey.entity.Place;
import org.tanrabad.survey.entity.User;
import org.tanrabad.survey.entity.lookup.PlaceType;
import org.tanrabad.survey.presenter.AccountUtils;
import org.tanrabad.survey.service.json.JsonEntomology;
import org.tanrabad.survey.utils.ResourceFile;
import org.tanrabad.survey.utils.http.Header;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;


public class EntomologyRestServiceTest extends WireMockTestBase {

    private static final String MON_30_NOV_2015_17_00_00_GMT = "Mon, 30 Nov 2015 17:00:00 GMT";
    private ServiceLastUpdate lastUpdate = Mockito.mock(ServiceLastUpdate.class);
    private EntomologyRestService restService;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        restService = new EntomologyRestService(
                localHost(),
                lastUpdate,
                stubPlace());
    }

    private Place stubPlace() {
        Place place = new Place(UUID.fromString("6e79ca31-d0da-fc50-64d2-ac403dfff644"), "หมู่ 5 บ้านท่าน้ำ");
        place.setType(PlaceType.VILLAGE_COMMUNITY);
        return place;
    }

    @Test
    public void testDefaultParam() throws Exception {
        User user = User.fromUsername("asdf");
        user.setOrganizationId(23);
        AccountUtils.setUser(user);
        assertFalse(restService.getQueryString().contains("org_id=23"));
        assertTrue(restService.getQueryString().contains("place_id=6e79ca31-d0da-fc50-64d2-ac403dfff644"));
    }

    @Test
    public void testGetUrl() throws Exception {
        assertEquals(localHost() + EntomologyRestService.PATH + restService.getQueryString(),
                restService.getUrl());
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
                        .withBody(ResourceFile.read("entomologyList1Item.json"))));

        List<JsonEntomology> jsonEntomologyList = restService.getUpdate();

        assertEquals(1, jsonEntomologyList.size());
        JsonEntomology jsonEntomology1 = jsonEntomologyList.get(0);
        assertEquals("6e79ca31-d0da-fc50-64d2-ac403dfff644", jsonEntomology1.placeId.toString());
        assertEquals("หมู่ 5 บ้านท่าน้ำ", jsonEntomology1.placeName);
        assertEquals(54.00, jsonEntomology1.hiValue, 0);
        assertEquals(25.00, jsonEntomology1.ciValue, 0);
        assertEquals(125.00, jsonEntomology1.biValue, 0);
        assertEquals("แจกัน", jsonEntomology1.keyContainerIn.get(0).containerName);
        assertEquals("ภาชนะที่ไม่ใช้", jsonEntomology1.keyContainerOut.get(0).containerName);
    }

    @Test
    public void testSuccessResponseWithNextPage() throws Exception {
        stubFor(get(urlPathEqualTo(EntomologyRestService.PATH))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(Header.LAST_MODIFIED, MON_30_NOV_2015_17_00_00_GMT)
                        .withHeader(Header.LINK,
                                "<" + localHost() + EntomologyRestService.PATH + "?page=2&per_page=10>; rel=\"next\","
                                        + "<" + localHost()
                                        + EntomologyRestService.PATH + "?page=2&per_page=10>; rel=\"last\"")
                        .withBody(ResourceFile.read("entomologyList1Item.json"))));
        stubFor(get(urlEqualTo(EntomologyRestService.PATH + "?page=2&per_page=10"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(Header.LAST_MODIFIED, MON_30_NOV_2015_17_00_00_GMT)
                        .withBody(ResourceFile.read("entomologyNextList1Item.json"))));

        ArrayList<JsonEntomology> jsonEntomologyList = new ArrayList<>();
        do {
            jsonEntomologyList.addAll(restService.getUpdate());
        } while (restService.hasNextRequest());

        assertEquals(2, jsonEntomologyList.size());
        JsonEntomology jsonEntomology1 = jsonEntomologyList.get(0);
        assertEquals("6e79ca31-d0da-fc50-64d2-ac403dfff644", jsonEntomology1.placeId.toString());
        assertEquals("หมู่ 5 บ้านท่าน้ำ", jsonEntomology1.placeName);
        assertEquals(54.00, jsonEntomology1.hiValue, 0);
        assertEquals(25.00, jsonEntomology1.ciValue, 0);
        assertEquals(125.00, jsonEntomology1.biValue, 0);
        JsonEntomology jsonEntomology2 = jsonEntomologyList.get(1);
        assertEquals("6e79ca31-d0da-fc50-64d2-ac403dfff644", jsonEntomology1.placeId.toString());
        assertEquals("หมู่ 5 บ้านท่าน้ำ", jsonEntomology1.placeName);
        assertEquals(10.0, jsonEntomology2.hiValue, 0);
        assertEquals(15.00, jsonEntomology2.ciValue, 0);
        assertEquals(300.00, jsonEntomology2.biValue, 0);
    }
}
