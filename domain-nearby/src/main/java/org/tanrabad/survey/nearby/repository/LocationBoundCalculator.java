package org.tanrabad.survey.nearby.repository;

import org.tanrabad.survey.entity.field.Location;
import org.tanrabad.survey.entity.field.LocationBound;

public interface LocationBoundCalculator {
    LocationBound get(Location location, int distance);
}
