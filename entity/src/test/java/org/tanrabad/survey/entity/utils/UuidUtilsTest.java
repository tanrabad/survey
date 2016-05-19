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

package org.tanrabad.survey.entity.utils;

import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class UuidUtilsTest {

    @Test
    public void testOrder() throws Exception {
        UUID uuid = UUID.fromString("58e0a7d7-eebc-11d8-9669-0800200c9a66");

        UUID orderedUuid = UuidUtils.order(uuid);

        assertEquals("11d8eebc-58e0-a7d7-9669-0800200c9a66", orderedUuid.toString());
    }

    @Test
    public void testNullMacAddress() throws Exception {
        assertNotNull(UuidUtils.generateOrdered(null));
    }
}
