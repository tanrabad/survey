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

package org.tanrabad.survey.utils.time;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PrettyTimeAgoPrinterTest {

    private static final String CURRENT_TIME = "2012-09-12 15:04:01";
    private static final DateTimeFormatter dateStringFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTime CURRENT_DATETIME = DateTime.parse(CURRENT_TIME, dateStringFormat);
    private static final long CURRENT_TIME_MILLS = CURRENT_DATETIME.getMillis();

    private CurrentTimer currentTimer = mock(CurrentTimer.class);

    @Before
    public void setUp() throws Exception {
        when(currentTimer.getInMills()).thenReturn(CURRENT_TIME_MILLS);
    }

    @Test
    public void testShowInSecondAgo() {
        TimePrettyPrinter inSecondAgoPretty = new SecondsAgoPrinter(currentTimer);

        assertEquals("1 วินาทีที่แล้ว", inSecondAgoPretty.print(
                CURRENT_TIME_MILLS - (TimePrettyPrinter.SECOND_IN_MILLS)));
        assertEquals("59 วินาทีที่แล้ว", inSecondAgoPretty.print(
                CURRENT_TIME_MILLS - (TimePrettyPrinter.SECOND_IN_MILLS * 59)));
    }

    @Test
    public void testShowInMinuteAgo() {
        TimePrettyPrinter inMiniteAgoPretty = new MinuteAgoPrinter(currentTimer);

        assertEquals("1 นาทีที่แล้ว", inMiniteAgoPretty.print(
                CURRENT_TIME_MILLS - (TimePrettyPrinter.MINITE_IN_MILLS)));
        assertEquals("59 นาทีที่แล้ว", inMiniteAgoPretty.print(
                CURRENT_TIME_MILLS - (TimePrettyPrinter.MINITE_IN_MILLS * 59)));
    }

    @Test
    public void testShowInHouseAgo() throws Exception {
        TimePrettyPrinter inHoursAgoPrinter = new HoursAgoPrinter(currentTimer);

        assertEquals("0 ชั่วโมงที่แล้ว", inHoursAgoPrinter.print(
                CURRENT_TIME_MILLS - (TimePrettyPrinter.HOUR_IN_MILLS - TimePrettyPrinter.MINITE_IN_MILLS)));
        assertEquals("1 ชั่วโมงที่แล้ว", inHoursAgoPrinter.print(
                CURRENT_TIME_MILLS - (TimePrettyPrinter.HOUR_IN_MILLS)));
        assertEquals("23 ชั่วโมงที่แล้ว", inHoursAgoPrinter.print(
                CURRENT_TIME_MILLS - (TimePrettyPrinter.HOUR_IN_MILLS * 23)));
    }

    @Test
    public void testShowInDayAgo() throws Exception {
        TimePrettyPrinter daysAgoPrinter = new DaysAgoPrinter(currentTimer);

        assertEquals("เมื่อวาน 14:04", daysAgoPrinter.print(
                CURRENT_DATETIME.minusDays(1).minusHours(1).getMillis()));
        assertEquals(null, daysAgoPrinter.print(
                CURRENT_DATETIME.minusHours(15).getMillis()));
        assertEquals("เมื่อวาน 23:04", daysAgoPrinter.print(
                CURRENT_DATETIME.minusHours(16).getMillis()));
    }

    @Test
    public void testShowfullDay() throws Exception {
        TimePrettyPrinter fulldatetimePrinter = new DateTimePrinter(currentTimer);

        assertEquals("10 ส.ค. 15:04", fulldatetimePrinter.print(
                CURRENT_DATETIME.minusMonths(1).minusDays(2).getMillis()));
        assertEquals("10 ก.ค. 2554 15:04", fulldatetimePrinter.print(
                CURRENT_DATETIME.minusYears(1).minusMonths(2).minusDays(2).getMillis()));
    }

    @Test
    public void testFactory() throws Exception {
        TimePrettyPrinter factory = new TimePrettyPrinterFactory(currentTimer);

        assertEquals("35 วินาทีที่แล้ว", factory.print(CURRENT_DATETIME.minusSeconds(35).getMillis()));
        assertEquals("1 นาทีที่แล้ว", factory.print(CURRENT_DATETIME.minusMinutes(1).getMillis()));
        assertEquals("1 ชั่วโมงที่แล้ว", factory.print(CURRENT_DATETIME.minusHours(1).getMillis()));
        assertEquals("เมื่อวาน 23:04", factory.print(CURRENT_DATETIME.minusHours(16).getMillis()));
        assertEquals("10 ก.ย. 15:04", factory.print(CURRENT_DATETIME.minusDays(2).getMillis()));
        assertEquals("12 ธ.ค. 2554 15:04", factory.print(CURRENT_DATETIME.minusMonths(9).getMillis()));
        assertEquals("12 ก.ย. 2554 15:04", factory.print(CURRENT_DATETIME.minusYears(1).getMillis()));
    }
}
