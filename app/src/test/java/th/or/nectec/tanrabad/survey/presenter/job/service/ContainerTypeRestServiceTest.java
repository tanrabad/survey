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
import th.or.nectec.tanrabad.entity.ContainerType;
import th.or.nectec.tanrabad.survey.WireMockTestBase;
import th.or.nectec.tanrabad.survey.utils.ResourceFile;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;


public class ContainerTypeRestServiceTest extends WireMockTestBase {

    public static final String CONTAINER_TYPE = "/containertype";

    @Test
    @Ignore
    public void testRequest() throws Exception {
        ContainerTypeRestService restService = new ContainerTypeRestService();

        assertEquals(0, restService.getUpdate().size());
    }

    @Test(expected = RestServiceException.class)
    public void test404Response() throws Exception {
        stubFor(get(urlEqualTo(CONTAINER_TYPE))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withBody("")));

        ContainerTypeRestService restService = new ContainerTypeRestService(
                localHost(),
                new LastUpdatePreference());
        restService.getUpdate();
    }

    @Test
    public void testNotModifiedResponse() throws Exception {
        stubFor(get(urlEqualTo(CONTAINER_TYPE))
                .willReturn(aResponse()
                        .withStatus(304)
                        .withBody("")));

        ContainerTypeRestService restService = new ContainerTypeRestService(
                localHost(),
                new LastUpdatePreference());
        List<ContainerType> buildings = restService.getUpdate();
        assertEquals(0, buildings.size());
    }

    @Test
    public void testSuccessResponse() throws Exception {
        stubFor(get(urlEqualTo(CONTAINER_TYPE))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(ResourceFile.read("containerTypeList.json"))));
        ContainerTypeRestService restService = new ContainerTypeRestService(
                localHost(),
                new LastUpdatePreference());
        List<ContainerType> containerTypeList = restService.getUpdate();
        ContainerType containerType = containerTypeList.get(0);

        assertEquals(0, containerType.getId());
        assertEquals("ไม่ระบุ", containerType.getName());
    }

    @Test
    public void testSuccessResponseMultipleItem() throws Exception {
        stubFor(get(urlEqualTo(CONTAINER_TYPE))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(ResourceFile.read("containerTypeList.json"))));
        ContainerTypeRestService restService = new ContainerTypeRestService(
                localHost(),
                new LastUpdatePreference());
        List<ContainerType> containerTypeList = restService.getUpdate();
        ContainerType containerTypeHead = containerTypeList.get(0);
        assertEquals(0, containerTypeHead.getId());
        assertEquals("ไม่ระบุ", containerTypeHead.getName());

        ContainerType containerTypeMiddle = containerTypeList.get(5);
        assertEquals(5, containerTypeMiddle.getId());
        assertEquals("จานรองกระถาง", containerTypeMiddle.getName());

        ContainerType containerTypeTail = containerTypeList.get(10);
        assertEquals(10, containerTypeTail.getId());
        assertEquals("อื่นๆ(ที่ใช้ประโยชน์)", containerTypeTail.getName());
    }

    @Test
    public void testGetUrl() throws Exception {
        ContainerTypeRestService restService = new ContainerTypeRestService(
                localHost(),
                new LastUpdatePreference());
        assertEquals(localHost() + CONTAINER_TYPE, restService.provinceUrl());
    }
}