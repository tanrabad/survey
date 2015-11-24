package th.or.nectec.tanrabad.domain;

import th.or.nectec.tanrabad.entity.Location;

public class FilterBoundaryCalculate implements FilterBoundaryCalculator {

    @Override
    public Location getMinLocation(Location currentLocation, int distanceInKm) {
        double longitudeDegreeFromKm  = distanceInKm/111.699;
        double latitudeDegreeFromKm = distanceInKm/110.567;
        double minimumLongitude = currentLocation.getLongitude() - longitudeDegreeFromKm;
        double minimumLatitude = currentLocation.getLatitude() - latitudeDegreeFromKm;

        return new Location(minimumLatitude, minimumLongitude);
    }

    @Override
    public Location getMaxLocation(Location currentLocation, int distanceInKm) {
        double longitudeDegreeFromKm  = distanceInKm/111.699;
        double latitudeDegreeFromKm = distanceInKm/110.567;
        double maximumLongitude = currentLocation.getLongitude() + longitudeDegreeFromKm;
        double maximumLatitude = currentLocation.getLatitude() + latitudeDegreeFromKm;

        return new Location(maximumLatitude, maximumLongitude);
    }
}
