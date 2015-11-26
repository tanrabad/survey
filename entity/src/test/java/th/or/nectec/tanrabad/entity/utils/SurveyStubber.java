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

public class SurveyStubber {

    private static final ContainerType น้ำใช้ = new ContainerType(1, "น้ำใช้");
    private static final ContainerType น้ำดื่ม = new ContainerType(2, "น้ำดื่ม");
    private static final User surveyor = User.fromUsername("surveyor");

    public static Survey getSurveyWithoutLarvae(Building building) {

        List<SurveyDetail> indoor = new ArrayList<>();
        indoor.add(SurveyDetail.fromResult(น้ำใช้, 20, 0));


        Survey survey = new Survey(surveyor, building);
        survey.setIndoorDetail(indoor);
        return survey;
    }

    public static Survey getSurveyWithLarvae(Building building) {
        List<SurveyDetail> indoor = new ArrayList<>();
        indoor.add(SurveyDetail.fromResult(น้ำใช้, 5, 2));

        List<SurveyDetail> outdoor = new ArrayList<>();
        outdoor.add(SurveyDetail.fromResult(น้ำดื่ม, 10, 2));

        Survey survey = new Survey(surveyor, building);
        survey.setIndoorDetail(indoor);
        survey.setOutdoorDetail(outdoor);
        return survey;
    }

    public static Survey getSurveyWithResult(int total, int foundLarvae) {
        return getSurveyWithResult(น้ำใช้, total, foundLarvae);
    }

    public static Survey getSurveyWithResult(ContainerType containerType, int total, int foundLarvae) {
        List<SurveyDetail> indoor = new ArrayList<>();
        indoor.add(SurveyDetail.fromResult(containerType, total, foundLarvae));

        Survey survey = new Survey(surveyor, Building.withName("1"));
        survey.setIndoorDetail(indoor);
        return survey;
    }

    public static Survey getWithIndoorOutdoorResult(int indoorTotal, int indoorFound, int outdoorTotal, int outdoorFound) {
        List<SurveyDetail> indoor = new ArrayList<>();
        indoor.add(SurveyDetail.fromResult(น้ำใช้, indoorTotal, indoorFound));
        List<SurveyDetail> outdoor = new ArrayList<>();
        outdoor.add(SurveyDetail.fromResult(น้ำดื่ม, outdoorTotal, outdoorFound));

        Survey survey = new Survey(surveyor, Building.withName("1"));
        survey.setIndoorDetail(indoor);
        survey.setOutdoorDetail(outdoor);
        return survey;
    }
}
