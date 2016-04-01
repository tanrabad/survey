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

import com.bluelinelabs.logansquare.LoganSquare;
import org.joda.time.DateTime;
import org.junit.Test;
import org.mockito.Mockito;
import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.entity.Survey;
import th.or.nectec.tanrabad.entity.SurveyDetail;
import th.or.nectec.tanrabad.entity.User;
import th.or.nectec.tanrabad.entity.field.Location;
import th.or.nectec.tanrabad.entity.lookup.ContainerType;
import th.or.nectec.tanrabad.survey.WireMockTestBase;
import th.or.nectec.tanrabad.survey.service.json.JsonSurvey;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class SurveyRestServiceTest extends WireMockTestBase {

    private final ServiceLastUpdate lastUpdate = Mockito.mock(ServiceLastUpdate.class);

    @Test
    public void testPost() throws Exception {
        stubFor(post(urlEqualTo(SurveyRestService.PATH))
                .willReturn(aResponse()
                        .withStatus(201)));
        SurveyRestService surveyRestService = new SurveyRestService(localHost(), lastUpdate);
        Survey survey = getSurvey();

        boolean postDataResult = surveyRestService.post(survey);

        assertEquals(true, postDataResult);
        verify(postRequestedFor(urlEqualTo(SurveyRestService.PATH))
                .withRequestBody(equalToJson(LoganSquare.serialize(JsonSurvey.parse(survey)))));
    }

    private Survey getSurvey() {
        Survey survey = new Survey(UUID.randomUUID(), User.fromUsername("dpc-user"),
                new Building(UUID.fromString("b7a9d934-04fc-a22e-0539-6c17504f7e3e"), "อาคาร 1"));
        survey.setLocation(new Location(15, 120));
        survey.setResidentCount(15);
        SurveyDetail surveyDetail1 = new SurveyDetail(UUID.randomUUID(), getWater(), 3, 2);
        SurveyDetail surveyDetail3 = new SurveyDetail(UUID.randomUUID(), getWater(), 6, 5);
        List<SurveyDetail> indoorDetail = new ArrayList<>();
        indoorDetail.add(surveyDetail1);
        survey.setIndoorDetail(indoorDetail);
        List<SurveyDetail> outdoorDetail = new ArrayList<>();
        outdoorDetail.add(surveyDetail3);
        SurveyDetail surveyDetail4 = new SurveyDetail(UUID.randomUUID(), getDrinkingWater(), 4, 1);
        outdoorDetail.add(surveyDetail4);
        survey.setOutdoorDetail(outdoorDetail);
        survey.setStartTimestamp(new DateTime("2015-12-24T12:19:20.626+07:00"));
        survey.setFinishTimestamp(new DateTime("2015-12-24T13:20:21.626+07:00"));
        return survey;
    }

    private ContainerType getWater() {
        return new ContainerType(1, "น้ำใช้");
    }

    private ContainerType getDrinkingWater() {
        return new ContainerType(2, "น้ำดื่ม");
    }

    @Test
    public void testDelete() throws Exception {
        Survey survey = getSurvey();
        String deleteUrl = SurveyRestService.PATH.concat("/").concat(survey.getId().toString());
        stubFor(delete(urlPathEqualTo(deleteUrl))
                .willReturn(aResponse()
                        .withStatus(200)));

        SurveyRestService surveyRestService = new SurveyRestService(localHost(), lastUpdate);
        boolean result = surveyRestService.delete(survey);

        assertTrue(result);
        verify(deleteRequestedFor(urlEqualTo(deleteUrl)));
    }

    @Test(expected = RestServiceException.class)
    public void testDeleteNotSuccess() throws Exception {
        Survey survey = getSurvey();
        String deleteUrl = SurveyRestService.PATH.concat("/").concat(survey.getId().toString());
        stubFor(delete(urlPathEqualTo(deleteUrl))
                .willReturn(aResponse()
                        .withStatus(409)));

        SurveyRestService surveyRestService = new SurveyRestService(localHost(), lastUpdate);
        surveyRestService.delete(survey);

    }
}
