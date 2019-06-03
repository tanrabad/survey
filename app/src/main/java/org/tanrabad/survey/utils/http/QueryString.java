package org.tanrabad.survey.utils.http;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryString {

    private final String url;

    public QueryString(String url) {
        this.url = url;
    }

    public String getParam(String name) {
        Pattern p = Pattern.compile("[\\?\\&]"+name+"=([^&]*\\d)");
        Matcher matcher = p.matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            throw new IllegalArgumentException("Not found param ["+ name + "]");
        }
    }
}
