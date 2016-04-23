package org.tanrabad.survey.service.json;

import com.bluelinelabs.logansquare.LoganSquare;

import org.junit.Test;

import th.or.nectec.tanrabad.entity.field.Location;

import static org.junit.Assert.assertEquals;


public class GeoJsonPointTest {

    private String rawPointGeoJson = "{ \"type\": \"Point\", \"coordinates\": [30.0, 10.0]}";

    @Test
    public void testParseArray() throws Exception {
        GeoJsonPoint geoJsonPoint = LoganSquare.parse(rawPointGeoJson, GeoJsonPoint.class);

        assertEquals("Point", geoJsonPoint.type);
        assertEquals(10.0f, geoJsonPoint.getLatitude(), 0);
        assertEquals(30.0f, geoJsonPoint.getLongitude(), 0);
    }

    @Test
    public void testGetLocationEntityFromGeoJson() throws Exception {
        GeoJsonPoint geoJsonPoint = LoganSquare.parse(rawPointGeoJson, GeoJsonPoint.class);
        Location location = geoJsonPoint.getEntity();

        assertEquals(10.0f, location.getLatitude(), 0);
        assertEquals(30.0f, location.getLongitude(), 0);
    }

    @Test
    public void testParseLocationToGeoJson() throws Exception {
        Location location = new Location(10, 30);
        GeoJsonPoint geoJsonPoint = GeoJsonPoint.parse(location);

        assertEquals("Point", geoJsonPoint.type);
        assertEquals(10.0f, geoJsonPoint.getLatitude(), 0);
        assertEquals(30.0f, geoJsonPoint.getLongitude(), 0);
    }
}
