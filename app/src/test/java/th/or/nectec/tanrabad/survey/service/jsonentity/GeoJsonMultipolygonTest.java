package th.or.nectec.tanrabad.survey.service.jsonentity;

import com.bluelinelabs.logansquare.LoganSquare;
import org.junit.Test;
import th.or.nectec.tanrabad.entity.Location;
import th.or.nectec.tanrabad.entity.Polygon;
import th.or.nectec.tanrabad.survey.utils.ResourceFile;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class GeoJsonMultipolygonTest {

    String rawMultipolygonGeoJson = "{" +
            "\"coordinates\":[" +
            "[[[40.0,40.0],[20.0,45.0],[45.0,30.0],[40.0,40.0]]]," +
            "[[[20.0,35.0],[10.0,30.0],[10.0,10.0],[30.0,5.0],[45.0,20.0],[20.0,35.0]]," +
            "[[30.0,20.0],[20.0,15.0],[20.0,25.0],[30.0,20.0]]]" +
            "]," +
            "\"type\":\"MultiPolygon\"}";


    @Test
    public void testSerialize() throws Exception {
        GeoJsonMultipolygon geoJsonMultipolygon = LoganSquare.parse(rawMultipolygonGeoJson, GeoJsonMultipolygon.class);
        assertEquals(rawMultipolygonGeoJson, LoganSquare.serialize(geoJsonMultipolygon));
    }

    @Test
    public void testGetBoundary() throws Exception {
        GeoJsonMultipolygon geoJsonMultipolygon = LoganSquare.parse(rawMultipolygonGeoJson, GeoJsonMultipolygon.class);

        assertEquals(new Location(40, 40), geoJsonMultipolygon.getPolygon(0).getBoundary().get(0));
        assertEquals(new Location(45, 20), geoJsonMultipolygon.getPolygon(0).getBoundary().get(1));
        assertEquals(new Location(40, 40), geoJsonMultipolygon.getPolygon(0).getBoundary().get(3));
    }

    @Test
    public void testGetHoleCount() throws Exception {
        GeoJsonMultipolygon geoJsonMultipolygon = LoganSquare.parse(rawMultipolygonGeoJson, GeoJsonMultipolygon.class);

        assertEquals(0, geoJsonMultipolygon.getPolygon(0).getHolesCount());
        assertEquals(1, geoJsonMultipolygon.getPolygon(1).getHolesCount());
    }

    @Test
    public void testGetHole() throws Exception {
        GeoJsonMultipolygon geoJsonMultipolygon = LoganSquare.parse(rawMultipolygonGeoJson, GeoJsonMultipolygon.class);
        assertEquals(new Location(20, 30), geoJsonMultipolygon.getPolygon(1).getHole(0).get(0));
        assertEquals(new Location(15, 20), geoJsonMultipolygon.getPolygon(1).getHole(0).get(1));
        assertEquals(new Location(20, 30), geoJsonMultipolygon.getPolygon(1).getHole(0).get(3));
    }

    @Test
    public void testParseToGeoJson() throws Exception {
        ArrayList<Polygon> polygons = new ArrayList<>();
        polygons.add(mockPolygon());
        GeoJsonMultipolygon multipolygon = GeoJsonMultipolygon.parse(polygons);

        assertEquals("MultiPolygon", multipolygon.type);
        assertEquals(new Location(10, 20), multipolygon.getPolygon(0).getBoundary().get(0));
        assertEquals(new Location(10, 30), multipolygon.getPolygon(0).getBoundary().get(1));
        assertEquals(new Location(10, 20), multipolygon.getPolygon(0).getBoundary().get(3));
        assertEquals(new Location(15, 15), multipolygon.getPolygon(0).getHole(0).get(0));
        assertEquals(new Location(15, 20), multipolygon.getPolygon(0).getHole(0).get(1));
        assertEquals(new Location(15, 15), multipolygon.getPolygon(0).getHole(0).get(3));
    }

    private Polygon mockPolygon() {
        List<Location> boundary = mockBoundary();
        List<Location>[] holes = new ArrayList[]{mockHole()};
        return new Polygon(boundary, holes);
    }

    private List<Location> mockBoundary() {
        List<Location> boundary = new ArrayList<>();
        boundary.add(new Location(10, 20));
        boundary.add(new Location(10, 30));
        boundary.add(new Location(50, 30));
        boundary.add(new Location(10, 20));
        return boundary;
    }

    private ArrayList<Location> mockHole() {
        ArrayList<Location> boundary = new ArrayList<>();
        boundary.add(new Location(15, 15));
        boundary.add(new Location(15, 20));
        boundary.add(new Location(40, 20));
        boundary.add(new Location(15, 15));
        return boundary;
    }

    @Test
    public void testGetEntities() throws Exception {
        GeoJsonMultipolygon geoJsonMultipolygon = LoganSquare.parse(rawMultipolygonGeoJson, GeoJsonMultipolygon.class);
        List<Polygon> polygons = geoJsonMultipolygon.getEntities();

        assertEquals(new Location(40, 40), polygons.get(0).getBoundary().get(0));
        assertEquals(new Location(45, 20), polygons.get(0).getBoundary().get(1));
        assertEquals(new Location(40, 40), polygons.get(0).getBoundary().get(3));
    }

    @Test
    public void testGetMultiplePolygonList() throws Exception {
        List<GeoJsonMultipolygon> multipolygonList = LoganSquare.parseList(ResourceFile.read("multiPolygon10Item.json"), GeoJsonMultipolygon.class);
        assertEquals(10, multipolygonList.size());
        assertEquals(new Location(40, 40), multipolygonList.get(0).getPolygon(0).getBoundary().get(0));
        assertEquals(new Location(45, 20), multipolygonList.get(0).getPolygon(0).getBoundary().get(1));
        assertEquals(new Location(40, 40), multipolygonList.get(0).getPolygon(0).getBoundary().get(3));

        assertEquals(new Location(35, 20), multipolygonList.get(4).getPolygon(0).getBoundary().get(0));
        assertEquals(new Location(30, 10), multipolygonList.get(4).getPolygon(0).getBoundary().get(1));
        assertEquals(new Location(5, 30), multipolygonList.get(4).getPolygon(0).getBoundary().get(3));

        assertEquals(new Location(15, 23), multipolygonList.get(9).getPolygon(0).getHole(0).get(0));
        assertEquals(new Location(15, 2), multipolygonList.get(9).getPolygon(0).getHole(0).get(1));
        assertEquals(new Location(15, 23), multipolygonList.get(9).getPolygon(0).getHole(0).get(3));

    }
}
