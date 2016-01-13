package th.or.nectec.tanrabad.survey.presenter.job.service.jsonentity;

import com.bluelinelabs.logansquare.LoganSquare;
import org.joda.time.DateTime;
import org.junit.Test;
import th.or.nectec.tanrabad.entity.*;
import th.or.nectec.tanrabad.survey.utils.ResourceFile;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class JsonSurveyTest {

    @Test
    public void testParseToJsonString() throws Exception {
        JsonSurvey jsonSurvey = LoganSquare.parse(ResourceFile.read("survey.json"), JsonSurvey.class);

        assertEquals("6af5225b-5642-10fb-a3a0-4e000a842583", jsonSurvey.surveyID.toString());
        assertEquals(5, jsonSurvey.personCount);
        assertEquals(39.745675, jsonSurvey.location.getLatitude(), 0);
        assertEquals(-73.150055, jsonSurvey.location.getLongitude(), 0);
        assertEquals("2015-12-01 13:26:17", jsonSurvey.createTimestamp);
        assertEquals("dcp-user", jsonSurvey.surveyor);

        assertEquals(1, jsonSurvey.details.get(0).containerLocationID);
        assertEquals(1, jsonSurvey.details.get(0).containerType);
        assertEquals(24, jsonSurvey.details.get(0).containerCount);
        assertEquals(10, jsonSurvey.details.get(0).containerHaveLarvae);
    }

    @Test
    public void testParseSurveyDataToJsonString() throws Exception {
        Survey survey = new Survey.Builder(UUID.fromString("1619f46f-6a70-4049-82ec-69dad861a5c6"), stubUser())
                .addIndoorDetail(UUID.fromString("772c4938-b910-11e5-a0c5-aabbccddeeff"), new ContainerType(1, "น้ำใช้"), 10, 5)
                .addOutdoorDetail(UUID.fromString("772c4938-b917-11e5-a0c5-aabbccddeeff"), new ContainerType(2, "น้ำดื่ม"), 7, 5)
                .setBuilding(stubBuilding())
                .setLocation(stubLocation())
                .setStartTimeStamp(new DateTime(2015, 1, 11, 10, 0, 0))
                .setFinishTimeStamp(new DateTime(2015, 1, 11, 10, 5, 0))
                .setResident(5)
                .build();

        JsonSurvey jsonSurvey = JsonSurvey.parse(survey);
        assertEquals("{\"building_id\":\"5cf5665b-5642-10fb-a3a0-5e612a842583\"," +
                "\"create_timestamp\":\"2015-01-11 10:00:00\",\"" +
                "details\":" +
                "[{\"container_count\":10,\"container_have_larvae\":5," +
                "\"container_location_id\":1,\"container_type\":1," +
                "\"survey_detail_id\":\"772c4938-b910-11e5-a0c5-aabbccddeeff\"}," +
                "{\"container_count\":7,\"container_have_larvae\":5," +
                "\"container_location_id\":2,\"container_type\":2,\"" +
                "survey_detail_id\":\"772c4938-b917-11e5-a0c5-aabbccddeeff\"}]," +
                "\"location\":{\"coordinates\":[-73.150055,39.745675],\"type\":\"Point\"}," +
                "\"person_count\":0,\"survey_id\":\"1619f46f-6a70-4049-82ec-69dad861a5c6\"}", LoganSquare.serialize(jsonSurvey));
    }

    private Building stubBuilding() {
        return new Building(UUID.fromString("5cf5665b-5642-10fb-a3a0-5e612a842583"), "abc");
    }

    private Location stubLocation() {
        return new Location(39.745675, -73.150055);
    }

    private User stubUser() {
        return new User("dcp-user");
    }
}