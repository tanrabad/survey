package th.or.nectec.tanrabad.domain;

import org.junit.Test;

import th.or.nectec.tanrabad.entity.Location;

import static org.junit.Assert.assertEquals;

public class DistanceCalculatePlanarTest {

    @Test
    public void testDistanceCalculatePlanar() throws Exception {
        DistanceCalculatePlanar distanceCalculatePlanar = new DistanceCalculatePlanar();
        Location startLocation = new Location(40.6892,-74.0444);
        Location destinationLocation = new Location(39.7802,-74.9453);
        assertEquals(distanceCalculatePlanar.calculate(startLocation, destinationLocation),125,5);
    }
}
