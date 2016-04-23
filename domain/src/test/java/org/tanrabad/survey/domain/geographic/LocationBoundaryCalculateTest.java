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

package org.tanrabad.survey.domain.geographic;

import org.junit.Test;

import th.or.nectec.tanrabad.entity.field.Location;

import static org.junit.Assert.assertEquals;

public class LocationBoundaryCalculateTest {

    private static final int DISTANCE_IN_KM = 100;
    private final Location myLocation = new Location(40.6892, -74.0444);

    @Test
    public void testMinimumLocation() throws Exception {
        Location minimumLocation = new Location(39.78477101124205, -74.93966316260665);
        FilterBoundaryCalculate filterBoundaryCalculate = new FilterBoundaryCalculate();

        assertEquals(minimumLocation, filterBoundaryCalculate.getMinLocation(myLocation, DISTANCE_IN_KM));
    }

    @Test
    public void testMaximumLocation() throws Exception {
        Location maximumLocation = new Location(41.59362898875795, -73.14913683739334);
        FilterBoundaryCalculate filterBoundaryCalculate = new FilterBoundaryCalculate();

        assertEquals(maximumLocation, filterBoundaryCalculate.getMaxLocation(myLocation, DISTANCE_IN_KM));
    }
}
