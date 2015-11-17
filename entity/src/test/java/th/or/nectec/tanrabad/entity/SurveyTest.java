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

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class SurveyTest {

    private final User user1 = User.fromUsername("Tom50");
    private final Building building1 = Building.withName("โรงเรียนเซนต์เมรี่");
    private final int resident = 2250;
    private final Survey survey1 = new Survey(user1, building1);

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
        Building building2 = Building.withName("โรงเรียนดอนบอสโก");
        survey1.setSurveyBuilding(building2);
        assertEquals(building2, survey1.getSurveyBuilding());
    }

    @Test
    public void testGetUser() {
        assertEquals(user1, survey1.getUser());
    }

    @Test
    public void testSetThenGetUser() {
        User user2 = User.fromUsername("Sara2");
        survey1.setUser(user2);
        assertEquals(user2, survey1.getUser());
    }
}