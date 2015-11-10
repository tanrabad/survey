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


public class SurveyDetail {

    private final ContainerType containerType;
    private int totalContainer;
    private int foundLarvaContainer;

    public SurveyDetail(ContainerType containerType, int totalContainer, int foundLarvaContainer) {
        this.containerType = containerType;
        setContainerCount(totalContainer, foundLarvaContainer);
    }

    private void setContainerCount(int total, int found) {
        if (found > total)
            throw new ContainerFoundLarvaOverTotalException();
        this.totalContainer = total;
        this.foundLarvaContainer = found;
    }

    public static SurveyDetail fromResult(ContainerType containerType, int total, int found) {
        return new SurveyDetail(containerType, total, found);
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
    public int hashCode() {
        int result = containerType.hashCode();
        result = 31 * result + totalContainer;
        result = 31 * result + foundLarvaContainer;
        return result;
    }

    public class ContainerFoundLarvaOverTotalException extends RuntimeException {
    }
}