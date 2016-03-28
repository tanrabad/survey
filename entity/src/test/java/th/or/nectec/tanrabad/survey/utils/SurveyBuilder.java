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

package th.or.nectec.tanrabad.survey.utils;

import org.joda.time.DateTime;
import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.entity.Survey;
import th.or.nectec.tanrabad.entity.SurveyDetail;
import th.or.nectec.tanrabad.entity.User;
import th.or.nectec.tanrabad.entity.field.Location;
import th.or.nectec.tanrabad.entity.lookup.ContainerType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SurveyBuilder {

    public static final Building DEFAULT_BUILDING = Building.withName("default");
    public static final User TESTER = User.fromUsername("tester");
    private final List<SurveyDetail> indoor = new ArrayList<>();
    private final List<SurveyDetail> outdoor = new ArrayList<>();
    private final UUID surveyId;
    private int resident;
    private User surveyor = TESTER;
    private Building building = DEFAULT_BUILDING;
    private Location location;
    private DateTime startTimeStamp;
    private DateTime finishTimeStamp;

    public SurveyBuilder() {
        this(UUID.randomUUID(), TESTER);
    }

    public SurveyBuilder(UUID surveyId, User surveyor) {
        this.surveyId = surveyId;
        this.surveyor = surveyor;
    }

    public Survey build() {
        Survey survey = new Survey(surveyId, surveyor, building);
        survey.setResidentCount(resident);
        survey.setIndoorDetail(indoor);
        survey.setOutdoorDetail(outdoor);
        survey.setLocation(location);
        survey.setStartTimestamp(startTimeStamp);
        survey.setFinishTimestamp(finishTimeStamp);
        return survey;
    }

    public SurveyBuilder setBuilding(Building building) {
        this.building = building;
        return this;
    }

    public SurveyBuilder setResident(int residentCount) {
        resident = residentCount;
        return this;
    }

    public SurveyBuilder addIndoorDetail(UUID surveyDetailid,
                                         ContainerType containerType,
                                         int total,
                                         int foundLarvae) {
        indoor.add(new SurveyDetail(surveyDetailid, containerType, total, foundLarvae));
        return this;
    }

    public SurveyBuilder addOutdoorDetail(UUID surveyDetailId,
                                          ContainerType containerType,
                                          int total,
                                          int foundLarvae) {
        outdoor.add(new SurveyDetail(surveyDetailId, containerType, total, foundLarvae));
        return this;
    }

    public SurveyBuilder setLocation(Location location) {
        this.location = location;
        return this;
    }

    public SurveyBuilder setStartTimeStamp(DateTime startTimeStamp) {
        this.startTimeStamp = startTimeStamp;
        return this;
    }

    public SurveyBuilder setFinishTimeStamp(DateTime finishTimeStamp) {
        this.finishTimeStamp = finishTimeStamp;
        return this;
    }

}
