package th.or.nectec.tanrabad.survey.service.json;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import org.joda.time.DateTimeZone;
import th.or.nectec.tanrabad.entity.Survey;

import java.util.List;
import java.util.UUID;

@JsonObject
public class JsonSurvey {

    @JsonField(name = "survey_id", typeConverter = UUIDConverter.class)
    public UUID surveyID;

    @JsonField(name = "building_id", typeConverter = UUIDConverter.class)
    public UUID buildingID;

    @JsonField(name = "person_count")
    public int personCount;

    @JsonField
    public GeoJsonPoint location;

    @JsonField
    public String surveyor;

    @JsonField
    public List<JsonSurveyDetail> details;

    @JsonField(name = "create_timestamp")
    String createTimestamp;

    public static JsonSurvey parse(Survey survey) {
        JsonSurvey jsonSurvey = new JsonSurvey();
        jsonSurvey.surveyID = survey.getId();
        jsonSurvey.buildingID = survey.getSurveyBuilding().getId();
        jsonSurvey.personCount = survey.getResidentCount();
        jsonSurvey.details = JsonSurveyDetail.parseList(survey.getIndoorDetail(), survey.getOutdoorDetail());
        jsonSurvey.location = GeoJsonPoint.parse(survey.getLocation());
        jsonSurvey.surveyor = survey.getUser().getUsername();
        jsonSurvey.createTimestamp = survey.getStartTimestamp().withZone(DateTimeZone.UTC).toString();
        return jsonSurvey;
    }
}
