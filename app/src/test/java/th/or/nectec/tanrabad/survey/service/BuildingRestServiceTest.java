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

import android.support.annotation.NonNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.UUID;

import th.or.nectec.tanrabad.domain.place.PlaceRepository;
import th.or.nectec.tanrabad.domain.user.UserRepository;
import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.entity.lookup.PlaceType;
import th.or.nectec.tanrabad.survey.WireMockTestBase;
import th.or.nectec.tanrabad.survey.service.http.Header;
import th.or.nectec.tanrabad.survey.utils.ResourceFile;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.junit.Assert.assertEquals;

public class BuildingRestServiceTest extends WireMockTestBase {

    public static final String MON_30_NOV_2015_17_00_00_GMT = "Mon, 30 Nov 2015 17:00:00 GMT";
    UserRepository userRepository = Mockito.mock(UserRepository.class);
    PlaceRepository placeRepository = Mockito.mock(PlaceRepository.class);
    ServiceLastUpdate lastUpdate = Mockito.mock(ServiceLastUpdate.class);
    BuildingRestService restService;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        Mockito.when(lastUpdate.get()).thenReturn(PlaceRestServiceTest.MON_30_NOV_2015_17_00_00_GMT);
        Mockito.when(userRepository.findByUsername("dcp-user")).thenReturn(stubUser());
        Mockito.when(placeRepository.findByUUID(uuid("b5f7b062-12f5-3402-ac88-0343733503bd"))).thenReturn(stubPlace());

        restService = new BuildingRestService(
                localHost(),
                lastUpdate,
                placeRepository, userRepository);
    }

    private UUID uuid(String uuid) {
        return UUID.fromString(uuid);
    }

    @NonNull
    private Place stubPlace() {
        Place place = new Place(UUID.fromString("b5f7b062-12f5-3402-ac88-0343733503bd"), "รพ.สต.ตำบลนาทราย");
        place.setType(PlaceType.HOSPITAL);
        return place;
    }

    @Test(expected = RestServiceException.class)
    public void test404Response() throws Exception {
        stubFor(get(urlEqualTo(BuildingRestService.PATH))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withBody("")));

        restService.getUpdate();
    }

    @Test
    public void testNotModifiedResponse() throws Exception {
        stubFor(get(urlPathEqualTo(BuildingRestService.PATH))
                .willReturn(aResponse()
                        .withStatus(304)
                        .withBody("[]")));

        List<Building> buildings = restService.getUpdate();

        assertEquals(0, buildings.size());
    }

    @Test
    public void testSuccessResponse() throws Exception {
        stubFor(get(urlPathEqualTo(BuildingRestService.PATH))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(Header.LAST_MODIFIED, MON_30_NOV_2015_17_00_00_GMT)
                        .withBody(ResourceFile.read("buildingList1Item.json"))));

        List<Building> buildingList = restService.getUpdate();

        Building building = buildingList.get(0);
        assertEquals(1, buildingList.size());
        assertEquals(uuid("b7a9d934-04fc-a22e-0539-6c17504f732e"), building.getId());
        assertEquals("อาคาร 1", building.getName());
        assertEquals(null, building.getLocation());
        Mockito.verify(lastUpdate).save(MON_30_NOV_2015_17_00_00_GMT);
    }

    @Test
    public void testSuccessResponseMultipleItem() throws Exception {
        stubFor(get(urlPathEqualTo(BuildingRestService.PATH))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(Header.LAST_MODIFIED, MON_30_NOV_2015_17_00_00_GMT)
                        .withBody(ResourceFile.read("buildingList3Item.json"))));

        List<Building> buildingList = restService.getUpdate();

        assertEquals(3, buildingList.size());
        Building building1 = buildingList.get(0);
        assertEquals(uuid("b7a9d934-04fc-a22e-0539-6c17504f732e"), building1.getId());
        assertEquals("อาคาร 1", building1.getName());
        assertEquals(null, building1.getLocation());
        Building building2 = buildingList.get(1);
        assertEquals(uuid("b7a9d934-04fc-a22e-0539-6c17504f7310"), building2.getId());
        assertEquals("อาคาร 2", building2.getName());
        assertEquals(null, building2.getLocation());
        Building building3 = buildingList.get(2);
        assertEquals(uuid("b7a9d934-04fc-a22e-0539-6c17504f7311"), building3.getId());
        assertEquals("อาคาร 3", building3.getName());
        assertEquals(null, building3.getLocation());
        Mockito.verify(lastUpdate).save(MON_30_NOV_2015_17_00_00_GMT);
    }

    @Test
    public void testWithoutIfModifiedSinceHeader() throws Exception {
        Mockito.when(lastUpdate.get()).thenReturn(null);
        stubFor(get(urlPathEqualTo(BuildingRestService.PATH))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(Header.LAST_MODIFIED, MON_30_NOV_2015_17_00_00_GMT)
                        .withBody(ResourceFile.read("buildingList3Item.json"))));

        restService.getUpdate();

        verify(getRequestedFor(urlPathEqualTo(BuildingRestService.PATH))
                .withoutHeader(Header.IF_MODIFIED_SINCE));
    }
}