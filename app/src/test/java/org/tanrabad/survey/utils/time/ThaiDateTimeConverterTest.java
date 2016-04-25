package org.tanrabad.survey.utils.time;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;


public class ThaiDateTimeConverterTest {
    @Test
    public void testParseFromUtctoIct() throws Exception {
        String utcTime = "2015-01-11T03:00:00.000Z";
        assertEquals("2015-01-11T10:00:00.000+07:00", ThaiDateTimeConverter.convert(utcTime).toString());
    }

    @Test
    public void testParseFromIcttoIct() throws Exception {
        String ictTime = "2015-01-11T10:00:00.000+07:00";
        assertEquals("2015-01-11T10:00:00.000+07:00", ThaiDateTimeConverter.convert(ictTime).toString());
    }
}
