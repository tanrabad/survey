package org.tanrabad.survey.nearby.repository;

import org.tanrabad.survey.entity.field.Location;

class ImpLocationBoundCalculator implements LocationBoundCalculator {

    private static final double KM_PER_LNG = 111.699;
    private static final double KM_PER_LAT = 110.567;

    @Override public org.tanrabad.survey.entity.field.LocationBound get(Location location, int distance) {
        Location minLocation = getMinLocation(location, distance);
        Location maxLocation = getMaxLocation(location, distance);
        return new org.tanrabad.survey.entity.field.LocationBound(minLocation, maxLocation);
    }

    private Location getMinLocation(Location currentLocation, double distanceInKm) {
        double longitudeDegreeFromKm = distanceInKm / KM_PER_LNG;
        double latitudeDegreeFromKm = distanceInKm / KM_PER_LAT;
        double minimumLongitude = currentLocation.getLongitude() - longitudeDegreeFromKm;
        double minimumLatitude = currentLocation.getLatitude() - latitudeDegreeFromKm;

        return new Location(minimumLatitude, minimumLongitude);
    }

    private Location getMaxLocation(Location currentLocation, double distanceInKm) {
        double longitudeDegreeFromKm = distanceInKm / KM_PER_LNG;
        double latitudeDegreeFromKm = distanceInKm / KM_PER_LAT;
        double maximumLongitude = currentLocation.getLongitude() + longitudeDegreeFromKm;
        double maximumLatitude = currentLocation.getLatitude() + latitudeDegreeFromKm;

        return new Location(maximumLatitude, maximumLongitude);
    }
}
