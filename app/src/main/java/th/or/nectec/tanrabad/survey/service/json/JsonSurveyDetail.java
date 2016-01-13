package th.or.nectec.tanrabad.survey.service.json;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import th.or.nectec.tanrabad.entity.SurveyDetail;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@JsonObject
public class JsonSurveyDetail {

    public static final int INDOOR_BUILDING = 1;
    public static final int OUTDOOR_BUILDING = 2;

    @JsonField(name = "survey_detail_id", typeConverter = UUIDConverter.class)
    UUID surveyDetailID;

    @JsonField(name = "container_location_id")
    int containerLocationID;

    @JsonField(name = "container_type")
    int containerType;

    @JsonField(name = "container_count")
    int containerCount;

    @JsonField(name = "container_have_larvae")
    int containerHaveLarvae;

    public static ArrayList<JsonSurveyDetail> parseList(List<SurveyDetail> indoorDetailList, List<SurveyDetail> outdoorDetailList) {
        ArrayList<JsonSurveyDetail> jsonSurveyDetailList = new ArrayList<>();
        for (SurveyDetail jsonSurveyDetail : indoorDetailList) {
            jsonSurveyDetailList.add(JsonSurveyDetail.parse(INDOOR_BUILDING, jsonSurveyDetail));
        }
        for (SurveyDetail jsonSurveyDetail : outdoorDetailList) {
            jsonSurveyDetailList.add(JsonSurveyDetail.parse(OUTDOOR_BUILDING, jsonSurveyDetail));
        }

        return jsonSurveyDetailList;
    }

    public static JsonSurveyDetail parse(int containerLocationID, SurveyDetail surveyDetail) {
        JsonSurveyDetail jsonSurveyDetail = new JsonSurveyDetail();
        jsonSurveyDetail.surveyDetailID = surveyDetail.getId();
        jsonSurveyDetail.containerLocationID = containerLocationID;
        jsonSurveyDetail.containerCount = surveyDetail.getTotalContainer();
        jsonSurveyDetail.containerHaveLarvae = surveyDetail.getFoundLarvaContainer();
        jsonSurveyDetail.containerType = surveyDetail.getContainerType().getId();
        return jsonSurveyDetail;
    }
}