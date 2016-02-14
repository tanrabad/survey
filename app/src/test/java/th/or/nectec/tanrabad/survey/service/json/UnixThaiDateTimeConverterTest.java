package th.or.nectec.tanrabad.survey.service.json;

import org.joda.time.DateTime;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UnixThaiDateTimeConverterTest {

    @Test
    public void testConvertUnixTimeToDateTime() throws Exception {
        assertEquals("2016-02-07T15:44:12.000+07:00", new UnixThaiDateTimeConverter()
                .getFromLong(1454834652).toString());
    }

    @Test
    public void testConvertDateTimeToUnixTime() throws Exception {
        assertEquals(1454834652, new UnixThaiDateTimeConverter()
                .convertToLong(new DateTime("2016-02-07T15:44:12.000+07:00")));
    }
}