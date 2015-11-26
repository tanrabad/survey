package th.or.nectec.tanrabad.domain;

import th.or.nectec.tanrabad.entity.Location;

public class NewLocationCalculate implements NewLocationCalculator {

    private double Radius = 6371;

    @Override
    public Location getNewMaxLocation(Location currentLocation, double distanceInKm) {
        double lat1 = currentLocation.getLatitude();
        double lon1 = currentLocation.getLongitude();

        double lat1Radian = Math.toRadians(lat1);
        double lon1Radian = Math.toRadians(lon1);

        double brng = 0.7853981; //Bearing is 45 degrees converted to radians. brng = (degree*pi)/180

        //d/Redius called angular distance in radians.

        double newMaximumLatitude = lat1 + Math.asin(Math.sin(lat1Radian) * Math.cos(distanceInKm / Radius) +
                Math.cos(lat1Radian) * Math.sin(distanceInKm / Radius) * Math.cos(brng));

        double newMaximumLongitude = lon1 + Math.atan2(Math.sin(brng) * Math.sin(distanceInKm / Radius) * Math.cos(lon1Radian),
                Math.cos(distanceInKm / Radius) - Math.sin(lon1Radian) * Math.sin(Math.toRadians(newMaximumLatitude)));

        return new Location(newMaximumLatitude, newMaximumLongitude);

    }

    @Override
    public Location getNewMinLocation(Location currentLocation, double distanceInKm) {
        double lat1 = currentLocation.getLatitude();
        double lon1 = currentLocation.getLongitude();

        double lat1Radian = Math.toRadians(lat1);
        double lon1Radian = Math.toRadians(lon1);

        double brng = 3.92699081; //Bearing is 225 degrees converted to radians. brng = (degree*pi)/180

        double newMinimumLatitude = lat1 - Math.asin(Math.sin(Math.toRadians(lat1)) * Math.cos(distanceInKm / Radius) +
                Math.cos(Math.toRadians(lat1)) * Math.sin(distanceInKm / Radius) * Math.cos(brng));

        double newMinimumLongitude = lon1 - Math.atan2(Math.sin(brng) * Math.sin(distanceInKm / Radius) * Math.cos(lon1Radian),
                Math.cos(distanceInKm / Radius) - Math.sin(lon1Radian) * Math.sin(Math.toRadians(newMinimumLatitude)));

        return new Location(newMinimumLatitude, newMinimumLongitude);
    }
}
