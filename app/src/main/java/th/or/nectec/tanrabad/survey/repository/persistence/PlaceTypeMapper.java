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

import android.util.SparseIntArray;

import static th.or.nectec.tanrabad.entity.lookup.PlaceType.*;

public class PlaceTypeMapper {
    public static final int สำนักงานสาธารณสุขจังหวัด = 1;
    public static final int สำนักงานสาธารณสุขอำเภอ = 2;
    public static final int โรงพยาบาลส่งเสริมสุขภาพตำบล = 3;
    public static final int โรงพยาบาลทั่วไป = 4;
    public static final int โรงพยาบาลชุมชน = 5;
    public static final int โรงพยาบาลสังกัดกระทรวงอื่น = 6;
    public static final int โรงพยาบาลเอกชน = 7;
    public static final int ศูนย์สุขภาพชุมชน = 8;
    public static final int ศูนย์วิชาการ = 9;
    public static final int ชุมชนแออัด = 10;
    public static final int ชุมชนพักอาศัย = 11;
    public static final int ชุมชนพาณิชย์ = 12;
    public static final int วัด = 13;
    public static final int โบสถ์ = 14;
    public static final int มัสยิด = 15;
    public static final int โรงเรียน = 16;
    public static final int โรงงาน = 17;
    private static PlaceTypeMapper instances;
    private SparseIntArray placeSubTypeMapping = new SparseIntArray();

    private PlaceTypeMapper() {
        placeSubTypeMapping.put(สำนักงานสาธารณสุขจังหวัด, HOSPITAL);
        placeSubTypeMapping.put(สำนักงานสาธารณสุขอำเภอ, HOSPITAL);
        placeSubTypeMapping.put(โรงพยาบาลส่งเสริมสุขภาพตำบล, HOSPITAL);
        placeSubTypeMapping.put(โรงพยาบาลทั่วไป, HOSPITAL);
        placeSubTypeMapping.put(โรงพยาบาลชุมชน, HOSPITAL);
        placeSubTypeMapping.put(โรงพยาบาลสังกัดกระทรวงอื่น, HOSPITAL);
        placeSubTypeMapping.put(โรงพยาบาลเอกชน, HOSPITAL);
        placeSubTypeMapping.put(ศูนย์สุขภาพชุมชน, HOSPITAL);
        placeSubTypeMapping.put(ศูนย์วิชาการ, HOSPITAL);
        placeSubTypeMapping.put(ชุมชนแออัด, VILLAGE_COMMUNITY);
        placeSubTypeMapping.put(ชุมชนพักอาศัย, VILLAGE_COMMUNITY);
        placeSubTypeMapping.put(ชุมชนพาณิชย์, VILLAGE_COMMUNITY);
        placeSubTypeMapping.put(วัด, WORSHIP);
        placeSubTypeMapping.put(โบสถ์, WORSHIP);
        placeSubTypeMapping.put(มัสยิด, WORSHIP);
        placeSubTypeMapping.put(โรงเรียน, SCHOOL);
        placeSubTypeMapping.put(โรงงาน, FACTORY);
    }

    public static PlaceTypeMapper getInstance() {
        if (instances == null)
            instances = new PlaceTypeMapper();
        return instances;
    }

    public int findBySubType(int subtypeId) {
        return placeSubTypeMapping.get(subtypeId, subtypeId);
    }

    public int getDefaultPlaceType(int placetypeid) {
        switch (placetypeid) {
            case VILLAGE_COMMUNITY:
                return ชุมชนพักอาศัย;
            case WORSHIP:
                return วัด;
            case SCHOOL:
                return โรงเรียน;
            case HOSPITAL:
                return โรงพยาบาลทั่วไป;
            case FACTORY:
                return โรงงาน;
            default:
                return -1;
        }
    }
}
