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

package org.tanrabad.survey.service.json;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import org.joda.time.DateTimeZone;
import th.or.nectec.tanrabad.entity.Survey;

import java.util.List;
import java.util.UUID;

@JsonObject(serializeNullObjects = true)
public class JsonSurvey {

    @JsonField(name = "survey_id", typeConverter = UuidTypeConverter.class)
    public UUID surveyId;

    @JsonField(name = "building_id", typeConverter = UuidTypeConverter.class)
    public UUID buildingId;

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
        jsonSurvey.surveyId = survey.getId();
        jsonSurvey.buildingId = survey.getSurveyBuilding().getId();
        jsonSurvey.personCount = survey.getResidentCount();
        jsonSurvey.details = JsonSurveyDetail.parseList(survey.getIndoorDetail(), survey.getOutdoorDetail());
        jsonSurvey.location = survey.getLocation() == null ? null : GeoJsonPoint.parse(survey.getLocation());
        jsonSurvey.surveyor = survey.getUser().getUsername();
        jsonSurvey.createTimestamp = survey.getStartTimestamp().withZone(DateTimeZone.UTC).toString();
        return jsonSurvey;
    }
}
