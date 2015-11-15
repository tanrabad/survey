package th.or.nectec.tanrabad.entity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(JUnit4.class)
public class PlaceTest {

    public static final UUID BANGPHAI_UUID = UUID.randomUUID();
    public static final String BANGPHAI_NAME = "บางไผ่";
    public static final int BANGPHAI_TYPE = Place.TYPE_VILLAGE_COMMUNITY;

    @Test
    public void testPlaceWithName() throws Exception {
        Place place = Place.withName(BANGPHAI_NAME);
        assertEquals(BANGPHAI_NAME, place.getName());
    }

    @Test
    public void testGetPlaceId() throws Exception {
        Place place = new Place(BANGPHAI_UUID, BANGPHAI_NAME);
        assertEquals(BANGPHAI_UUID, place.getId());
    }

    @Test
    public void testGetPlaceName() throws Exception {
        Place place = new Place(BANGPHAI_UUID, BANGPHAI_NAME);
        assertEquals(BANGPHAI_NAME, place.getName());
    }

    @Test
    public void testSetThenGetPlaceName() throws Exception {
        Place place = new Place(BANGPHAI_UUID, BANGPHAI_NAME);
        place.setName("บางโพธิ์");
        assertEquals("บางโพธิ์", place.getName());
    }

    @Test
    public void testSetThenGetPlaceType() throws Exception {
        Place place = new Place(BANGPHAI_UUID, BANGPHAI_NAME);
        place.setType(Place.TYPE_SCHOOL);
        assertEquals(Place.TYPE_SCHOOL, place.getType());
    }

    @Test
    public void placeWithDifferentNameMustNotEqual() throws Exception {
        Place place1 = new Place(BANGPHAI_UUID, BANGPHAI_NAME);
        place1.setType(BANGPHAI_TYPE);
        Place place2 = new Place(BANGPHAI_UUID, "บางโพธิ์");
        place2.setType(BANGPHAI_TYPE);
        assertNotEquals(place1, place2);
    }

    @Test
    public void placeWithDifferentTypeMustNotEqual() throws Exception {
        Place place1 = new Place(BANGPHAI_UUID, BANGPHAI_NAME);
        place1.setType(Place.TYPE_FACTORY);
        Place place2 = new Place(BANGPHAI_UUID, BANGPHAI_NAME);
        place2.setType(Place.TYPE_SCHOOL);
        assertNotEquals(place1, place2);
    }

    @Test
    public void placeWithTheSameNameAndTypeMustEqual() throws Exception {
        Place place1 = new Place(BANGPHAI_UUID, BANGPHAI_NAME);
        place1.setType(BANGPHAI_TYPE);
        Place place2 = new Place(BANGPHAI_UUID, BANGPHAI_NAME);
        place2.setType(BANGPHAI_TYPE);
        assertEquals(place1, place2);
    }
}
