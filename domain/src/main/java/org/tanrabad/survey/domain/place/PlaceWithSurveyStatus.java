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

package org.tanrabad.survey.domain.place;

import org.tanrabad.survey.entity.Place;


public class PlaceWithSurveyStatus {
    private final Place place;
    private final boolean isSurvey;

    public PlaceWithSurveyStatus(Place place, boolean isSurvey) {
        this.place = place;
        this.isSurvey = isSurvey;
    }

    @Override
    public int hashCode() {
        int result = place.hashCode();
        result = 31 * result + (isSurvey ? 1 : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlaceWithSurveyStatus that = (PlaceWithSurveyStatus) o;

        return isSurvey == that.isSurvey && place.equals(that.place);
    }
}
