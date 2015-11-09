/*
 * Copyright (c) 2015  NECTEC
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

package th.or.nectec.tanrabad.domain;

import th.or.nectec.tanrabad.entity.Building;

import java.util.ArrayList;

public class Survey {
    private TRBUser user;
    private Building surveyBuilding;
    private int residentCount;
    private ArrayList<SurveyDetail> indoorDetails;
    private ArrayList<SurveyDetail> outdoorDetails;

    public Survey(TRBUser user, Building surveyBuilding) {
        this.user = user;
        this.surveyBuilding = surveyBuilding;
    }

    public void setResidentCount(int residentCount) {
        this.residentCount = residentCount;
    }

    public void setIndoorDetail(ArrayList<SurveyDetail> indoorDetails) {
        this.indoorDetails = indoorDetails;
    }

    public void setOutdoorDetail(ArrayList<SurveyDetail> outdoorDetails) {
        this.outdoorDetails = outdoorDetails;
    }
}
