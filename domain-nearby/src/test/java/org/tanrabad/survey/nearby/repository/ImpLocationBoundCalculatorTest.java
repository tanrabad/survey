package org.tanrabad.survey.nearby.repository;

import org.junit.Test;
import org.tanrabad.survey.entity.field.Location;
import org.tanrabad.survey.entity.field.LocationBound;

import static org.junit.Assert.*;

public class ImpLocationBoundCalculatorTest {

    private static final int DISTANCE_IN_KM = 100;
    private final Location myLocation = new Location(40.6892, -74.0444);
    private final LocationBound expectedBound = new LocationBound(
        new Location(39.78477101124205, -74.93966316260665),
        new Location(41.59362898875795, -73.14913683739334));

    @Test
    public void get() throws Exception {
        ImpLocationBoundCalculator impLocationBoundary = new ImpLocationBoundCalculator();
        LocationBound locationBound = impLocationBoundary.get(myLocation, DISTANCE_IN_KM);

        assertEquals(expectedBound, locationBound);
    }
}
