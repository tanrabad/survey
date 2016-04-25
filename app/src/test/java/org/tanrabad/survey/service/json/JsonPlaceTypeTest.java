package org.tanrabad.survey.service.json;

import com.bluelinelabs.logansquare.LoganSquare;

import org.junit.Test;

import java.util.List;

import org.tanrabad.survey.entity.lookup.PlaceType;
import org.tanrabad.survey.utils.ResourceFile;

import static org.junit.Assert.assertEquals;

public class JsonPlaceTypeTest {

    private String placeTypeList = ResourceFile.read("placeTypeList.json");

    @Test
    public void testParseToJsonString() throws Exception {
        List<JsonPlaceType> jsonPlaceTypes = LoganSquare.parseList(placeTypeList, JsonPlaceType.class);
        JsonPlaceType placeType1 = jsonPlaceTypes.get(0);
        assertEquals(1, placeType1.placeTypeId);
        assertEquals("หมู่บ้าน/ชุมชน", placeType1.placeTypeName);
    }

    @Test
    public void testParseToMultipleItem() throws Exception {
        List<JsonPlaceType> jsonPlaceTypes = LoganSquare.parseList(placeTypeList, JsonPlaceType.class);
        JsonPlaceType placeType1 = jsonPlaceTypes.get(0);
        assertEquals(1, placeType1.placeTypeId);
        assertEquals("หมู่บ้าน/ชุมชน", placeType1.placeTypeName);
        JsonPlaceType placeType3 = jsonPlaceTypes.get(2);
        assertEquals(3, placeType3.placeTypeId);
        assertEquals("สถานศึกษา", placeType3.placeTypeName);
        JsonPlaceType placeType6 = jsonPlaceTypes.get(5);
        assertEquals(6, placeType6.placeTypeId);
        assertEquals("ที่พักชั่วคราว", placeType6.placeTypeName);
    }

    @Test
    public void testParseToContainerTypeEntity() throws Exception {
        List<JsonPlaceType> jsonPlaceTypes = LoganSquare.parseList(placeTypeList, JsonPlaceType.class);
        PlaceType placeType1 = jsonPlaceTypes.get(0).getEntity();
        assertEquals(1, placeType1.getId());
        assertEquals("หมู่บ้าน/ชุมชน", placeType1.getName());
    }
}
