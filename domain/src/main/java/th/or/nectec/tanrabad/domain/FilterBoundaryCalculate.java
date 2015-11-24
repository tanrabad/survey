package th.or.nectec.tanrabad.domain;

import th.or.nectec.tanrabad.entity.Location;

public class FilterBoundaryCalculate implements FilterBoundaryCalculator {

    double currentLatitude = 40.6892;
    double currentLongitude = -74.0444;
    final Location currentLocation = new Location(currentLatitude, currentLongitude);

    private double minimumLongitude;
    private double minimumLatitude;
    private double maximumLongitude;
    private double maximumLatitude;

    double distanceInKm = 100;

    private double longitudeDegreeFromKm  = distanceInKm/111.699;
    private double latitudeDegreeFromKm = distanceInKm/110.567;

    @Override
    public Location getMinLocation(Location currentLocation, int distanceInKm) {
        minimumLongitude = currentLocation.getLongitude() - longitudeDegreeFromKm;
        minimumLatitude = currentLocation.getLatitude() - latitudeDegreeFromKm;

        return  new Location(minimumLatitude, minimumLongitude);
    }

    @Override
    public Location getMaxLocation(Location currentLocation, int distanceInKm) {
        maximumLongitude = currentLocation.getLongitude() + longitudeDegreeFromKm;
        maximumLatitude = currentLocation.getLatitude() + latitudeDegreeFromKm;

        return  new Location(maximumLatitude, maximumLongitude);
    }
}
