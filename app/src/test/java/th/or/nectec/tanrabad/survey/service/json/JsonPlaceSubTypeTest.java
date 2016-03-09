package th.or.nectec.tanrabad.survey.service.json;

import com.bluelinelabs.logansquare.LoganSquare;
import org.junit.Test;
import th.or.nectec.tanrabad.entity.lookup.PlaceSubType;
import th.or.nectec.tanrabad.survey.utils.ResourceFile;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class JsonPlaceSubTypeTest {

    String placeTypeList = ResourceFile.read("placeSubTypeList.json");

    @Test
    public void testParseToJsonString() throws Exception {
        List<JsonPlaceSubType> jsonPlaceTypes = LoganSquare.parseList(placeTypeList, JsonPlaceSubType.class);
        JsonPlaceSubType placeType1 = jsonPlaceTypes.get(0);
        assertEquals(1, placeType1.placeSubTypeId);
        assertEquals("สำนักงานสาธารณสุขจังหวัด", placeType1.placeSubTypeName);
        assertEquals(4, placeType1.placeTypeId);
    }

    @Test
    public void testParseToMultipleItem() throws Exception {
        List<JsonPlaceSubType> jsonPlaceTypes = LoganSquare.parseList(placeTypeList, JsonPlaceSubType.class);
        JsonPlaceSubType placeSubType1 = jsonPlaceTypes.get(0);
        assertEquals(1, placeSubType1.placeSubTypeId);
        assertEquals("สำนักงานสาธารณสุขจังหวัด", placeSubType1.placeSubTypeName);
        assertEquals(4, placeSubType1.placeTypeId);
        JsonPlaceSubType placeSubType8 = jsonPlaceTypes.get(7);
        assertEquals(8, placeSubType8.placeSubTypeId);
        assertEquals("ศูนย์สุขภาพชุมชน/บริการสาธารณสุข", placeSubType8.placeSubTypeName);
        assertEquals(4, placeSubType8.placeTypeId);
        JsonPlaceSubType placeSubType17 = jsonPlaceTypes.get(16);
        assertEquals(17, placeSubType17.placeSubTypeId);
        assertEquals("โรงงาน", placeSubType17.placeSubTypeName);
        assertEquals(5, placeSubType17.placeTypeId);
    }

    @Test
    public void testParseToContainerTypeEntity() throws Exception {
        List<JsonPlaceSubType> jsonPlaceTypes = LoganSquare.parseList(placeTypeList, JsonPlaceSubType.class);
        PlaceSubType placeType1 = jsonPlaceTypes.get(0).getEntity();
        assertEquals(1, placeType1.getId());
        assertEquals("สำนักงานสาธารณสุขจังหวัด", placeType1.getName());
        assertEquals(4, placeType1.getPlaceTypeId());
    }
}
