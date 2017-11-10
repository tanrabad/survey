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

package org.tanrabad.survey.nearby.distance;

import org.junit.Test;

import org.tanrabad.survey.nearby.distance.PlanarDistance;
import org.tanrabad.survey.entity.field.Location;

import static org.junit.Assert.assertEquals;

public class PlanarDistanceTest {

    private static final int DELTA = 5;

    @Test
    public void testDistanceCalculatePlanar() throws Exception {
        Location startLocation = new Location(40.6892, -74.0444);
        Location destinationLocation = new Location(39.7802, -74.9453);

        PlanarDistance planarDistance = new PlanarDistance();

        assertEquals(125, planarDistance.calculate(startLocation, destinationLocation), DELTA);
    }
}
