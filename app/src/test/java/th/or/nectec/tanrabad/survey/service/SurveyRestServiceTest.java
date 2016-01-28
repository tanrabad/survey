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
import th.or.nectec.tanrabad.survey.service.json.SurveyRestService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static junit.framework.Assert.assertEquals;

public class SurveyRestServiceTest extends WireMockTestBase {
    @Test
    public void testPost() throws Exception {
        stubFor(post(urlEqualTo(SurveyRestService.PATH))
                .willReturn(aResponse()
                        .withStatus(201)));
        SurveyRestService surveyRestService = new SurveyRestService(localHost(), Mockito.mock(ServiceLastUpdate.class));
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
        SurveyDetail surveyDetail4 = new SurveyDetail(UUID.randomUUID(), getDrinkingWater(), 4, 1);
        List<SurveyDetail> indoorDetail = new ArrayList<>();
        indoorDetail.add(surveyDetail1);
        survey.setIndoorDetail(indoorDetail);
        List<SurveyDetail> outdoorDetail = new ArrayList<>();
        outdoorDetail.add(surveyDetail3);
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
}
