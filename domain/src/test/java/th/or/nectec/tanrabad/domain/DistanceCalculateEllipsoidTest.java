package th.or.nectec.tanrabad.domain;

import org.junit.Test;

import th.or.nectec.tanrabad.entity.Location;

import static org.junit.Assert.assertEquals;

public class DistanceCalculateEllipsoidTest {
    @Test
    public void testDistanceCalculateEllipsoid() throws Exception {
        DistanceCalculateEllipsoid distanceCalculateEllipsoid = new DistanceCalculateEllipsoid();
        Location startLocation = new Location(40.6892,-74.0444);
        Location destinationLocation = new Location(39.7802,-74.9453);
        assertEquals(distanceCalculateEllipsoid.calculate(startLocation, destinationLocation),125,5);
    }
}
