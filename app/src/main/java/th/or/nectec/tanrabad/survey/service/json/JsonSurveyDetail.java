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

package th.or.nectec.tanrabad.survey.service.json;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import th.or.nectec.tanrabad.entity.SurveyDetail;

@JsonObject
public class JsonSurveyDetail {

    public static final int INDOOR_BUILDING = 1;
    public static final int OUTDOOR_BUILDING = 2;

    @JsonField(name = "survey_detail_id", typeConverter = UuidTypeConverter.class)
    UUID surveyDetailId;

    @JsonField(name = "container_location_id")
    int containerLocationId;

    @JsonField(name = "container_type")
    int containerType;

    @JsonField(name = "container_count")
    int containerCount;

    @JsonField(name = "container_have_larva")
    int containerHaveLarva;

    public static ArrayList<JsonSurveyDetail> parseList(
            List<SurveyDetail> indoorDetailList, List<SurveyDetail> outdoorDetailList) {
        ArrayList<JsonSurveyDetail> jsonSurveyDetailList = new ArrayList<>();
        for (SurveyDetail jsonSurveyDetail : indoorDetailList) {
            jsonSurveyDetailList.add(JsonSurveyDetail.parse(INDOOR_BUILDING, jsonSurveyDetail));
        }
        for (SurveyDetail jsonSurveyDetail : outdoorDetailList) {
            jsonSurveyDetailList.add(JsonSurveyDetail.parse(OUTDOOR_BUILDING, jsonSurveyDetail));
        }

        return jsonSurveyDetailList;
    }

    public static JsonSurveyDetail parse(int containerLocationId, SurveyDetail surveyDetail) {
        JsonSurveyDetail jsonSurveyDetail = new JsonSurveyDetail();
        jsonSurveyDetail.surveyDetailId = surveyDetail.getId();
        jsonSurveyDetail.containerLocationId = containerLocationId;
        jsonSurveyDetail.containerCount = surveyDetail.getTotalContainer();
        jsonSurveyDetail.containerHaveLarva = surveyDetail.getFoundLarvaContainer();
        jsonSurveyDetail.containerType = surveyDetail.getContainerType().getId();
        return jsonSurveyDetail;
    }
}