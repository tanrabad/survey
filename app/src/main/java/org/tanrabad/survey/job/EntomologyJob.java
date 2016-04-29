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

package org.tanrabad.survey.job;

import org.tanrabad.survey.entity.Place;
import org.tanrabad.survey.service.EntomologyRestService;
import org.tanrabad.survey.service.RestService;
import org.tanrabad.survey.service.json.JsonEntomology;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class EntomologyJob implements Job {

    private static final int ID = 30000;
    private RestService<JsonEntomology> restService;
    private JsonEntomology data;

    public EntomologyJob(Place place) {
        this.restService = new EntomologyRestService(place);
    }

    @Override
    public int getId() {
        return ID;
    }

    @Override
    public void execute() throws IOException {
        List<JsonEntomology> entomologyList = new ArrayList<>();
        do {
            entomologyList.addAll(restService.getUpdate());
        } while (restService.hasNextRequest());
        data = entomologyList.isEmpty() ? null : entomologyList.get(entomologyList.size() - 1);
    }

    public JsonEntomology getData() {
        return data;
    }
}
