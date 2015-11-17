/*
 * Copyright (c) 2015 NECTEC
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


public class LocationTest {
    private final double latitude1 = 14.078606;
    private final double longitude1 = 100.603120;
    private final Location location1 = new Location(latitude1, longitude1);

    @Test
    public void testGetLatitude()  {
        assertEquals(14.078606, location1.getLatitude(), 0.0001);
    }

    @Test
    public void testGetLongitude()  {
        assertEquals(100.603120, location1.getLongitude(), 0.0001);
    }

    @Test
    public void locationWithDifferentLatitudeMustNotEquals()  {
        Location location2 = new Location(15.078606, longitude1);
        assertNotEquals(location1, location2);
    }

    @Test
    public void locationWithDifferentLongitudeMustNotEquals()  {
        Location location2 = new Location(latitude1, 200.603120);
        assertNotEquals(location1, location2);
    }

    @Test
    public void locationTheSameLatitudeLongitudeMustEquals()  {
        Location location2 = new Location(latitude1, longitude1);
        assertEquals(location1, location2);
    }}