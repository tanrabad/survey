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

package th.or.nectec.tanrabad.domain.geographic;

import th.or.nectec.tanrabad.entity.field.Location;

public class PlanarDistance implements DistanceCalculator {
    private static final double RADIUS = 6371;

    @Override
    public double calculate(Location currentLocation, Location targetLocation) {
        double lat1 = currentLocation.getLatitude();
        double lon1 = currentLocation.getLongitude();
        double lat2 = targetLocation.getLatitude();
        double lon2 = targetLocation.getLongitude();

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double x;
        double y;
        double d;

        x = dLon * Math.cos(Math.toRadians(lat1));
        y = dLat;
        d = RADIUS * Math.sqrt(x * x + y * y);

        return d;
    }
}
