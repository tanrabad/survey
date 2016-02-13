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

package th.or.nectec.tanrabad.survey.job;

import th.or.nectec.tanrabad.survey.service.RestService;

import java.util.List;

public class GetDataJob<T> implements Job {

    public static final int ID = 30000;
    private RestService<T> restService;
    private List<T> data;

    public GetDataJob(RestService<T> restService) {
        this.restService = restService;
    }

    @Override
    public int id() {
        return ID;
    }

    @Override
    public void execute() {
        data = restService.getUpdate();
    }

    public List<T> getData() {
        return data;
    }
}
