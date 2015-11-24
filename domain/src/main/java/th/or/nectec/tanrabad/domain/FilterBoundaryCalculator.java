package th.or.nectec.tanrabad.domain;

import th.or.nectec.tanrabad.entity.Location;

public interface FilterBoundaryCalculator {
    Location getMinLocation(Location currentLocation, int distanceInKm);

    Location getMaxLocation(Location currentLocation, int distanceInKm);
}
