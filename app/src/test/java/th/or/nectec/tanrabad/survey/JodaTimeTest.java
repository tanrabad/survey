/*
 * Copyright (c) 2016 NECTEC
 *   National Electronics and Computer Technology Center, Thailand
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package th.or.nectec.tanrabad.survey;


import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JodaTimeTest {

    private static final DateTimeFormatter RFC1123_FORMATTER =
            DateTimeFormat.forPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'");

    @Test
    public void testParseFromRfc1123() throws Exception {
        DateTime dateTime = RFC1123_FORMATTER.parseDateTime("Tue, 01 Dec 2015 17:00:00 GMT");
        assertEquals(1, dateTime.getDayOfMonth());
        assertEquals(12, dateTime.getMonthOfYear());
        assertEquals(2015, dateTime.getYear());
        assertEquals(17, dateTime.getHourOfDay());
        assertEquals(0, dateTime.getMinuteOfHour());
        assertEquals(0, dateTime.getSecondOfMinute());
    }

    @Test
    public void testToRfc1123() throws Exception {
        DateTime rfc1123 = RFC1123_FORMATTER.parseDateTime("Tue, 01 Dec 2015 17:00:00 GMT");
        DateTime dateTime = new DateTime(rfc1123.toString());

        assertEquals("Tue, 01 Dec 2015 17:00:00 GMT", RFC1123_FORMATTER.print(dateTime));

    }
}
