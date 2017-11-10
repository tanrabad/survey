package org.tanrabad.survey.nearby.matching;

import org.junit.Test;
import org.tanrabad.survey.nearby.matching.CommonPlaceTypeRemover;

import static org.junit.Assert.assertEquals;

public class CommonPlaceTypeRemoverTest {
    @Test public void testRemoveVillageName() throws Exception {
        assertEquals("บ้านxxxx", CommonPlaceTypeRemover.remove("หมู่ 1 บ้านxxxx"));
    }

    @Test public void testRemoveSchoolName() throws Exception {
        assertEquals("วัดบางไผ่", CommonPlaceTypeRemover.remove("โรงเรียนวัดบางไผ่"));
    }

    @Test public void testRemoveSchoolNameWithPunctuation() throws Exception {
        assertEquals("บางปะอินราชานุเคราะห์๑", CommonPlaceTypeRemover.remove("โรงเรียนบางปะอิน \"ราชานุเคราะห์ ๑\""));
    }

    @Test public void testRemovePunctuation() throws Exception {
        assertEquals("ทดสอบ", CommonPlaceTypeRemover.remove("วัดท @ด!สอ*บ& "));
    }
}
