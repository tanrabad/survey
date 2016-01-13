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

import java.util.UUID;

public class SurveyDetail implements Comparable {
    private final ContainerType containerType;
    private int totalContainer;
    private int foundLarvaContainer;
    private UUID surveyDetailID;

    public SurveyDetail(UUID surveyDetailID, ContainerType containerType, int totalContainer, int foundLarvaContainer) {
        this.containerType = containerType;
        this.surveyDetailID = surveyDetailID;
        setContainerCount(totalContainer, foundLarvaContainer);
    }

    private void setContainerCount(int total, int found) {
        if (found > total)
            throw new ContainerFoundLarvaOverTotalException();
        this.totalContainer = total;
        this.foundLarvaContainer = found;
    }

    public ContainerType getContainerType() {
        return containerType;
    }

    @Override
    public int hashCode() {
        int result = containerType.hashCode();
        result = 31 * result + totalContainer;
        result = 31 * result + foundLarvaContainer;
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        SurveyDetail that = (SurveyDetail) other;
        return totalContainer == that.totalContainer &&
                foundLarvaContainer == that.foundLarvaContainer &&
                containerType.equals(that.containerType);
    }

    @Override
    public String toString() {
        return "SurveyDetail{" +
                "containerType=" + containerType +
                ", totalContainer=" + totalContainer +
                ", foundLarvaContainer=" + foundLarvaContainer +
                '}';
    }

    public int getTotalContainer() {
        return totalContainer;
    }

    public int getFoundLarvaContainer() {
        return foundLarvaContainer;
    }

    public boolean isFoundLarva() {
        return foundLarvaContainer > 0;
    }

    @Override
    public int compareTo(Object o) {
        SurveyDetail other = (SurveyDetail) o;

        if (foundLarvaContainer > other.foundLarvaContainer)
            return 1;
        else if (foundLarvaContainer < other.foundLarvaContainer)
            return -1;
        else
            return 0;

    }

    public UUID getId() {
        return surveyDetailID;
    }

    public class ContainerFoundLarvaOverTotalException extends RuntimeException {
    }
}
