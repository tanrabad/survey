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

package th.or.nectec.tanrabad.domain.geographic;

import org.junit.Test;
import th.or.nectec.tanrabad.entity.Location;

import static org.junit.Assert.assertEquals;

public class CoordinateLocationCalculateTest {

    @Test
    public void testNewMaximumLocation() throws Exception {
        Location myLocation = new Location(40.6892, -74.0444);
        Location newMaximumLocation = new Location(41.322039734494695, -73.87166829177177);
        int distanceInKm = 100;

        CoordinateLocationCalculate coordinateLocationCalculate = new CoordinateLocationCalculate();
        assertEquals(coordinateLocationCalculate.getNewMaxLocation(myLocation, distanceInKm), newMaximumLocation );
    }

    @Test
    public void testNewMinimumLocation() throws Exception {
        Location myLocation = new Location(40.6892, -74.0444);
        Location newMinimumLocation = new Location(40.05029095061978, -74.21719530257553);
        int distanceInKm = 100;

        CoordinateLocationCalculate coordinateLocationCalculate = new CoordinateLocationCalculate();
        assertEquals(coordinateLocationCalculate.getNewMinLocation(myLocation, distanceInKm), newMinimumLocation );
    }
}
