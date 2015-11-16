package th.or.nectec.tanrabad.entity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(JUnit4.class)
public class BuildingTest {

    public static final UUID BUILDING_UUID = UUID.randomUUID();
    public static final String BUILDING_NAME = "โบสถ์ใหญ่";
    private final Place place = Place.withName("วิหารเซนต์เมรี่");
    private final Building building1 = new Building(BUILDING_UUID, BUILDING_NAME);

    @Test
    public void testWithName() throws Exception {
        Building building = Building.withName(BUILDING_NAME);
        assertEquals(BUILDING_NAME, building.getName());
    }

    @Test
    public void testSetThenGetPlace() throws Exception {
        building1.setPlace(place);
        assertEquals(place, building1.getPlace());
    }

    @Test
    public void testGetName() throws Exception {
        assertEquals(BUILDING_NAME, building1.getName());
    }

    @Test
    public void testSetThenGetName() throws Exception {
        building1.setName("โบสถ์เล็ก");
        assertEquals("โบสถ์เล็ก", building1.getName());
    }

    @Test
    public void testGetId() throws Exception {
        assertEquals(BUILDING_UUID, building1.getId());
    }

    @Test
    public void buildingWithDifferentIdMustNotEqual() throws Exception {
        building1.setPlace(place);
        Building building2 = new Building(UUID.randomUUID(), BUILDING_NAME);
        building2.setPlace(place);
        assertNotEquals(building1, building2);
    }

    @Test
    public void buildingWithDifferentNameMustNotEqual() throws Exception {
        building1.setPlace(place);
        Building building2 = new Building(BUILDING_UUID, "โบสถ์เล็ก");
        building2.setPlace(place);
        assertNotEquals(building1, building2);
    }

    @Test
    public void buildingWithDifferentPlaceMustNotEqual() throws Exception {
        building1.setPlace(place);
        Building building2 = new Building(BUILDING_UUID, BUILDING_NAME);
        building2.setPlace(Place.withName("โรงเรียนเซนต์เมรี่"));
        assertNotEquals(building1, building2);
    }

    @Test
    public void buildingWithTheSameNameAndIdAndPlaceMustEqual() throws Exception {
        building1.setPlace(place);
        Building building2 = new Building(BUILDING_UUID, BUILDING_NAME);
        building2.setPlace(place);
        assertEquals(building1, building2);
    }
}