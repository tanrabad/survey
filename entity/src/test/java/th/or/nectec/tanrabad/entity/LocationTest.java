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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import th.or.nectec.tanrabad.entity.field.Location;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


public class LocationTest {
    private static final double DELTA = 0.0001;
    private static final double latitude1 = 14.078606;
    private static final double longitude1 = 100.603120;
    @Rule
    public final ExpectedException exception = ExpectedException.none();
    private final Location location = new Location(latitude1, longitude1);

    @Test
    public void testGetLatitude() {
        assertEquals(latitude1, location.getLatitude(), DELTA);
    }

    @Test
    public void testGetLongitude() {
        assertEquals(longitude1, location.getLongitude(), DELTA);
    }

    @Test
    public void locationWithDifferentLatitudeMustNotEquals() {
        Location anotherLocation = new Location(15.078606, longitude1);

        assertNotEquals(location, anotherLocation);
    }

    @Test
    public void locationWithDifferentLongitudeMustNotEquals() {
        Location anotherLocation = new Location(latitude1, 179.603120);

        assertNotEquals(location, anotherLocation);
    }

    @Test
    public void locationTheSameLatitudeLongitudeMustEquals() {
        Location sameLocation = new Location(latitude1, longitude1);

        assertEquals(location, sameLocation);
    }

    @Test
    public void testOutOfRangeLatitude() throws Exception {
        new Location(89, 0);
        new Location(90, 0);
        new Location(-89, 0);
        new Location(-90, 0);

        exception.expect(IllegalArgumentException.class);
        new Location(-90.1, 0);
    }

    @Test
    public void testOutOfRangeLongitude() throws Exception {
        new Location(0, 179);
        new Location(0, 180);
        new Location(0, -179);
        new Location(0, -180);

        exception.expect(IllegalArgumentException.class);
        new Location(0, 180.1f);


    }
}
