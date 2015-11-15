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

    @Test
    public void testWithName() throws Exception {
        Building building = Building.withName(BUILDING_NAME);
        assertEquals(BUILDING_NAME, building.getName());
    }

    @Test
    public void testSetThenGetPlace() throws Exception {
        Building building = new Building(BUILDING_UUID, BUILDING_NAME);
        building.setPlace(place);
        assertEquals(place, building.getPlace());
    }

    @Test
    public void testGetName() throws Exception {
        Building building = new Building(BUILDING_UUID, BUILDING_NAME);
        assertEquals(BUILDING_NAME, building.getName());
    }

    @Test
    public void testSetThenGetName() throws Exception {
        Building building = new Building(BUILDING_UUID, BUILDING_NAME);
        building.setName("โบสถ์เล็ก");
        assertEquals("โบสถ์เล็ก", building.getName());
    }

    @Test
    public void testGetId() throws Exception {
        Building building = new Building(BUILDING_UUID, BUILDING_NAME);
        assertEquals(BUILDING_UUID, building.getId());
    }

    @Test
    public void buildingWithDifferentIdMustNotEqual() throws Exception {
        Building building1 = new Building(BUILDING_UUID, BUILDING_NAME);
        building1.setPlace(place);
        Building building2 = new Building(UUID.randomUUID(), BUILDING_NAME);
        building2.setPlace(place);
        assertNotEquals(building1, building2);
    }

    @Test
    public void buildingWithDifferentNameMustNotEqual() throws Exception {
        Building building1 = new Building(BUILDING_UUID, BUILDING_NAME);
        building1.setPlace(place);
        Building building2 = new Building(BUILDING_UUID, "โบสถ์เล็ก");
        building2.setPlace(place);
        assertNotEquals(building1, building2);
    }

    @Test
    public void buildingWithDifferentPlaceMustNotEqual() throws Exception {
        Building building1 = new Building(BUILDING_UUID, BUILDING_NAME);
        building1.setPlace(place);
        Building building2 = new Building(BUILDING_UUID, BUILDING_NAME);
        building2.setPlace(Place.withName("โรงเรียนเซนต์เมรี่"));
        assertNotEquals(building1, building2);
    }

    @Test
    public void buildingWithTheSameNameAndIdAndPlaceMustEqual() throws Exception {
        Building building1 = new Building(BUILDING_UUID, BUILDING_NAME);
        building1.setPlace(place);
        Building building2 = new Building(BUILDING_UUID, BUILDING_NAME);
        building2.setPlace(place);
        assertEquals(building1, building2);
    }
}