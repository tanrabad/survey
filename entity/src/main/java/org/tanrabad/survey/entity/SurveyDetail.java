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

package org.tanrabad.survey.entity;

import org.tanrabad.survey.entity.lookup.ContainerType;

import java.util.UUID;

public class SurveyDetail implements Comparable {
    private final UUID surveyDetailId;
    private final ContainerType containerType;
    private int totalContainer;
    private int foundLarvaContainer;

    public SurveyDetail(UUID surveyDetailId, ContainerType containerType, int totalContainer, int foundLarvaContainer) {
        this.containerType = containerType;
        this.surveyDetailId = surveyDetailId;
        setContainerCount(totalContainer, foundLarvaContainer);
    }

    public void setContainerCount(int total, int found) {
        if (found > total)
            throw new ContainerFoundLarvaOverTotalException();
        this.totalContainer = total;
        this.foundLarvaContainer = found;
    }

    public UUID getId() {
        return surveyDetailId;
    }

    public ContainerType getContainerType() {
        return containerType;
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
    public int hashCode() {
        int result = surveyDetailId.hashCode();
        result = 31 * result + containerType.hashCode();
        result = 31 * result + totalContainer;
        result = 31 * result + foundLarvaContainer;
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SurveyDetail that = (SurveyDetail) o;

        if (totalContainer != that.totalContainer) return false;
        if (foundLarvaContainer != that.foundLarvaContainer) return false;
        if (containerType != null ? !containerType.equals(that.containerType) : that.containerType != null)
            return false;
        return surveyDetailId != null
                ? surveyDetailId.equals(that.surveyDetailId)
                : that.surveyDetailId == null;
    }

    @Override
    public String toString() {
        return "SurveyDetail{"
                + "containerType=" + containerType
                + ", totalContainer=" + totalContainer
                + ", foundLarvaContainer=" + foundLarvaContainer
                + ", surveyDetailId=" + surveyDetailId
                + '}';
    }

    @Override
    public int compareTo(Object o) {
        SurveyDetail other = (SurveyDetail) o;
        if (foundLarvaContainer > other.foundLarvaContainer) {
            return 1;
        } else if (foundLarvaContainer < other.foundLarvaContainer) {
            return -1;
        } else {
            return 0;
        }
    }

    public static class ContainerFoundLarvaOverTotalException extends RuntimeException {
    }
}
