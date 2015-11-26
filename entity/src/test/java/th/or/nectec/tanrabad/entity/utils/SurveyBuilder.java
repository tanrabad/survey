/*
 * Copyright (c) 2015 NECTEC
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

package th.or.nectec.tanrabad.entity.utils;

import th.or.nectec.tanrabad.entity.*;

import java.util.ArrayList;
import java.util.List;


public class SurveyBuilder {

    public static final Building DEFAULT_BUILDING = Building.withName("default");
    public static final User TESTER = User.fromUsername("tester");
    private List<SurveyDetail> indoor = new ArrayList<>();
    private List<SurveyDetail> outdoor = new ArrayList<>();
    private int resident = 0;
    private User surveyor = TESTER;
    private Building building = DEFAULT_BUILDING;

    public SurveyBuilder() {
        this(TESTER);
    }

    public SurveyBuilder(User surveyor) {
        this.surveyor = surveyor;
    }

    public SurveyBuilder setBuilding(Building building) {
        this.building = building;
        return this;
    }

    public SurveyBuilder setResident(int residentCount) {
        resident = residentCount;
        return this;
    }

    public SurveyBuilder addIndoorDetail(ContainerType containerType, int total, int foundLarvae) {
        indoor.add(SurveyDetail.fromResult(containerType, total, foundLarvae));
        return this;
    }

    public SurveyBuilder addOutdoorDetail(ContainerType containerType, int total, int foundLarvae) {
        outdoor.add(SurveyDetail.fromResult(containerType, total, foundLarvae));
        return this;
    }

    public Survey build() {
        Survey survey = new Survey(surveyor, building);
        survey.setResidentCount(resident);
        survey.setIndoorDetail(indoor);
        survey.setOutdoorDetail(outdoor);
        return survey;
    }

}
