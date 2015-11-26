package th.or.nectec.tanrabad.domain;

import th.or.nectec.tanrabad.entity.Location;

public interface NewLocationCalculator {

    Location getNewMaxLocation(Location currentLocation, double distanceInKm);

    Location getNewMinLocation(Location currentLocation, double distanceInKm);
}
