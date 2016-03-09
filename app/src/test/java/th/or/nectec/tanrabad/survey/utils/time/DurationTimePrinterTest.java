/*
 * Copyright (c) 2015 NECTEC
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

package th.or.nectec.tanrabad.survey.utils.time;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static th.or.nectec.tanrabad.survey.utils.time.DurationTimePrinter.print;

public class DurationTimePrinterTest {

    private static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTime START_DATE = DateTime.parse("2014-09-16 00:00:00", FORMATTER);

    @Test
    public void testPrintDurationUnder60Minute() throws Exception {
        assertEquals("00:30", print(START_DATE, START_DATE.plusSeconds(30)));
        assertEquals("01:30", print(START_DATE, START_DATE.plusMinutes(1).plusSeconds(30)));
        assertEquals("59:59", print(START_DATE, START_DATE.plusMinutes(59).plusSeconds(59)));
    }

    @Test
    public void testPrintDurationOver60Minute() throws Exception {
        assertEquals("1:00:00", print(START_DATE, START_DATE.plusHours(1)));
        assertEquals("10:06:15", print(START_DATE, START_DATE.plusHours(10).plusMinutes(5).plusSeconds(75)));
    }
}
