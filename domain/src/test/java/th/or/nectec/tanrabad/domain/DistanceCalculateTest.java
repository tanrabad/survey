package th.or.nectec.tanrabad.domain;

import org.junit.Test;

import th.or.nectec.tanrabad.entity.Location;

import static org.junit.Assert.assertEquals;

public class DistanceCalculateTest {
    @Test
    public void testCalculateBetweenPoint() throws Exception {
        DistanceCalculate distanceCalculate = new DistanceCalculate();
        Location startLocation = new Location(40.6892,-74.0444);
        Location destinationLocation = new Location(39.7802,-74.9453);
        assertEquals(distanceCalculate.calculate(startLocation, destinationLocation),125,5);

    }
}
