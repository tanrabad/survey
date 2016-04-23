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

package org.tanrabad.survey.entity.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.tanrabad.survey.entity.Building;
import org.tanrabad.survey.entity.Survey;
import org.tanrabad.survey.entity.SurveyDetail;
import org.tanrabad.survey.entity.User;
import org.tanrabad.survey.entity.lookup.ContainerType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)

public class SurveyIndoorOutdoorDetailTest {
    private final Survey survey1 = new Survey(UUID.randomUUID(),
            User.fromUsername("Janie09"),
            Building.withName("โรงพยาบาลเซนต์เมรี่"));

    @Test
    public void testSetThenGetIndoorDetail() {
        survey1.setIndoorDetail(surveyDetails1());
        assertEquals(surveyDetails1(), survey1.getIndoorDetail());
    }

    private List<SurveyDetail> surveyDetails1() {
        ContainerType containerType1 = new ContainerType(1, "น้ำใช้");
        ContainerType containerType2 = new ContainerType(8, "กากใบพืช");
        ContainerType containerType3 = new ContainerType(7, "ยางรถยนต์เก่า");

        SurveyDetail detail1 = new SurveyDetail(UUID.nameUUIDFromBytes("1".getBytes()), containerType1, 5, 1);
        SurveyDetail detail2 = new SurveyDetail(UUID.nameUUIDFromBytes("2".getBytes()), containerType2, 4, 0);
        SurveyDetail detail3 = new SurveyDetail(UUID.nameUUIDFromBytes("3".getBytes()), containerType3, 4, 2);

        ArrayList<SurveyDetail> detailArrayList = new ArrayList<>();
        detailArrayList.add(detail1);
        detailArrayList.add(detail2);
        detailArrayList.add(detail3);
        return detailArrayList;
    }

    @Test
    public void testSetThenGetOutdoorDetail() {
        survey1.setOutdoorDetail(surveyDetails1());
        assertEquals(surveyDetails1(), survey1.getOutdoorDetail());
    }
}
