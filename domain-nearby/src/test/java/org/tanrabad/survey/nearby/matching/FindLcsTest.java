package org.tanrabad.survey.nearby.matching;

import info.debatty.java.stringsimilarity.LongestCommonSubsequence;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FindLcsTest {

    private String tuHospital = "โรงพยาบาลธรรมศาสตร์";
    private String tuSchool = "โรงเรียนอนุบาลธรรมศาสตร์";

    @Test public void findLcsLength() throws Exception {
        LongestCommonSubsequence lcs = new LongestCommonSubsequence();
        assertEquals(17, lcs.length(tuHospital, tuSchool), 0);
    }
}
