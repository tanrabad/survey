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

package org.tanrabad.survey.utils.http;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class QueryStringBuilderTest {
    @Test
    public void testAddQueryString() throws Exception {
        String queryStr = new QueryStringBuilder()
                .add("geostd=4326")
                .build();
        assertEquals("?geostd=4326", queryStr);
    }

    @Test
    public void testAddMultipleQueryString() throws Exception {
        String queryStr = new QueryStringBuilder()
                .add("geostd=4326")
                .add("prov_code=10")
                .build();

        assertEquals("?geostd=4326&prov_code=10", queryStr);
    }

    @Test
    public void testAddMultipleQueryWithNullString() throws Exception {
        String queryStr = new QueryStringBuilder()
                .add("geostd=4326")
                .add(null)
                .add("prov_code=10")
                .build();

        assertEquals("?geostd=4326&prov_code=10", queryStr);
    }

    @Test
    public void testAddQueryByConstructor() throws Exception {
        String queryStr = new QueryStringBuilder("geostd=4326").build();

        assertEquals("?geostd=4326", queryStr);
    }

    @Test
    public void testAddMultipleQueryByConstructor() throws Exception {
        String queryStr = new QueryStringBuilder("geostd=4326", "prov_code=10").build();
        assertEquals("?geostd=4326&prov_code=10", queryStr);
    }
}
