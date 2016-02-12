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

package th.or.nectec.tanrabad.domain.building;

import th.or.nectec.tanrabad.entity.Building;


public class BuildingWithSurveyStatus {
    Building building;
    boolean isSurvey;

    public BuildingWithSurveyStatus(Building building, boolean isSurvey) {
        this.building = building;
        this.isSurvey = isSurvey;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public boolean isSurvey() {
        return isSurvey;
    }

    public void setIsSurvey(boolean isSurvey) {
        this.isSurvey = isSurvey;
    }

    @Override
    public int hashCode() {
        int result = building != null ? building.hashCode() : 0;
        result = 31 * result + (isSurvey ? 1 : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BuildingWithSurveyStatus that = (BuildingWithSurveyStatus) o;

        return isSurvey == that.isSurvey && !(building != null
                ? !building.equals(that.building)
                : that.building != null);

    }

    @Override
    public String toString() {
        return "SurveyBuilding{" +
                "building=" + building +
                ", isSurvey=" + isSurvey +
                '}';
    }
}
