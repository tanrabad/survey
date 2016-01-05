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

import android.support.annotation.NonNull;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import th.or.nectec.tanrabad.domain.UserRepository;
import th.or.nectec.tanrabad.domain.place.PlaceRepository;
import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.entity.User;
import th.or.nectec.tanrabad.survey.WireMockTestBase;
import th.or.nectec.tanrabad.survey.utils.ResourceFile;

import java.util.List;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;


public class BuildingRestServiceTest extends WireMockTestBase {

    public static final String BUILDING = "/building";
    UserRepository userRepository = Mockito.mock(UserRepository.class);
    PlaceRepository placeRepository = Mockito.mock(PlaceRepository.class);

    @Test
    @Ignore
    public void testRequest() throws Exception {
        BuildingRestService restService = new BuildingRestService();

        assertEquals(0, restService.getUpdate().size());
    }

    @Test(expected = RestServiceException.class)
    public void test404Response() throws Exception {
        stubFor(get(urlEqualTo(BUILDING))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withBody("")));

        BuildingRestService restService = new BuildingRestService(
                localHost(),
                new LastUpdatePreference(),
                placeRepository, userRepository);
        restService.getUpdate();
    }

    @Test
    public void testNotModifiedResponse() throws Exception {
        stubFor(get(urlEqualTo(BUILDING))
                .willReturn(aResponse()
                        .withStatus(304)
                        .withBody("")));

        BuildingRestService restService = new BuildingRestService(
                localHost(),
                new LastUpdatePreference(),
                placeRepository, userRepository);
        List<Building> buildings = restService.getUpdate();
        assertEquals(0, buildings.size());
    }

    @Test
    public void testSuccessResponse() throws Exception {
        Mockito.when(userRepository.findUserByName("dcp-user")).thenReturn(stubUser());
        Mockito.when(placeRepository.findPlaceByUUID(uuid("b5f7b062-12f5-3402-ac88-0343733503bd"))).thenReturn(stubPlace());
        stubFor(get(urlEqualTo(BUILDING))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(ResourceFile.read("buildingList.json"))));
        BuildingRestService restService = new BuildingRestService(
                localHost(),
                new LastUpdatePreference(),
                placeRepository, userRepository);

        List<Building> buildingList = restService.getUpdate();
        Building building = buildingList.get(0);
        assertEquals(1, buildingList.size());
        assertEquals(uuid("b7a9d934-04fc-a22e-0539-6c17504f732e"), building.getId());
        assertEquals("อาคาร 1", building.getName());
        assertEquals(null, building.getLocation());
    }

    @Test
    public void testSuccessResponseMultipleItem() throws Exception {
        Mockito.when(userRepository.findUserByName("dcp-user")).thenReturn(stubUser());
        Mockito.when(placeRepository.findPlaceByUUID(uuid("b5f7b062-12f5-3402-ac88-0343733503bd"))).thenReturn(stubPlace());
        stubFor(get(urlEqualTo(BUILDING))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(ResourceFile.read("buildingList10Item.json"))));
        BuildingRestService restService = new BuildingRestService(
                localHost(),
                new LastUpdatePreference(),
                placeRepository, userRepository);

        List<Building> buildingList = restService.getUpdate();
        assertEquals(10, buildingList.size());
        Building building1 = buildingList.get(0);
        assertEquals(uuid("b7a9d934-04fc-a22e-0539-6c17504f732e"), building1.getId());
        assertEquals("อาคาร 1", building1.getName());
        assertEquals(null, building1.getLocation());
        Building building5 = buildingList.get(4);
        assertEquals(uuid("b7a9d934-04fc-a22e-0539-6c17504f7313"), building5.getId());
        assertEquals("อาคาร 5", building5.getName());
        assertEquals(null, building5.getLocation());
        Building building10 = buildingList.get(9);
        assertEquals(uuid("b7a9d934-04fc-a22e-0539-6c17504f7318"), building10.getId());
        assertEquals("อาคาร 10", building10.getName());
        assertEquals(null, building10.getLocation());
    }

    private UUID uuid(String uuid) {
        return UUID.fromString(uuid);
    }

    @Test
    public void testGetUrl() throws Exception {
        BuildingRestService restService = new BuildingRestService(
                localHost(),
                new LastUpdatePreference(),
                placeRepository, userRepository);
        assertEquals(localHost() + BUILDING, restService.buildingUrl());

    }

    private User stubUser() {
        return new User("dcp-user");
    }

    @NonNull
    private Place stubPlace() {
        Place place = new Place(UUID.fromString("b5f7b062-12f5-3402-ac88-0343733503bd"), "รพ.สต.ตำบลนาทราย");
        place.setType(Place.TYPE_HOSPITAL);
        return place;
    }
}