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

public class BuildingWithSurveyStatus implements Comparable<BuildingWithSurveyStatus> {
    public final Building building;
    public final boolean isSurvey;

    public BuildingWithSurveyStatus(Building building, boolean isSurvey) {
        this.building = building;
        this.isSurvey = isSurvey;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        BuildingWithSurveyStatus that = (BuildingWithSurveyStatus) other;

        if (isSurvey != that.isSurvey) return false;
        return building.equals(that.building);
    }

    @Override
    public int compareTo(BuildingWithSurveyStatus that) {
        if (this.building.compareTo(that.building) < 0) {
            return -1;
        } else if (this.building.compareTo(that.building) > 0) {
            return 1;
        }
        return 0;
    }
}
