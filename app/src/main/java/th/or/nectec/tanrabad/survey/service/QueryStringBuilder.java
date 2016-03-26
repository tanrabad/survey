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
        queryBuilder.append('?');
        for (String queryParam : queryParams) {
            if (queryBuilder.length() > 1 && isNotEmpty(queryParam)) {
                queryBuilder.append('&');
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
