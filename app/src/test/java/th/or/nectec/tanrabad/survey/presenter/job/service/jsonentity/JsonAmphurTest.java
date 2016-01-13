package th.or.nectec.tanrabad.survey.presenter.job.service.jsonentity;

import com.bluelinelabs.logansquare.LoganSquare;
import org.junit.Test;
import th.or.nectec.tanrabad.entity.District;
import th.or.nectec.tanrabad.entity.Location;
import th.or.nectec.tanrabad.survey.utils.ResourceFile;

import static org.junit.Assert.assertEquals;

public class JsonAmphurTest {
    @Test
    public void testParseToJsonString() throws Exception {
        JsonAmphur jsonAmphur = LoganSquare.parse(ResourceFile.read("amphur.json"), JsonAmphur.class);

        assertEquals("1202", jsonAmphur.amphurCode);
        assertEquals("บางกรวย", jsonAmphur.amphurName);
        assertEquals("12", jsonAmphur.provinceCode);
        assertEquals("นนทบุรี", jsonAmphur.provinceName);
        assertEquals(new Location(13.8358835573344, 100.426092280359), jsonAmphur.boundary.getPolygon(0).getBoundary().get(0));
        assertEquals(new Location(13.8328855111975, 100.428855074237), jsonAmphur.boundary.getPolygon(0).getBoundary().get(1));
        assertEquals(new Location(13.8279769668251, 100.439485944792), jsonAmphur.boundary.getPolygon(0).getBoundary().get(2));
    }

    @Test
    public void testParseToDistrictEntity() throws Exception {
        JsonAmphur jsonAmphur = LoganSquare.parse(ResourceFile.read("amphur.json"), JsonAmphur.class);
        District district = jsonAmphur.getEntity();

        assertEquals("1202", district.getCode());
        assertEquals("บางกรวย", district.getName());
        assertEquals("12", district.getProvinceCode());
        assertEquals("นนทบุรี", district.getProvinceName());
        assertEquals(new Location(13.8358835573344, 100.426092280359), district.getBoundary().get(0).getBoundary().get(0));
        assertEquals(new Location(13.8328855111975, 100.428855074237), district.getBoundary().get(0).getBoundary().get(1));
        assertEquals(new Location(13.8279769668251, 100.439485944792), district.getBoundary().get(0).getBoundary().get(2));
    }
}
