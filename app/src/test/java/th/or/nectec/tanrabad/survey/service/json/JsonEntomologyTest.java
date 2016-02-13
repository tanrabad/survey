package th.or.nectec.tanrabad.survey.service.json;

import com.bluelinelabs.logansquare.LoganSquare;
import org.junit.Test;
import th.or.nectec.tanrabad.survey.utils.ResourceFile;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class JsonEntomologyTest {


    @Test
    public void testParseEntomoLogyToObject() throws Exception {
        List<JsonEntomology> jsonEntomologyList = LoganSquare.parseList(
                ResourceFile.read("entomologyList1Item.json"), JsonEntomology.class);

        JsonEntomology jsonEntomology1 = jsonEntomologyList.get(0);
        assertEquals("6e79ca31-d0da-fc50-64d2-ac403dfff644", jsonEntomology1.placeID.toString());
        assertEquals("หมู่ 5 บ้านท่าน้ำ", jsonEntomology1.placeName);
        assertEquals(1, jsonEntomology1.placeType);
        assertEquals(14.078273636945516, jsonEntomology1.location.getLatitude(), 0);
        assertEquals(100.60137651189666, jsonEntomology1.location.getLongitude(), 0);
        assertEquals("บางเขน", jsonEntomology1.tambonName);
        assertEquals("เมืองนนทบุรี", jsonEntomology1.amphurName);
        assertEquals("นนทบุรี", jsonEntomology1.provinceName);
        assertEquals(119, jsonEntomology1.numSurveyedContainer);
        assertEquals(30, jsonEntomology1.numFoundContainers);
        assertEquals("2015-07-03T00:00:00.000+07:00", jsonEntomology1.dateSurveyed.toString());
        assertEquals(54.00, jsonEntomology1.hiValue, 0);
        assertEquals(25.00, jsonEntomology1.ciValue, 0);
        assertEquals(125.00, jsonEntomology1.biValue, 0);
        assertEquals("แจกัน", jsonEntomology1.keyContainerIn.get(0).containerName);
        assertEquals("น้ำใช้", jsonEntomology1.keyContainerIn.get(1).containerName);
        assertEquals("ที่รองกันมด", jsonEntomology1.keyContainerIn.get(2).containerName);
        assertEquals("ภาชนะที่ไม่ใช้", jsonEntomology1.keyContainerOut.get(0).containerName);
        assertEquals("ยางรถยนต์เก่า", jsonEntomology1.keyContainerOut.get(1).containerName);
        assertEquals("อื่น ๆ (ที่ใช้ประโยชน์)", jsonEntomology1.keyContainerOut.get(2).containerName);
    }

    @Test
    public void testParseMultipleEntomoLogyToObjects() throws Exception {
        List<JsonEntomology> jsonEntomologyList = LoganSquare.parseList(
                ResourceFile.read("entomologyList1Item.json"), JsonEntomology.class);

        JsonEntomology jsonEntomology1 = jsonEntomologyList.get(0);
        assertEquals("6e79ca31-d0da-fc50-64d2-ac403dfff644", jsonEntomology1.placeID.toString());
        assertEquals("หมู่ 5 บ้านท่าน้ำ", jsonEntomology1.placeName);
        assertEquals(1, jsonEntomology1.placeType);
        assertEquals(14.078273636945516, jsonEntomology1.location.getLatitude(), 0);
        assertEquals(100.60137651189666, jsonEntomology1.location.getLongitude(), 0);
        assertEquals("บางเขน", jsonEntomology1.tambonName);
        assertEquals("เมืองนนทบุรี", jsonEntomology1.amphurName);
        assertEquals("นนทบุรี", jsonEntomology1.provinceName);
        assertEquals(119, jsonEntomology1.numSurveyedContainer);
        assertEquals(30, jsonEntomology1.numFoundContainers);
        assertEquals("2015-07-03T00:00:00.000+07:00", jsonEntomology1.dateSurveyed.toString());
        assertEquals(54.00, jsonEntomology1.hiValue, 0);
        assertEquals(25.00, jsonEntomology1.ciValue, 0);
        assertEquals(125.00, jsonEntomology1.biValue, 0);
        assertEquals("แจกัน", jsonEntomology1.keyContainerIn.get(0).containerName);
        assertEquals("น้ำใช้", jsonEntomology1.keyContainerIn.get(1).containerName);
        assertEquals("ที่รองกันมด", jsonEntomology1.keyContainerIn.get(2).containerName);
        assertEquals("ภาชนะที่ไม่ใช้", jsonEntomology1.keyContainerOut.get(0).containerName);
        assertEquals("ยางรถยนต์เก่า", jsonEntomology1.keyContainerOut.get(1).containerName);
        assertEquals("อื่น ๆ (ที่ใช้ประโยชน์)", jsonEntomology1.keyContainerOut.get(2).containerName);

        JsonEntomology jsonEntomology2 = jsonEntomologyList.get(1);
        assertEquals("a9956d82-3c3a-5bfa-ae0b-2c0e2b786810", jsonEntomology2.placeID.toString());
        assertEquals("หมู่ 9 บ้านวัดแดง", jsonEntomology2.placeName);
        assertEquals(1, jsonEntomology2.placeType);
        assertEquals(14.090106321804223, jsonEntomology2.location.getLatitude(), 0);
        assertEquals(100.60678079724312, jsonEntomology2.location.getLongitude(), 0);
        assertEquals("ท่าทราย", jsonEntomology2.tambonName);
        assertEquals("เมืองนนทบุรี", jsonEntomology2.amphurName);
        assertEquals("นนทบุรี", jsonEntomology2.provinceName);
        assertEquals(77, jsonEntomology2.numSurveyedContainer);
        assertEquals(17, jsonEntomology2.numFoundContainers);
        assertEquals("2014-07-16T00:00:00.000+07:00", jsonEntomology2.dateSurveyed.toString());
        assertEquals(75.0, jsonEntomology2.hiValue, 0);
        assertEquals(22.0, jsonEntomology2.ciValue, 0);
        assertEquals(425.0, jsonEntomology2.biValue, 0);
        assertEquals("อื่น ๆ (ที่ใช้ประโยชน์)", jsonEntomology2.keyContainerIn.get(0).containerName);
        assertEquals("จานรองกระถาง", jsonEntomology2.keyContainerIn.get(1).containerName);
        assertEquals("ยางรถยนต์เก่า,น้ำใช้", jsonEntomology2.keyContainerIn.get(2).containerName);
        assertEquals("แจกัน", jsonEntomology2.keyContainerOut.get(0).containerName);
        assertEquals("น้ำใช้", jsonEntomology2.keyContainerOut.get(1).containerName);
        assertEquals("ที่รองกันมด", jsonEntomology2.keyContainerOut.get(2).containerName);

        JsonEntomology jsonEntomology3 = jsonEntomologyList.get(2);
        assertEquals("86df6a0f-4368-c972-d4a1-15574868d085", jsonEntomology3.placeID.toString());
        assertEquals("หมู่ 1 บ้านท่าลาน", jsonEntomology3.placeName);
        assertEquals(1, jsonEntomology3.placeType);
        assertEquals(14.9733720169419, jsonEntomology3.location.getLatitude(), 0);
        assertEquals(103.1926757198, jsonEntomology3.location.getLongitude(), 0);
        assertEquals("ท่าแร้ง", jsonEntomology3.tambonName);
        assertEquals("เขตบางเขน", jsonEntomology3.amphurName);
        assertEquals("กรุงเทพมหานคร", jsonEntomology3.provinceName);
        assertEquals(61, jsonEntomology3.numSurveyedContainer);
        assertEquals(15, jsonEntomology3.numFoundContainers);
        assertEquals("2015-03-12T00:00:00.000+07:00", jsonEntomology3.dateSurveyed.toString());
        assertEquals(66.0, jsonEntomology3.hiValue, 0);
        assertEquals(6.0, jsonEntomology3.ciValue, 0);
        assertEquals(66.0, jsonEntomology3.biValue, 0);
        assertEquals("แจกัน,น้ำใช้", jsonEntomology3.keyContainerIn.get(0).containerName);
        assertEquals("ที่รองกันมด,จานรองกระถาง,น้ำดื่ม", jsonEntomology3.keyContainerIn.get(1).containerName);
        assertEquals("ภาชนะที่ไม่ใช้,ยางรถยนต์เก่า,อื่น ๆ (ที่ใช้ประโยชน์)", jsonEntomology3.keyContainerIn.get(2).containerName);
        assertEquals("แจกัน", jsonEntomology3.keyContainerOut.get(0).containerName);
        assertEquals("น้ำใช้", jsonEntomology3.keyContainerOut.get(1).containerName);
        assertEquals("ที่รองกันมด", jsonEntomology3.keyContainerOut.get(2).containerName);
    }
}
