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

import th.or.nectec.tanrabad.domain.WritableRepository;
import th.or.nectec.tanrabad.survey.service.RestService;

import java.io.IOException;
import java.util.List;

public class WritableRepoUpdateJob<T> implements Job {

    public static final int ID = 192384;
    WritableRepository<T> repository;
    RestService<T> restService;

    public <K extends RestService<T>> WritableRepoUpdateJob(K restService, WritableRepository<T> repository) {
        this.restService = restService;
        this.repository = repository;
    }

    @Override
    public int id() {
        return ID;
    }

    @Override
    public void execute() throws IOException {
        do {
            List<T> update = restService.getUpdate();
            if (!update.isEmpty())
                repository.updateOrInsert(update);
        } while (restService.hasNextRequest());
    }
}
