package th.or.nectec.tanrabad.domain.geographic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import th.or.nectec.tanrabad.entity.field.Location;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class EllipsoidDistanceWithDummyTest {

    public static final double DELTA = 0.5;

    EllipsoidDistance ellipsoidDistance = new EllipsoidDistance();

    Location startLocation = new Location(40.6892, -74.0444);

    boolean testExpectResult;
    private Location destinationLocation;
    private double distanceBetweenPoint;
    private boolean expectValue;

    public EllipsoidDistanceWithDummyTest(Location destinationLocation, double distanceBetweenPoint, boolean expectValue) {
        this.destinationLocation = destinationLocation;
        this.distanceBetweenPoint = distanceBetweenPoint;
        this.expectValue = expectValue;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{

                //{new Location(39.7802, -74.9453)},

                //outside coverage area 17 (0-16)
                {new Location(39.00000, -73.00000), 207.7, false},
                {new Location(39.05000, -73.04000), 201.26, false},
                {new Location(39.60000, -73.04000), 148.15, false},
                {new Location(39.65000, -73.10000), 140.65, false},
                {new Location(39.70000, -73.15000), 133.64, false},
                {new Location(39.71000, -73.19000), 130.8, false},
                {new Location(40.95000, -74.95000), 81.68, true},
                {new Location(41.00000, -74.98000), 86.09, true},
                {new Location(41.10000, -75.00000), 92.53, true},
                {new Location(41.15000, -75.05000), 98.94, true},
                {new Location(41.20000, -73.00000), 104.62, false},
                {new Location(41.25000, -73.04000), 104.98, false},
                {new Location(41.30000, -73.04000), 108.34, false},
                {new Location(41.31000, -73.10000), 105.17, false},
                {new Location(41.32000, -74.25000), 72.15, true},
                {new Location(41.75000, -74.30000), 119.74, false},
                {new Location(41.80000, -74.35000), 125.99, false},

                //inside coverage area 22 (17-38)
                {new Location(40.09000, -73.30000), 91.75, true},
                {new Location(40.10000, -73.35000), 88.05, true},
                {new Location(40.15000, -73.90000), 61.11, true},
                {new Location(40.20000, -73.92000), 55.34, true},
                {new Location(40.25000, -74.20000), 50.52, true},
                {new Location(40.30000, -74.19000), 44.94, true},
                {new Location(40.35000, -74.18000), 39.38, true},
                {new Location(40.40000, -74.16000), 33.57, true},
                {new Location(40.45000, -74.15100), 28.05, true},
                {new Location(41.60000, -74.60000), 111.38, false},
                {new Location(41.63000, -74.70000), 118.08, false},
                {new Location(41.65000, -74.75000), 122.02, false},
                {new Location(41.70000, -74.90000), 133.23, false},
                {new Location(40.50000, -74.15000), 22.83, true},
                {new Location(40.55000, -73.94000), 17.80, true},
                {new Location(40.60000, -73.95000), 12.72, true},
                {new Location(40.65000, -73.96000), 8.35, true},
                {new Location(40.70000, -73.98000), 5.57, true},
                {new Location(40.75000, -73.99000), 8.16, true},
                {new Location(40.80000, -74.00000), 12.86, true},
                {new Location(40.85000, -74.05000), 17.86, true},
                {new Location(40.90000, -74.10000), 23.87, true},

                //NE (39-40)
                {new Location(41.35000, -73.30000), 96.45, true},
                {new Location(41.40000, -73.35000), 98.17, true},

                //SE (41-44)
                {new Location(41.45000, -74.40000), 89.62, true},
                {new Location(41.50000, -74.45000), 96.27, true},
                {new Location(41.55000, -74.47000), 102.06, false},
                {new Location(41.35000, -74.50000), 82.78, true},

                //SW (45-47)
                {new Location(39.90000, -74.25000), 89.36, true},
                {new Location(39.95000, -74.30000), 84.90, true},
                {new Location(40.00000, -74.35000), 80.81, true},

                //NW (48-49)
                {new Location(39.80000, -73.20000), 122.10, false},
                {new Location(39.85000, -73.25000), 115.09, false}

        });
    }

    @Test
    public void testDistanceCalculateEllipsoid() throws Exception {
        //Location destinationLocation = new Location(39.7802, -74.9453);

        assertEquals(distanceBetweenPoint, ellipsoidDistance.calculate(startLocation, destinationLocation), DELTA);
    }

    @Test
    public void testCheckIsDistanceInCoverageArea100Km() throws Exception {
        final boolean expectResult = ellipsoidDistance.calculate(startLocation, destinationLocation) <= 100;

        assertEquals(expectResult, expectValue);
    }
}
