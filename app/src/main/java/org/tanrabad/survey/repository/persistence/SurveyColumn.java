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

package org.tanrabad.survey.repository.persistence;

public class SurveyColumn {

    public static final String ID = "survey_id";
    public static final String BUILDING_ID = "building_id";
    public static final String PERSON_COUNT = "person_count";
    public static final String SURVEYOR = "surveyor";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String CREATE_TIME = "create_time";
    public static final String UPDATE_TIME = "update_time";
    public static final String CHANGED_STATUS = "changed_status";


    public static String[] wildcard() {
        return new String[]{ID, BUILDING_ID, PERSON_COUNT, SURVEYOR, LATITUDE, LONGITUDE,
                CREATE_TIME, UPDATE_TIME, CHANGED_STATUS};
    }
}
