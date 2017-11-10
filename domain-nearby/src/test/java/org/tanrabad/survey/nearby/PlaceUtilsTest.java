package org.tanrabad.survey.nearby;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.Test;
import org.tanrabad.survey.entity.Place;
import org.tanrabad.survey.entity.field.Location;
import org.tanrabad.survey.nearby.PlaceUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PlaceUtilsTest {
    @Test public void findPlaceWithoutLocationTest() throws Exception {
        Place place1 = new Place(UUID.nameUUIDFromBytes("2".getBytes()), "a");
        Place place2 = new Place(UUID.nameUUIDFromBytes("4".getBytes()), "b");
        Place place3 = new Place(UUID.nameUUIDFromBytes("6".getBytes()), "c");
        place3.setLocation(new Location(11, 3));

        List<Place> places = new ArrayList<>();
        places.add(place1);
        places.add(place2);
        places.add(place3);

        List<Place> placesWithoutLocation = new ArrayList<>();
        placesWithoutLocation.add(place1);
        placesWithoutLocation.add(place2);

        assertEquals(placesWithoutLocation, PlaceUtils.getPlacesWithoutLocation(places));
    }

    @Test public void findPlaceWithoutLocationByAllPlacesHaveLocationTest() throws Exception {
        Place place1 = new Place(UUID.nameUUIDFromBytes("1".getBytes()), "d");
        place1.setLocation(new Location(7, 5));
        Place place2 = new Place(UUID.nameUUIDFromBytes("3".getBytes()), "e");
        place2.setLocation(new Location(9, 6));
        Place place3 = new Place(UUID.nameUUIDFromBytes("5".getBytes()), "f");
        place3.setLocation(new Location(11, 3));

        List<Place> places = new ArrayList<>();
        places.add(place1);
        places.add(place2);
        places.add(place3);

        assertNull(PlaceUtils.getPlacesWithoutLocation(places));
    }

    @Test public void grouppingSubdistrictCodeTest() throws Exception {
        Place place1 = new Place(UUID.nameUUIDFromBytes("10".getBytes()), "aa");
        place1.setSubdistrictCode("100101");
        Place place2 = new Place(UUID.nameUUIDFromBytes("12".getBytes()), "bb");
        place2.setSubdistrictCode("100201");
        Place place3 = new Place(UUID.nameUUIDFromBytes("14".getBytes()), "cc");
        place3.setSubdistrictCode("100101");

        List<Place> places = new ArrayList<>();
        places.add(place1);
        places.add(place2);
        places.add(place3);

        List<String> groupingSubdistrictCode = new ArrayList<>();
        groupingSubdistrictCode.add("100101");
        groupingSubdistrictCode.add("100201");

        assertEquals(groupingSubdistrictCode, PlaceUtils.groupingSubdistict(places));
    }

    @Test public void setWeightOfPlaceWithoutLocationTest() throws Exception {
        Place place1 = new Place(UUID.nameUUIDFromBytes("10".getBytes()), "aa");
        place1.setSubdistrictCode("100101");
        Place place2 = new Place(UUID.nameUUIDFromBytes("12".getBytes()), "bb");
        place2.setSubdistrictCode("100201");
        Place place3 = new Place(UUID.nameUUIDFromBytes("14".getBytes()), "cc");
        place3.setSubdistrictCode("100101");
        Place place4 = new Place(UUID.nameUUIDFromBytes("16".getBytes()), "dd");
        place4.setSubdistrictCode("100105");

        List<Place> placesWithoutLocation = new ArrayList<>();
        placesWithoutLocation.add(place1);
        placesWithoutLocation.add(place2);
        placesWithoutLocation.add(place3);
        placesWithoutLocation.add(place4);

        List<Place> filterPlaceWithoutLocationInsideSubdistrict = new ArrayList<>();
        filterPlaceWithoutLocationInsideSubdistrict.add(place1);
        filterPlaceWithoutLocationInsideSubdistrict.add(place2);
        filterPlaceWithoutLocationInsideSubdistrict.add(place3);

        List<String> groupingSubdistrictCode = new ArrayList<>();
        groupingSubdistrictCode.add("100101");
        groupingSubdistrictCode.add("100201");

        assertEquals(filterPlaceWithoutLocationInsideSubdistrict,
                PlaceUtils.findPlacesWithoutLocationInsideSubdistrict(groupingSubdistrictCode, placesWithoutLocation));
    }
}
