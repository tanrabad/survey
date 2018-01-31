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

public class PlaceColumn {

    public static final String ID = "place_id";
    public static final String NAME = "name";
    public static final String TYPE_ID = "type_id";
    public static final String SUBTYPE_ID = "subtype_id";
    public static final String SUBDISTRICT_CODE = "subdistrict_code";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String UPDATE_BY = "update_by";
    public static final String CHANGED_STATUS = "changed_status";
    public static final String UPDATE_TIME = "update_time";
    public static final String IS_TYPE_EDITED = "is_type_edited";


    public static String[] wildcard() {
        return new String[]{
                ID, NAME, SUBTYPE_ID,
                SUBDISTRICT_CODE, LATITUDE, LONGITUDE,
            UPDATE_BY, UPDATE_TIME, CHANGED_STATUS, IS_TYPE_EDITED};
    }
}
