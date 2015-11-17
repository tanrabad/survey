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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(JUnit4.class)
public class SurveyTest {

    private final User user1 = User.fromUsername("Tom50");
    private final User user2 = User.fromUsername("Sara2");
    private final Building building1 = Building.withName("โรงเรียนเซนต์เมรี่");
    private final Building building2 = Building.withName("โรงเรียนดอนบอสโก");
    private final int resident = 2250;
    private final Survey survey1 = new Survey(user1, building1);
    private final ContainerType containerType1 = new ContainerType(1, "น้ำใช้");
    private final ContainerType containerType2 = new ContainerType(8, "กากใบพืช");
    private final ContainerType containerType3 = new ContainerType(7, "ยางรถยนต์เก่า");
    private final SurveyDetail detail1 = new SurveyDetail(containerType1, 5, 1);
    private final SurveyDetail detail2 = new SurveyDetail(containerType2, 4, 0);
    private final SurveyDetail detail3 = new SurveyDetail(containerType3, 4, 2);

    @Test
    public void testSetThenGetResidentCount() {
        survey1.setResidentCount(resident);
        assertEquals(resident, survey1.getResidentCount());
    }

    @Test
    public void testGetSurveyBuilding() {
        assertEquals(building1, survey1.getSurveyBuilding());
    }

    @Test
    public void testSetThenGetSurveyBuilding() {
        survey1.setSurveyBuilding(building2);
        assertEquals(building2, survey1.getSurveyBuilding());
    }

    @Test
    public void testGetUser() {
        assertEquals(user1, survey1.getUser());
    }

    @Test
    public void testSetThenGetUser() {
        survey1.setUser(user2);
        assertEquals(user2, survey1.getUser());
    }

    @Test
    public void testSetThenGetIndoorDetail() {
        ArrayList<SurveyDetail> detailArrayList = new ArrayList<>();
        detailArrayList.add(detail1);
        detailArrayList.add(detail2);
        detailArrayList.add(detail3);
        survey1.setIndoorDetail(detailArrayList);
        assertEquals(detailArrayList, survey1.getIndoorDetail());
    }

    @Test
    public void testSetThenGetOutdoorDetail() {
        ArrayList<SurveyDetail> detailArrayList = new ArrayList<>();
        detailArrayList.add(detail1);
        detailArrayList.add(detail2);
        detailArrayList.add(detail3);
        survey1.setOutdoorDetail(detailArrayList);
        assertEquals(detailArrayList, survey1.getOutdoorDetail());
    }

    @Test
    public void surveyWithDifferentUserMustNotEquals() {
        Survey survey2 = new Survey(user2, building1);
        assertNotEquals(survey1, survey2);
    }

    @Test
    public void surveyWithDifferentBuildingMustNotEquals() {
        Survey survey = new Survey(user1, building2);
        assertNotEquals(survey1, survey);
    }

    @Test
    public void surveyWithTheSameSurveyAndBuildingMustEquals() {
        Survey survey = new Survey(user1, building1);
        assertEquals(survey1, survey);
    }
}