package org.tanrabad.survey.utils.http;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class QueryStringTest {

    private static final String URL =
        "http://api.tanrabad.org/v2/tambon?geostd=4326&hr_code=N&page=2&per_page=200&province=";
    QueryString query = new QueryString(URL);

    @Test public void getPage() {
        assertEquals(query.getParam("page"), "2");
    }

    @Test public void getPerPage() {
        assertEquals(query.getParam("per_page"), "200");
    }

    @Test public void getFirstParam() {
        assertEquals(query.getParam("geostd"), "4326");
    }

    @Test(expected = IllegalArgumentException.class) public void getProvince() {
        query.getParam("province");
    }
}
