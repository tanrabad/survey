package th.or.nectec.tanrabad.domain;

import th.or.nectec.tanrabad.entity.Location;

public class DistanceCalculatePlanar implements DistanceCalculator {
    private double Radius = 6371;

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
        d = Radius * Math.sqrt(((x * x) + (y * y)));

        return d;
    }
}
