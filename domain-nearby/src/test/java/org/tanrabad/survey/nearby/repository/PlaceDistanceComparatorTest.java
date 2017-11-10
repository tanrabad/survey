package org.tanrabad.survey.nearby.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.Test;
import org.tanrabad.survey.entity.Place;
import org.tanrabad.survey.entity.field.Location;
import org.tanrabad.survey.nearby.repository.PlaceDistanceComparator;

import static org.junit.Assert.assertEquals;

public class PlaceDistanceComparatorTest {
    @Test public void compareFromNearToFarTest() throws Exception {
        Place place1 = new Place(UUID.nameUUIDFromBytes("2".getBytes()), "Valayalongkorn Rajabhat University");
        place1.setLocation(new Location(14.133843, 100.613192));
        Place place2 = new Place(UUID.nameUUIDFromBytes("4".getBytes()), "Bangkok University");
        place2.setLocation(new Location(14.039604, 100.615558));
        Place place3 = new Place(UUID.nameUUIDFromBytes("6".getBytes()), "SACICT");
        place3.setLocation(new Location(14.148401, 100.523451));
        List<Place> rawPlace = new ArrayList<>();
        rawPlace.add(place1);
        rawPlace.add(place2);
        rawPlace.add(place3);

        List<Place> sortedPlace = new ArrayList<>();
        sortedPlace.add(place2);
        sortedPlace.add(place1);
        sortedPlace.add(place3);

        Location centerLocation = new Location(14.0811245, 100.6083875);
        Collections.sort(rawPlace, new PlaceDistanceComparator(centerLocation));
        assertEquals(sortedPlace, rawPlace);
    }
}
