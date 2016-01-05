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
import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.survey.WireMockTestBase;
import th.or.nectec.tanrabad.survey.utils.ResourceFile;

import java.util.List;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;


public class BuildingRestServiceTest extends WireMockTestBase {

    public static final String BUILDING = "/building";

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
                new LastUpdatePreference());
        restService.getUpdate();
    }

    @Test
    public void testNotModifieResponse() throws Exception {
        stubFor(get(urlEqualTo(BUILDING))
                .willReturn(aResponse()
                        .withStatus(304)
                        .withBody("ssssssASDFASF")));

        BuildingRestService restService = new BuildingRestService(
                localHost(),
                new LastUpdatePreference());
        List<Building> buildings = restService.getUpdate();
        assertEquals(0, buildings.size());
    }

    @Test @Ignore
    public void testSuccessResponse() throws Exception {
        stubFor(get(urlEqualTo(BUILDING))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(ResourceFile.read("buildingList.json"))));
        BuildingRestService restService = new BuildingRestService(
                localHost(),
                new LastUpdatePreference());

        List<Building> buildingList = restService.getUpdate();
        Building building = buildingList.get(0);
        assertEquals(1, buildingList.size());
        assertEquals(uuid("b7a9d934-04fc-a22e-0539-6c17504f732e"), building.getId());
        assertEquals("อาคาร 1", building.getName());
        assertEquals(null, building.getLocation());
    }

    private UUID uuid(String uuid) {
        return UUID.fromString(uuid);
    }

    @Test
    public void testGetUrl() throws Exception {
        BuildingRestService restService = new BuildingRestService(
                localHost(),
                new LastUpdatePreference());
        assertEquals(localHost() + BUILDING, restService.buildingUrl());

    }
}