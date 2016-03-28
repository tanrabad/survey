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

package th.or.nectec.tanrabad.entity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import th.or.nectec.tanrabad.entity.field.Location;
import th.or.nectec.tanrabad.entity.lookup.ContainerType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class SurveyTest {

    private final User user1 = User.fromUsername("Tom50");
    private final Building building = Building.withName("โรงเรียนเซนต์เมรี่");
    private final Survey survey1 = new Survey(UUID.randomUUID(), user1, building);
    private final ContainerType drinkWater = new ContainerType(1, "drinkWater");
    private final ContainerType water = new ContainerType(2, "water");
    private final Location location = new Location(14.078606, 100.603120);

    @Test
    public void testGetUser() {
        assertEquals(user1, survey1.getUser());
    }

    @Test
    public void testSetThenGetResidentCount() {
        survey1.setResidentCount(2250);
        assertEquals(2250, survey1.getResidentCount());
    }

    @Test
    public void testSetThenGetSurveyBuilding() {
        assertEquals(building, survey1.getSurveyBuilding());
    }

    @Test
    public void testSetThenGetSurveyLocation() {
        survey1.setLocation(location);
        assertEquals(location, survey1.getLocation());
    }

    @Test
    public void testIsFoundLarvaeMustFalse() throws Exception {
        List<SurveyDetail> indoor2 = new ArrayList<>();
        indoor2.add(new SurveyDetail(UUID.randomUUID(), drinkWater, 2, 0));

        List<SurveyDetail> outdoor2 = new ArrayList<>();
        outdoor2.add(new SurveyDetail(UUID.randomUUID(), drinkWater, 5, 0));
        survey1.setIndoorDetail(indoor2);
        survey1.setOutdoorDetail(outdoor2);

        assertFalse(survey1.isFoundLarvae());
    }

    @Test
    public void testIsFoundLarvaeMustTrue() throws Exception {
        List<SurveyDetail> indoor2 = new ArrayList<>();
        indoor2.add(new SurveyDetail(UUID.randomUUID(), drinkWater, 2, 2));

        survey1.setIndoorDetail(indoor2);

        assertTrue(survey1.isFoundLarvae());
    }

    @Test
    public void testIsFoundLarvaeMustTrue2() throws Exception {
        List<SurveyDetail> indoor2 = new ArrayList<>();
        indoor2.add(new SurveyDetail(UUID.randomUUID(), drinkWater, 2, 2));

        List<SurveyDetail> outdoor2 = new ArrayList<>();
        outdoor2.add(new SurveyDetail(UUID.randomUUID(), drinkWater, 5, 0));
        outdoor2.add(new SurveyDetail(UUID.randomUUID(), water, 5, 2));

        survey1.setIndoorDetail(indoor2);
        survey1.setOutdoorDetail(outdoor2);

        assertTrue(survey1.isFoundLarvae());
    }
}
