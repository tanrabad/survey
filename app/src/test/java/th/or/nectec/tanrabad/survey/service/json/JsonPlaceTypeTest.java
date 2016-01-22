package th.or.nectec.tanrabad.survey.service.json;

import com.bluelinelabs.logansquare.LoganSquare;
import org.junit.Test;
import th.or.nectec.tanrabad.entity.PlaceType;
import th.or.nectec.tanrabad.survey.utils.ResourceFile;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class JsonPlaceTypeTest {

    String placeTypeList = ResourceFile.read("placeTypeList.json");

    @Test
    public void testParseToJsonString() throws Exception {
        List<JsonPlaceType> jsonPlaceTypes = LoganSquare.parseList(placeTypeList, JsonPlaceType.class);
        JsonPlaceType placeType1 = jsonPlaceTypes.get(0);
        assertEquals(1, placeType1.placeTypeID);
        assertEquals("หมู่บ้าน/ชุมชน", placeType1.placeTypeName);
    }

    @Test
    public void testParseToMultipleItem() throws Exception {
        List<JsonPlaceType> jsonPlaceTypes = LoganSquare.parseList(placeTypeList, JsonPlaceType.class);
        JsonPlaceType placeType1 = jsonPlaceTypes.get(0);
        assertEquals(1, placeType1.placeTypeID);
        assertEquals("หมู่บ้าน/ชุมชน", placeType1.placeTypeName);
        JsonPlaceType placeType3 = jsonPlaceTypes.get(2);
        assertEquals(3, placeType3.placeTypeID);
        assertEquals("สถานศึกษา", placeType3.placeTypeName);
        JsonPlaceType placeType6 = jsonPlaceTypes.get(5);
        assertEquals(6, placeType6.placeTypeID);
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
