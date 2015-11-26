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

import th.or.nectec.tanrabad.entity.ContainerType;
import th.or.nectec.tanrabad.entity.Survey;
import th.or.nectec.tanrabad.entity.SurveyDetail;

import java.util.*;

public class KeyContainer {

    private final List<Survey> surveys;
    Map<ContainerType,Integer> IndoorMap = new HashMap<>();
    Map<ContainerType,Integer> outdoorMap = new HashMap<>();
    private List<SurveyDetail> indoorKey;

    public KeyContainer(Survey survey) {
        surveys = new ArrayList<>();
        surveys.add(survey);
    }

    public ContainerType indoorNumberOne() {
        return indoorKey.get(indoorKey.size() - 1).getContainerType();
    }

    public ContainerType indoorNumberTwo() {
        return indoorKey.get(indoorKey.size() - 2).getContainerType();
    }

    public ContainerType indoorNumberThree() {
        return indoorKey.get(indoorKey.size() - 3).getContainerType();
    }

    public void calculate() {
        for (Survey survey : surveys) {
            for (SurveyDetail surveyDetail : survey.getIndoorDetail()) {
                ContainerType containerType = surveyDetail.getContainerType();
                if (!IndoorMap.containsKey(containerType))
                    IndoorMap.put(containerType, 0);
                Integer count = IndoorMap.get(containerType);
                IndoorMap.put(containerType, count + surveyDetail.getFoundLarvaContainer());
            }
            for (SurveyDetail surveyDetail : survey.getOutdoorDetail()) {
                ContainerType containerType = surveyDetail.getContainerType();
                if (!outdoorMap.containsKey(containerType))
                    outdoorMap.put(containerType, 0);
                Integer count = outdoorMap.get(containerType);
                outdoorMap.put(containerType, count + surveyDetail.getFoundLarvaContainer());
            }
        }

        Iterator<Map.Entry<ContainerType,Integer>> iterator = IndoorMap.entrySet().iterator();
        indoorKey = new ArrayList<>();
        while (iterator.hasNext()) {
            Map.Entry<ContainerType,Integer> containerTypeIntegerEntry = iterator.next();
            indoorKey.add(new SurveyDetail(containerTypeIntegerEntry.getKey(), containerTypeIntegerEntry.getValue(), containerTypeIntegerEntry.getValue()));
        }
        Collections.sort(indoorKey);
    }

    public ContainerType outdoorNumberTwo() {
        return null;
    }
}
