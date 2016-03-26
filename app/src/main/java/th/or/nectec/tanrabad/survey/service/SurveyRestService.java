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

import com.bluelinelabs.logansquare.LoganSquare;
import th.or.nectec.tanrabad.entity.Survey;
import th.or.nectec.tanrabad.survey.TanrabadApp;
import th.or.nectec.tanrabad.survey.service.json.JsonSurvey;

import java.io.IOException;
import java.util.List;

public class SurveyRestService extends AbsUploadRestService<Survey> {

    public static final String PATH = "/survey";

    public SurveyRestService() {
        this(BASE_API, new ServiceLastUpdatePreference(TanrabadApp.getInstance(), PATH));
    }

    public SurveyRestService(String baseApi, ServiceLastUpdate serviceLastUpdate) {
        super(baseApi, serviceLastUpdate);
    }

    @Override
    protected String entityToJsonString(Survey data) throws IOException {
        return LoganSquare.serialize(JsonSurvey.parse(data));
    }

    @Override
    protected String getId(Survey data) {
        return data.getId().toString();
    }

    @Override
    protected String getPath() {
        return PATH;
    }

    @Override
    protected List<Survey> jsonToEntityList(String responseBody) {
        throw new IllegalArgumentException("Survey rest service not support convert to entity.");
    }
}
