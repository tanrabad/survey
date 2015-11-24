package th.or.nectec.tanrabad.domain;

import org.junit.Test;

import th.or.nectec.tanrabad.entity.Location;

import static org.junit.Assert.assertEquals;

public class LocationBoundaryCalculateTest {
    @Test
    public void testMinimumLocation() throws Exception {
        Location myLocation = new Location(40.6892, -74.0444);
        Location minimumLocation = new Location(39.78477101124205, -74.93966316260665);
        int distanceInKm = 100;

        FilterBoundaryCalculate filterBoundaryCalculate = new FilterBoundaryCalculate();
        assertEquals(filterBoundaryCalculate.getMinLocation(myLocation, distanceInKm), minimumLocation );
    }

    @Test
    public void testMaximumLocation() throws Exception {
        Location myLocation = new Location(40.6892, -74.0444);
        Location maximumLocation = new Location(41.59362898875795, -73.14913683739334);
        int distanceInKm = 100;

        FilterBoundaryCalculate filterBoundaryCalculate = new FilterBoundaryCalculate();
        assertEquals(filterBoundaryCalculate.getMaxLocation(myLocation, distanceInKm), maximumLocation );
    }
}
