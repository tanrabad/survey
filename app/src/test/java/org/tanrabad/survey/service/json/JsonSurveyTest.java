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

import com.bluelinelabs.logansquare.LoganSquare;

import org.joda.time.DateTime;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.tanrabad.survey.entity.Building;
import org.tanrabad.survey.entity.Organization;
import org.tanrabad.survey.entity.Survey;
import org.tanrabad.survey.entity.User;
import org.tanrabad.survey.entity.field.Location;
import org.tanrabad.survey.entity.lookup.ContainerType;
import org.tanrabad.survey.entity.utils.SurveyBuilder;
import org.tanrabad.survey.utils.ResourceFile;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class JsonSurveyTest {

    @Test
    public void testParseFromJson() throws Exception {
        JsonSurvey jsonSurvey = LoganSquare.parse(ResourceFile.read("survey.json"), JsonSurvey.class);

        assertEquals("6af5225b-5642-10fb-a3a0-4e000a842583", jsonSurvey.surveyId.toString());
        assertEquals(5, jsonSurvey.personCount);
        assertEquals(39.745675, jsonSurvey.location.getLatitude(), 0);
        assertEquals(-73.150055, jsonSurvey.location.getLongitude(), 0);
        assertEquals("2015-01-11T03:00:00.000Z", jsonSurvey.createTimestamp);
        assertEquals("dcp-user", jsonSurvey.surveyor.username);
        assertEquals(1, jsonSurvey.surveyor.organizationId);
        assertEquals("DCP", jsonSurvey.surveyor.organizationName);
        assertEquals(1, jsonSurvey.details.get(0).containerLocationId);
        assertEquals(1, jsonSurvey.details.get(0).containerType);
        assertEquals(24, jsonSurvey.details.get(0).containerCount);
        assertEquals(10, jsonSurvey.details.get(0).containerHaveLarva);
    }

    @Test
    public void testParseSurveyToJson() throws Exception {
        Survey survey = new SurveyBuilder(UUID.fromString("1619f46f-6a70-4049-82ec-69dad861a5c6"), stubUser())
                .addIndoorDetail(UUID.fromString("772c4938-b910-11e5-a0c5-aabbccddeeff"),
                        new ContainerType(1, "น้ำใช้"), 10, 5)
                .addOutdoorDetail(UUID.fromString("772c4938-b917-11e5-a0c5-aabbccddeeff"),
                        new ContainerType(2, "น้ำดื่ม"), 7, 5)
                .setBuilding(stubBuilding())
                .setLocation(stubLocation())
                .setStartTimeStamp(DateTime.parse("2015-01-11T10:00:00.000+07:00"))
                .setFinishTimeStamp(DateTime.parse("2015-01-11T10:00:00.000+07:00"))
                .setResident(5)
                .build();

        JsonSurvey jsonSurvey = JsonSurvey.parse(survey);

        JSONAssert.assertEquals(ResourceFile.read("surveyData.json"),
            LoganSquare.serialize(jsonSurvey), false);
    }
    @Test
    public void testParseSurveyDataWithNullLocationToJsonString() throws Exception {
        Survey survey = new SurveyBuilder(UUID.fromString("1619f46f-6a70-4049-82ec-69dad861a5c6"), stubUser())
                .addIndoorDetail(UUID.fromString("772c4938-b910-11e5-a0c5-aabbccddeeff"),
                        new ContainerType(1, "น้ำใช้"), 10, 5)
                .addOutdoorDetail(UUID.fromString("772c4938-b917-11e5-a0c5-aabbccddeeff"),
                        new ContainerType(2, "น้ำดื่ม"), 7, 5)
                .setBuilding(stubBuilding())
                .setLocation(null)
                .setStartTimeStamp(DateTime.parse("2015-01-11T10:00:00.000+07:00"))
                .setFinishTimeStamp(DateTime.parse("2015-01-11T10:00:00.000+07:00"))
                .setResident(5)
                .build();

        JsonSurvey jsonSurvey = JsonSurvey.parse(survey);

        JSONAssert.assertEquals(ResourceFile.read("surveyNoLocation.json"),
            LoganSquare.serialize(jsonSurvey), false);
    }


    private User stubUser() {
        User user = new User("dcp-user");
        user.setFirstname("dcp");
        user.setLastname("moph");
        user.setOrganization(new Organization(1, "DCP"));
        return user;
    }

    private Building stubBuilding() {
        return new Building(UUID.fromString("5cf5665b-5642-10fb-a3a0-5e612a842583"), "abc");
    }

    private Location stubLocation() {
        return new Location(39.745675, -73.150055);
    }


}


