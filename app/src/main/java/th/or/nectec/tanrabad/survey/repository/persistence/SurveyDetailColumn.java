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

package th.or.nectec.tanrabad.survey.repository.persistence;

public class SurveyDetailColumn {

    public static final String ID = "detail_id";
    public static final String SURVEY_ID = "survey_id";
    public static final String CONTAINER_TYPE_ID = "container_type_id";
    public static final String CONTAINER_LOCATION_ID = "container_location_id";
    public static final String CONTAINER_COUNT = "container_count";
    public static final String CONTAINER_HAVE_LARVA = "container_have_larva";

    public static String[] wildcard() {
        return new String[]{
                ID, SURVEY_ID,
                CONTAINER_TYPE_ID, CONTAINER_LOCATION_ID, CONTAINER_COUNT, CONTAINER_HAVE_LARVA};
    }
}
