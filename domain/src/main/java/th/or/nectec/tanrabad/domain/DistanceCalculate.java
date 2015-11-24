package th.or.nectec.tanrabad.domain;

import th.or.nectec.tanrabad.entity.Location;

public interface DistanceCalculate {
    double calculate(Location currentLocation, Location targetLocation);
}
