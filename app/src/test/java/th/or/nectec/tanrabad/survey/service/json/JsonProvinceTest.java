package org.tanrabad.survey.service.json;

import com.bluelinelabs.logansquare.LoganSquare;
import org.junit.Test;
import th.or.nectec.tanrabad.entity.lookup.Province;
import org.tanrabad.survey.utils.ResourceFile;

import static org.junit.Assert.assertEquals;

public class JsonProvinceTest {
    @Test
    public void testParseToJsonString() throws Exception {
        JsonProvince jsonProvince = LoganSquare.parse(ResourceFile.read("province.json"), JsonProvince.class);
        assertEquals("50", jsonProvince.provinceCode);
        assertEquals("เชียงใหม่", jsonProvince.provinceName);
        assertEquals(true, jsonProvince.boundary != null);
    }

    @Test
    public void testParseToProvinceEntity() throws Exception {
        JsonProvince jsonProvince = LoganSquare.parse(ResourceFile.read("province.json"), JsonProvince.class);
        Province district = jsonProvince.getEntity();
        assertEquals("50", district.getCode());
        assertEquals("เชียงใหม่", district.getName());
        assertEquals(true, jsonProvince.boundary != null);
    }
}
