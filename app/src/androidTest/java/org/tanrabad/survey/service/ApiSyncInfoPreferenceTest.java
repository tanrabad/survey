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

package org.tanrabad.survey.service;

import android.support.test.InstrumentationRegistry;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class ApiSyncInfoPreferenceTest {


    @Test
    public void testSaveThenGet() throws Exception {
        ApiSyncInfoPreference lastUpdatePreference = new ApiSyncInfoPreference(
                InstrumentationRegistry.getTargetContext(), "test");
        lastUpdatePreference.save("Mon, 30 Nov 2015 17:00:00 GMT");

        assertEquals("Mon, 30 Nov 2015 17:00:00 GMT", lastUpdatePreference.get());
    }
}
