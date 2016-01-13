package th.or.nectec.tanrabad.survey.service.jsonentity;

import com.bluelinelabs.logansquare.LoganSquare;
import org.junit.Test;
import th.or.nectec.tanrabad.entity.District;
import th.or.nectec.tanrabad.survey.utils.ResourceFile;

import static org.junit.Assert.assertEquals;

public class JsonAmphurTest {
    @Test
    public void testParseToJsonString() throws Exception {
        JsonAmphur jsonAmphur = LoganSquare.parse(ResourceFile.read("amphur.json"), JsonAmphur.class);

        assertEquals("1202", jsonAmphur.amphurCode);
        assertEquals("บางกรวย", jsonAmphur.amphurName);
        assertEquals("12", jsonAmphur.provinceCode);
        assertEquals(true, jsonAmphur.boundary.getPolygon(0) != null);
    }

    @Test
    public void testParseToDistrictEntity() throws Exception {
        JsonAmphur jsonAmphur = LoganSquare.parse(ResourceFile.read("amphur.json"), JsonAmphur.class);
        District district = jsonAmphur.getEntity();

        assertEquals("1202", district.getCode());
        assertEquals("บางกรวย", district.getName());
        assertEquals("12", district.getProvinceCode());
        assertEquals(true, district.getBoundary().get(0) != null);
    }
}
