package th.or.nectec.tanrabad.domain;

import org.junit.Test;

import th.or.nectec.tanrabad.entity.Location;

import static org.junit.Assert.assertEquals;

public class NewLocationBoundaryCalculateTest {

    @Test
    public void testNewMaximumLocation() throws Exception {
        Location myLocation = new Location(40.6892, -74.0444);
        Location newMaximumLocation = new Location(41.410405647717255, -74.04253499416869);
        int distanceInKm = 100;

        NewLocationCalculate newLocationCalculate = new NewLocationCalculate();
        assertEquals(newLocationCalculate.getNewMaxLocation(myLocation, distanceInKm), newMaximumLocation );
    }

    @Test
    public void testNewMinimumLocation() throws Exception {
        Location myLocation = new Location(40.6892, -74.0444);
        Location newMinimumLocation = new Location(39.99019055660681, -74.04251416529044);
        int distanceInKm = 100;

        NewLocationCalculate newLocationCalculate = new NewLocationCalculate();
        assertEquals(newLocationCalculate.getNewMinLocation(myLocation, distanceInKm), newMinimumLocation );
    }
}
