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
public class PlaceTest {

    public static final UUID BANGPHAI_UUID = UUID.randomUUID();
    public static final String BANGPHAI_NAME = "บางไผ่";
    public static final int BANGPHAI_TYPE = Place.TYPE_VILLAGE_COMMUNITY;
    private final Place place1 = new Place(BANGPHAI_UUID, BANGPHAI_NAME);
    private final Place place2 = new Place(BANGPHAI_UUID, BANGPHAI_NAME);

    @Test
    public void testWithName() throws Exception {
        Place place = Place.withName(BANGPHAI_NAME);
        assertEquals(BANGPHAI_NAME, place.getName());
    }

    @Test
    public void testGetId() throws Exception {
        assertEquals(BANGPHAI_UUID, place1.getId());
    }

    @Test
    public void testGetName() throws Exception {
        assertEquals(BANGPHAI_NAME, place1.getName());
    }

    @Test
    public void testSetThenGetPlaceName() throws Exception {
        place1.setName("บางโพธิ์");
        assertEquals("บางโพธิ์", place1.getName());
    }

    @Test
    public void testSetThenGetPlaceType() throws Exception {
        place1.setType(Place.TYPE_SCHOOL);
        assertEquals(Place.TYPE_SCHOOL, place1.getType());
    }

    @Test
    public void placeWithDifferentNameMustNotEqual() throws Exception {
        place1.setType(BANGPHAI_TYPE);
        place2.setType(place1.getType());
        place2.setName("บางโพธิ์");
        assertNotEquals(place1, place2);
    }

    @Test
    public void placeWithDifferentTypeMustNotEqual() throws Exception {
        place1.setType(Place.TYPE_FACTORY);
        place2.setType(Place.TYPE_SCHOOL);
        assertNotEquals(place1, place2);
    }

    @Test
    public void placeWithTheSameNameAndTypeMustEqual() throws Exception {
        place1.setType(BANGPHAI_TYPE);
        place2.setType(BANGPHAI_TYPE);
        assertEquals(place1, place2);
    }
}
