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

package th.or.nectec.tanrabad.entity;

import org.joda.time.DateTime;

import java.util.ArrayList;

public class Survey {
    private User user;
    private Building surveyBuilding;
    private int residentCount;
    private ArrayList<SurveyDetail> indoorDetails;
    private ArrayList<SurveyDetail> outdoorDetails;
    private DateTime startTimestamp;
    private DateTime finishTimestamp;
    private Location location;

    public Survey(User user, Building surveyBuilding) {
        this.user = user;
        this.surveyBuilding = surveyBuilding;
    }

    public void startSurvey() {
        setStartTimestamp(DateTime.now());
    }

    public void finishSurvey() {
        setFinishTimestamp(DateTime.now());
    }

    public DateTime getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(DateTime startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public DateTime getFinishTimestamp() {
        return finishTimestamp;
    }

    public void setFinishTimestamp(DateTime finishTimestamp) {
        this.finishTimestamp = finishTimestamp;
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

    public Location getLocation() {
        return location;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Survey survey = (Survey) o;

        if (residentCount != survey.residentCount) return false;
        if (!user.equals(survey.user)) return false;
        if (!surveyBuilding.equals(survey.surveyBuilding)) return false;
        if (indoorDetails != null ? !indoorDetails.equals(survey.indoorDetails) : survey.indoorDetails != null)
            return false;
        if (outdoorDetails != null ? !outdoorDetails.equals(survey.outdoorDetails) : survey.outdoorDetails != null)
            return false;
        return !(startTimestamp != null ? !startTimestamp.equals(survey.startTimestamp) : survey.startTimestamp != null);

    }

    @Override
    public int hashCode() {
        int result = user.hashCode();
        result = 31 * result + surveyBuilding.hashCode();
        result = 31 * result + residentCount;
        result = 31 * result + (indoorDetails != null ? indoorDetails.hashCode() : 0);
        result = 31 * result + (outdoorDetails != null ? outdoorDetails.hashCode() : 0);
        result = 31 * result + (startTimestamp != null ? startTimestamp.hashCode() : 0);
        return result;
    }
}
