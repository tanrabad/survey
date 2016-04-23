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

import th.or.nectec.tanrabad.entity.field.Location;

public class CoordinateLocationCalculate implements CoordinateLocationCalculator {

    private static final double RADIUS = 6371;

    @Override
    public Location getNewMaxLocation(Location currentLocation, double distanceInKm) {
        double lat1 = currentLocation.getLatitude();
        double lon1 = currentLocation.getLongitude();

        double lat1Radian = Math.toRadians(lat1);
        double lon1Radian = Math.toRadians(lon1);

        double bearing = Math.toRadians(45); //Bearing is 45 degrees converted to radians. brng = (degree*pi)/180

        //d/Radius called angular distance in radians.

        double newMaximumLatitudeRadian = Math.asin(Math.sin(lat1Radian) * Math.cos(distanceInKm / RADIUS)
                + Math.cos(lat1Radian) * Math.sin(distanceInKm / RADIUS) * Math.cos(bearing));

        double newMaximumLongitudeRadian = Math.toRadians(lon1)
                + Math.atan2(Math.sin(bearing) * Math.sin(distanceInKm / RADIUS) * Math.cos(lon1Radian),
                Math.cos(distanceInKm / RADIUS)
                        - Math.sin(lon1Radian) * Math.sin(Math.toRadians(newMaximumLatitudeRadian)));

        double newMaximumLatitude = Math.toDegrees(newMaximumLatitudeRadian);
        double newMaximumLongitude = Math.toDegrees(newMaximumLongitudeRadian);

        return new Location(newMaximumLatitude, newMaximumLongitude);

    }

    @Override
    public Location getNewMinLocation(Location currentLocation, double distanceInKm) {
        double lat1 = currentLocation.getLatitude();
        double lon1 = currentLocation.getLongitude();

        double lat1Radian = Math.toRadians(lat1);
        double lon1Radian = Math.toRadians(lon1);

        double bearing = Math.toRadians(225); //Bearing is 225 degrees converted to radians. bearing = (degree*pi)/180

        double newMinimumLatitudeRadian = Math.asin(Math.sin(lat1Radian) * Math.cos(distanceInKm / RADIUS)
                + Math.cos(lat1Radian) * Math.sin(distanceInKm / RADIUS) * Math.cos(bearing));

        double newMinimumLongitudeRadian = Math.toRadians(lon1) + Math.atan2(Math.sin(bearing)
                        * Math.sin(distanceInKm / RADIUS) * Math.cos(lon1Radian),
                Math.cos(distanceInKm / RADIUS) - Math.sin(lon1Radian)
                        * Math.sin(Math.toRadians(newMinimumLatitudeRadian)));

        double newMinimumLatitude = Math.toDegrees(newMinimumLatitudeRadian);
        double newMinimumLongitude = Math.toDegrees(newMinimumLongitudeRadian);

        return new Location(newMinimumLatitude, newMinimumLongitude);
    }
}
