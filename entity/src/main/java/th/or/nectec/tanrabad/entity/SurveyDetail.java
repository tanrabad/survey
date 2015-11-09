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

    private final Container container;
    private final int total;
    private final int found;

    public SurveyDetail(Container container, int total, int found) {
        this.container = container;
        this.total = total;
        this.found = found;
    }

    public static SurveyDetail fromResult(Container container, int total, int found) {
        return new SurveyDetail(container, total, found);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SurveyDetail that = (SurveyDetail) o;
        return total == that.total && found == that.found && container.equals(that.container);
    }

    @Override
    public int hashCode() {
        int result = container.hashCode();
        result = 31 * result + total;
        result = 31 * result + found;
        return result;
    }
}
