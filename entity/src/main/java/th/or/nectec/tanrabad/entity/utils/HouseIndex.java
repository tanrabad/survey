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

import th.or.nectec.tanrabad.entity.Survey;

import java.util.ArrayList;
import java.util.List;

public class HouseIndex {

    private List<Survey> surveys;
    private int foundLarvaeCount;

    public HouseIndex() {
        this(new ArrayList<Survey>());
    }

    public HouseIndex(List<Survey> surveys) {
        this.surveys = surveys;
    }

    public void addSurvey(Survey s1) {
        surveys.add(s1);
    }

    public float calculate() {
        if (surveys.size() == 0)
            throw new IllegalStateException("must have some survey before calculate");

        foundLarvaeCount = 0;
        for (Survey survey : surveys) {
            if (survey.isFoundLarvae()) {
                foundLarvaeCount++;
            }
        }
        return foundLarvaeCount / (float) surveys.size() * 100;
    }

    public int getFoundLarvaeHouse() {
        return foundLarvaeCount;
    }

    public int getTotalSurveyHouse() {
        return surveys.size();
    }
}
