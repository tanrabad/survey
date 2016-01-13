package th.or.nectec.tanrabad.survey.presenter.job.service.jsonentity;

import com.bluelinelabs.logansquare.LoganSquare;
import org.junit.Test;
import th.or.nectec.tanrabad.entity.Location;
import th.or.nectec.tanrabad.entity.Subdistrict;
import th.or.nectec.tanrabad.survey.utils.ResourceFile;

import static org.junit.Assert.assertEquals;

public class JsonTambonTest {
    @Test
    public void testParseToJsonString() throws Exception {
        JsonTambon jsonTambon = LoganSquare.parse(ResourceFile.read("tambon.json"), JsonTambon.class);

        assertEquals("840212", jsonTambon.tambonCode);
        assertEquals("ทุ่งรัง", jsonTambon.tambonName);
        assertEquals("8402", jsonTambon.amphurCode);
        assertEquals("กาญจนดิษฐ์", jsonTambon.amphurName);
        assertEquals("84", jsonTambon.provinceCode);
        assertEquals("สุราษฎร์ธานี", jsonTambon.provinceName);
        assertEquals(new Location(9.10229074425328, 99.3916034624674), jsonTambon.boundary.getPolygon(0).getBoundary().get(0));
        assertEquals(new Location(9.10146829966842, 99.3925836929561), jsonTambon.boundary.getPolygon(0).getBoundary().get(1));
        assertEquals(new Location(9.10105544929067, 99.3943237020771), jsonTambon.boundary.getPolygon(0).getBoundary().get(2));
    }

    @Test
    public void testParseToTambonEntity() throws Exception {
        JsonTambon jsonTambon = LoganSquare.parse(ResourceFile.read("tambon.json"), JsonTambon.class);
        Subdistrict subdistrict = jsonTambon.getEntity();

        assertEquals("840212", subdistrict.getCode());
        assertEquals("ทุ่งรัง", subdistrict.getName());
        assertEquals("8402", subdistrict.getAmphurCode());
        assertEquals("กาญจนดิษฐ์", subdistrict.getAmphurName());
        assertEquals("84", subdistrict.getProvinceCode());
        assertEquals("สุราษฎร์ธานี", subdistrict.getProvinceName());
        assertEquals(new Location(9.10229074425328, 99.3916034624674), subdistrict.getBoundary().get(0).getBoundary().get(0));
        assertEquals(new Location(9.10146829966842, 99.3925836929561), subdistrict.getBoundary().get(0).getBoundary().get(1));
        assertEquals(new Location(9.10105544929067, 99.3943237020771), subdistrict.getBoundary().get(0).getBoundary().get(2));
    }
}
