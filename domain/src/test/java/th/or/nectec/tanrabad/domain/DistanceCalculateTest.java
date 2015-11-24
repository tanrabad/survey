package th.or.nectec.tanrabad.domain;

import org.junit.Test;

import th.or.nectec.tanrabad.entity.Location;

import static org.junit.Assert.assertEquals;

public class DistanceCalculateTest {
    @Test
    public void testCalculateBetweenPoint() throws Exception {
        DistanceCalculate distanceCalculate = new DistanceCalculate();
        Location startLocation = new Location(41.5982,-73.1434);
        Location destinationLocation = new Location(39.7802,-74.9453);
        assertEquals(distanceCalculate.calculate(startLocation, destinationLocation),250,5);

    }
}
