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

import com.bluelinelabs.logansquare.LoganSquare;

import org.joda.time.DateTime;
import org.tanrabad.survey.BuildConfig;
import org.tanrabad.survey.TanrabadApp;
import org.tanrabad.survey.entity.Place;
import org.tanrabad.survey.service.json.JsonEntomology;
import org.tanrabad.survey.utils.http.QueryStringBuilder;

import java.io.IOException;
import java.util.List;

public class EntomologyRestService extends AbsRestService<JsonEntomology> {

    public static final String PATH = "/entomology";
    private final Place place;

    public EntomologyRestService(Place place) {
        this(ImpRestServiceConfig.getInstance().getApiBaseUrl(),
                new ApiSyncInfoPreference(TanrabadApp.getInstance(), PATH), place);
    }

    public EntomologyRestService(String apiBaseUrl, ServiceLastUpdate serviceLastUpdate, Place place) {
        super(apiBaseUrl, serviceLastUpdate);
        this.place = place;
    }

    @Override
    protected String getPath() {
        return PATH;
    }

    @Override
    public String getQueryString() {
        return new QueryStringBuilder()
                .add("geostd=4326")
                .add(placeIdParam())
                .add(oneWeekIntervalParam())
                .build();
    }

    @Override
    protected List<JsonEntomology> jsonToEntityList(String responseBody) throws IOException {
        return LoganSquare.parseList(responseBody, JsonEntomology.class);
    }

    private String placeIdParam() {
        return "place_id=" + place.getId();
    }

    private String oneWeekIntervalParam() {
        DateTime dateTime = new DateTime();
        return "startdate=" + unixTime(dateTime.minusDays(BuildConfig.SURVEY_RANGE_DAY));
    }

    private long unixTime(DateTime dateTime) {
        return dateTime.getMillis() / 1000;
    }
}
