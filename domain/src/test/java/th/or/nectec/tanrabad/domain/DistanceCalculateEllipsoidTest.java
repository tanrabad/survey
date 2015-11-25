package th.or.nectec.tanrabad.domain;

import org.junit.Test;

import th.or.nectec.tanrabad.entity.Location;

import static org.junit.Assert.assertEquals;

public class DistanceCalculateEllipsoidTest {
    @Test
    public void testDistanceCalculateEllipsoid() throws Exception {
        DistanceCalculateEllipsoid distanceCalculateEllipsoid = new DistanceCalculateEllipsoid();
        Location startLocation = new Location(41.5982,-73.1434);
        Location destinationLocation = new Location(39.7802,-74.9453);
        assertEquals(distanceCalculateEllipsoid.calculate(startLocation, destinationLocation),200,5);
    }
}
