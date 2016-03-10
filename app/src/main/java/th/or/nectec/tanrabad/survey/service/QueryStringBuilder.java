package th.or.nectec.tanrabad.survey.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QueryStringBuilder {
    List<String> queryParams;

    QueryStringBuilder(String... params) {
        queryParams = Arrays.asList(params);
    }

    public QueryStringBuilder() {
        this.queryParams = new ArrayList<>();
    }

    public QueryStringBuilder add(String queryParam) {
        queryParams.add(queryParam);
        return this;
    }

    public String build() {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("?");
        for (String queryParam : queryParams) {
            if (queryBuilder.length() > 1 && isNotEmpty(queryParam)) {
                queryBuilder.append("&");
            }

            if (isNotEmpty(queryParam))
                queryBuilder.append(queryParam);
        }
        return queryBuilder.toString();
    }

    private boolean isNotEmpty(String queryParam) {
        return queryParam != null && !queryParam.trim().equals("");
    }
}
