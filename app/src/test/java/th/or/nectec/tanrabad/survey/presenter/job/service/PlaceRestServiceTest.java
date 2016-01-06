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
import org.mockito.Mockito;
import th.or.nectec.tanrabad.domain.UserRepository;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.entity.User;
import th.or.nectec.tanrabad.survey.WireMockTestBase;
import th.or.nectec.tanrabad.survey.utils.ResourceFile;

import java.util.List;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;


public class PlaceRestServiceTest extends WireMockTestBase {

    public static final String PLACE = "/place";
    UserRepository userRepository = Mockito.mock(UserRepository.class);

    @Test
    @Ignore
    public void testRequest() throws Exception {
        PlaceRestService restService = new PlaceRestService();

        assertEquals(0, restService.getUpdate().size());
    }

    @Test(expected = RestServiceException.class)
    public void test404Response() throws Exception {
        stubFor(get(urlEqualTo(PLACE))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withBody("")));

        PlaceRestService restService = new PlaceRestService(
                localHost(),
                new LastUpdatePreference(),
                userRepository);
        restService.getUpdate();
    }

    @Test
    public void testNotModifiedResponse() throws Exception {
        stubFor(get(urlEqualTo(PLACE))
                .willReturn(aResponse()
                        .withStatus(304)
                        .withBody("ssssssASDFASF")));

        PlaceRestService restService = new PlaceRestService(
                localHost(),
                new LastUpdatePreference(),
                userRepository);
        List<Place> buildings = restService.getUpdate();
        assertEquals(0, buildings.size());
    }

    @Test
    public void testSuccessResponse() throws Exception {
        Mockito.when(userRepository.findUserByName("dcp-user")).thenReturn(stubUser());
        stubFor(get(urlEqualTo(PLACE))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(ResourceFile.read("placeList.json"))));
        PlaceRestService restService = new PlaceRestService(
                localHost(),
                new LastUpdatePreference(),
                userRepository);

        List<Place> placeList = restService.getUpdate();
        Place place = placeList.get(0);
        assertEquals(1, placeList.size());
        assertEquals(uuid("b7a9d934-04fc-a22e-0539-6c17504f732e"), place.getId());
        assertEquals("รพ.สต.ตำบลนาทราย", place.getName());
        assertEquals(null, place.getLocation());
    }

    private UUID uuid(String uuid) {
        return UUID.fromString(uuid);
    }

    private User stubUser() {
        return new User("dcp-user");
    }

    @Test
    public void testSuccessResponseMultipleItem() throws Exception {
        Mockito.when(userRepository.findUserByName("dcp-user")).thenReturn(stubUser());
        stubFor(get(urlEqualTo(PLACE))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(ResourceFile.read("placeList10Item.json"))));
        PlaceRestService restService = new PlaceRestService(
                localHost(),
                new LastUpdatePreference(),
                userRepository);

        List<Place> buildingList = restService.getUpdate();
        assertEquals(10, buildingList.size());
        Place place1 = buildingList.get(0);
        assertEquals(uuid("b7a9d934-04fc-a22e-0539-6c17504f732e"), place1.getId());
        assertEquals("รพ.สต.ตำบลนาทราย", place1.getName());
        assertEquals(null, place1.getLocation());
        Place place5 = buildingList.get(4);
        assertEquals(uuid("68f55d61-a9e9-3166-78bd-3470dbf5e434"), place5.getId());
        assertEquals("รพ.สต.บ้านห้วยไคร้", place5.getName());
        assertEquals(null, place5.getLocation());
        Place place10 = buildingList.get(9);
        assertEquals(uuid("f56e0c74-5b4f-b72f-a993-0227cad188fb"), place10.getId());
        assertEquals("รพ.สต.บ้านห้วยใส", place10.getName());
        assertEquals(null, place10.getLocation());
    }

    @Test
    public void testGetUrl() throws Exception {
        PlaceRestService restService = new PlaceRestService(
                localHost(),
                new LastUpdatePreference(),
                userRepository);
        assertEquals(localHost() + PLACE, restService.placeUrl());

    }
}