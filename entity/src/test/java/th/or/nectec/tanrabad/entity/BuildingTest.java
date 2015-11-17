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

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(JUnit4.class)
public class BuildingTest {

    public static final UUID BUILDING_UUID = UUID.randomUUID();
    public static final String BUILDING_NAME = "โบสถ์ใหญ่";
    private final Place place = Place.withName("วิหารเซนต์เมรี่");
    private final Building building1 = new Building(BUILDING_UUID, BUILDING_NAME);
    private Location location = new Location(14.078606, 100.603120);

    @Test
    public void testWithName() {
        Building building = Building.withName(BUILDING_NAME);
        assertEquals(BUILDING_NAME, building.getName());
    }

    @Test
    public void testSetThenGetPlace() {
        building1.setPlace(place);
        assertEquals(place, building1.getPlace());
    }

    @Test
    public void testGetName() {
        assertEquals(BUILDING_NAME, building1.getName());
    }

    @Test
    public void testSetThenGetName() {
        building1.setName("โบสถ์เล็ก");
        assertEquals("โบสถ์เล็ก", building1.getName());
    }

    @Test
    public void testGetId() {
        assertEquals(BUILDING_UUID, building1.getId());
    }

    @Test
    public void testSetThenGetBuildingLocation() {
        building1.setLocation(location);
        assertEquals(location, building1.getLocation());
    }

    @Test
    public void buildingWithDifferentIdMustNotEqual() {
        building1.setPlace(place);
        Building building2 = new Building(UUID.randomUUID(), BUILDING_NAME);
        building2.setPlace(place);
        assertNotEquals(building1, building2);
    }

    @Test
    public void buildingWithDifferentNameMustNotEqual() {
        building1.setPlace(place);
        Building building2 = new Building(BUILDING_UUID, "โบสถ์เล็ก");
        building2.setPlace(place);
        assertNotEquals(building1, building2);
    }

    @Test
    public void buildingWithDifferentPlaceMustNotEqual() {
        building1.setPlace(place);
        Building building2 = new Building(BUILDING_UUID, BUILDING_NAME);
        building2.setPlace(Place.withName("โรงเรียนเซนต์เมรี่"));
        assertNotEquals(building1, building2);
    }

    @Test
    public void buildingWithTheSameNameAndIdAndPlaceMustEqual() {
        building1.setPlace(place);
        Building building2 = new Building(BUILDING_UUID, BUILDING_NAME);
        building2.setPlace(place);
        assertEquals(building1, building2);
    }
}