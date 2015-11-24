package th.or.nectec.tanrabad.domain;

import th.or.nectec.tanrabad.entity.Location;

public interface DistanceCalculator {
    double calculate(Location currentLocation, Location targetLocation);
}
