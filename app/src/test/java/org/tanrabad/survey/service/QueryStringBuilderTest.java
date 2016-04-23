package org.tanrabad.survey.service;

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
