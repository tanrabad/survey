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

package org.tanrabad.survey.service.json;

import org.joda.time.DateTime;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UnixThaiDateTimeConverterTest {

    @Test
    public void testConvertUnixTimeToDateTime() throws Exception {
        assertEquals("2016-02-07T15:44:12.000+07:00", new UnixThaiDateTimeConverter()
                .getFromLong(1454834652).toString());
        assertEquals("2016-02-15T00:00:00.000+07:00", new UnixThaiDateTimeConverter()
                .getFromLong(1455469200).toString());
    }

    @Test
    public void testConvertDateTimeToUnixTime() throws Exception {
        assertEquals(1454834652, new UnixThaiDateTimeConverter()
                .convertToLong(new DateTime("2016-02-07T15:44:12.000+07:00")));
        assertEquals(1455469200, new UnixThaiDateTimeConverter()
                .convertToLong(new DateTime("2016-02-15T00:00:00.000+07:00")));
    }
}
