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

import org.junit.Test;
import org.mockito.Mockito;
import th.or.nectec.tanrabad.domain.UserRepository;
import th.or.nectec.tanrabad.entity.Location;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.entity.User;
import th.or.nectec.tanrabad.survey.WireMockTestBase;
import th.or.nectec.tanrabad.survey.service.http.Header;
import th.or.nectec.tanrabad.survey.utils.ResourceFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;


public class PlaceRestServiceTest extends WireMockTestBase {

    UserRepository userRepository = Mockito.mock(UserRepository.class);
    LastUpdate lastUpdate = Mockito.mock(LastUpdate.class);


    @Test(expected = RestServiceException.class)
    public void test404Response() throws Exception {
        stubFor(get(urlEqualTo(PlaceRestService.PATH))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withBody("")));

        PlaceRestService restService = new PlaceRestService(
                localHost(),
                lastUpdate,
                userRepository);
        restService.getUpdate();
    }

    @Test
    public void testNotModifiedResponse() throws Exception {
        stubFor(get(urlEqualTo(PlaceRestService.PATH))
                .willReturn(aResponse()
                        .withStatus(304)
                        .withBody("")));

        PlaceRestService restService = new PlaceRestService(
                localHost(),
                lastUpdate,
                userRepository);
        List<Place> buildings = restService.getUpdate();
        assertEquals(0, buildings.size());
    }

    @Test
    public void testSuccessResponse() throws Exception {
        Mockito.when(userRepository.findUserByName("dcp-user")).thenReturn(stubUser());
        stubFor(get(urlEqualTo(PlaceRestService.PATH))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(Header.LAST_MODIFIED, "Mon, 30 Nov 2015 17:00:00 GMT")
                        .withBody(ResourceFile.read("placeList.json"))));
        PlaceRestService restService = new PlaceRestService(
                localHost(),
                lastUpdate,
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
        stubFor(get(urlEqualTo(PlaceRestService.PATH))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(Header.LAST_MODIFIED, "Mon, 30 Nov 2015 17:00:00 GMT")
                        .withBody(ResourceFile.read("placeList10Item.json"))));
        PlaceRestService restService = new PlaceRestService(
                localHost(),
                lastUpdate,
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
    public void testSuccessResponseWithNextPage() throws Exception {
        Mockito.when(userRepository.findUserByName("dcp-user")).thenReturn(stubUser());
        stubFor(get(urlEqualTo(PlaceRestService.PATH))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(Header.LAST_MODIFIED, "Mon, 30 Nov 2015 17:00:00 GMT")
                        .withHeader(Header.LINK,
                                "<" + localHost() + PlaceRestService.PATH + "&page=2&per_page=10>; rel=\"next\"," +
                                        "<" + localHost() + PlaceRestService.PATH + "&page=2&per_page=10>; rel=\"last\"")
                        .withBody(ResourceFile.read("placeList10Item.json"))));
        stubFor(get(urlEqualTo(PlaceRestService.PATH + "&page=2&per_page=10"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(Header.LAST_MODIFIED, "Mon, 30 Nov 2015 17:00:00 GMT")
                        .withBody(ResourceFile.read("placeNextList5Item.json"))));


        RestService<Place> service = new PlaceRestService(
                localHost(),
                lastUpdate,
                userRepository);
        ArrayList<Place> placeArrayList = new ArrayList<>();

        do {
            placeArrayList.addAll(service.getUpdate());
        } while (service.hasNextRequest());

        assertEquals(15, placeArrayList.size());
        Place place1 = placeArrayList.get(0);
        assertEquals(uuid("b7a9d934-04fc-a22e-0539-6c17504f732e"), place1.getId());
        assertEquals("รพ.สต.ตำบลนาทราย", place1.getName());
        assertEquals(null, place1.getLocation());
        Place place5 = placeArrayList.get(4);
        assertEquals(uuid("68f55d61-a9e9-3166-78bd-3470dbf5e434"), place5.getId());
        assertEquals("รพ.สต.บ้านห้วยไคร้", place5.getName());
        assertEquals(null, place5.getLocation());
        Place place15 = placeArrayList.get(14);
        assertEquals(uuid("648e41e1-2ccd-00f2-f065-ba111c384c5b"), place15.getId());
        assertEquals("รพ.สต.บ้านหลวง ต.นาพู่", place15.getName());
        assertEquals(new Location(17.6048028519667, 102.755400339956), place15.getLocation());
    }
}