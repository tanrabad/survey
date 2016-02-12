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

package th.or.nectec.tanrabad.domain.entomology;

import th.or.nectec.tanrabad.entity.Survey;
import th.or.nectec.tanrabad.entity.SurveyDetail;
import th.or.nectec.tanrabad.entity.lookup.ContainerType;

import java.util.*;

public class KeyContainer {

    private final List<Survey> surveys;
    protected List<SurveyDetail> indoorKey;
    protected List<SurveyDetail> outdoorKey;
    Map<ContainerType, Integer> indoorMap = new HashMap<>();
    Map<ContainerType, Integer> outdoorMap = new HashMap<>();

    public KeyContainer(Survey survey) {
        surveys = new ArrayList<>();
        surveys.add(survey);
    }

    public KeyContainer(ArrayList<Survey> surveys) {
        this.surveys = surveys;
    }

    public ContainerType indoorNumberOne() {
        return getAtNumber(indoorKey, 1);
    }

    private ContainerType getAtNumber(List<SurveyDetail> sortedKeyContainer, int number) {
        try {
            return sortedKeyContainer.get(sortedKeyContainer.size() - number).getContainerType();
        } catch (ArrayIndexOutOfBoundsException aob) {
            return null;
        }
    }

    public ContainerType indoorNumberTwo() {
        return getAtNumber(indoorKey, 2);
    }

    public ContainerType indoorNumberThree() {
        return getAtNumber(indoorKey, 3);
    }

    public void calculate() {
        for (Survey survey : surveys) {
            for (SurveyDetail surveyDetail : survey.getIndoorDetail()) {
                ContainerType containerType = surveyDetail.getContainerType();
                if (!indoorMap.containsKey(containerType))
                    indoorMap.put(containerType, 0);
                Integer count = indoorMap.get(containerType);
                indoorMap.put(containerType, count + surveyDetail.getFoundLarvaContainer());
            }
            for (SurveyDetail surveyDetail : survey.getOutdoorDetail()) {
                ContainerType containerType = surveyDetail.getContainerType();
                if (!outdoorMap.containsKey(containerType))
                    outdoorMap.put(containerType, 0);
                Integer count = outdoorMap.get(containerType);
                outdoorMap.put(containerType, count + surveyDetail.getFoundLarvaContainer());
            }
        }

        Iterator<Map.Entry<ContainerType, Integer>> indoorItr = indoorMap.entrySet().iterator();
        indoorKey = new ArrayList<>();
        while (indoorItr.hasNext()) {
            Map.Entry<ContainerType, Integer> containerTypeIntegerEntry = indoorItr.next();
            indoorKey.add(new SurveyDetail(UUID.randomUUID(), containerTypeIntegerEntry.getKey(),
                    containerTypeIntegerEntry.getValue(), containerTypeIntegerEntry.getValue()));
        }
        Collections.sort(indoorKey);

        Iterator<Map.Entry<ContainerType, Integer>> outdoorItr = outdoorMap.entrySet().iterator();
        outdoorKey = new ArrayList<>();
        while (outdoorItr.hasNext()) {

            Map.Entry<ContainerType, Integer> containerTypeIntegerEntry = outdoorItr.next();
            outdoorKey.add(new SurveyDetail(UUID.randomUUID(), containerTypeIntegerEntry.getKey(),
                    containerTypeIntegerEntry.getValue(), containerTypeIntegerEntry.getValue()));
        }
        Collections.sort(outdoorKey);
    }

    public ContainerType outdoorNumberOne() {
        return getAtNumber(outdoorKey, 1);
    }

    public ContainerType outdoorNumberTwo() {
        return getAtNumber(outdoorKey, 2);
    }

    public ContainerType outdoorNumberThree() {
        return getAtNumber(outdoorKey, 3);
    }

}
