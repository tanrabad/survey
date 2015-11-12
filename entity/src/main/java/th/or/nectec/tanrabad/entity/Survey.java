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

package th.or.nectec.tanrabad.entity;

import java.util.ArrayList;

public class Survey {
    private User user;
    private Building surveyBuilding;
    private int residentCount;
    private ArrayList<SurveyDetail> indoorDetails;
    private ArrayList<SurveyDetail> outdoorDetails;
    public Survey(User user, Building surveyBuilding) {
        this.user = user;
        this.surveyBuilding = surveyBuilding;
    }

    @Override
    public String toString() {
        return "Survey{" +
                "user=" + user +
                ", surveyBuilding=" + surveyBuilding +
                ", residentCount=" + residentCount +
                ", indoorDetails=" + indoorDetails +
                ", outdoorDetails=" + outdoorDetails +
                '}';
    }

    public int getResidentCount() {
        return residentCount;
    }

    public void setResidentCount(int residentCount) {
        this.residentCount = residentCount;
    }

    public Building getSurveyBuilding() {
        return surveyBuilding;
    }

    public void setSurveyBuilding(Building surveyBuilding) {
        this.surveyBuilding = surveyBuilding;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<SurveyDetail> getIndoorDetail() {
        return indoorDetails;
    }

    public void setIndoorDetail(ArrayList<SurveyDetail> indoorDetails) {
        this.indoorDetails = indoorDetails;
    }

    public ArrayList<SurveyDetail> getOutdoorDetail() {
        return outdoorDetails;
    }

    public void setOutdoorDetail(ArrayList<SurveyDetail> outdoorDetails) {
        this.outdoorDetails = outdoorDetails;
    }
}
