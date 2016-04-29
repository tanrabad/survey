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

import org.tanrabad.survey.TanrabadApp;
import org.tanrabad.survey.domain.WritableRepository;
import org.tanrabad.survey.service.DeleteRestService;

import java.io.IOException;

public class DeleteDataJob<T> implements Job {

    private final WritableRepository<T> repo;
    private final DeleteRestService<T> service;
    private final T[] data;
    private int successCount;

    public DeleteDataJob(WritableRepository<T> repo, DeleteRestService<T> service, T... data) {
        this.repo = repo;
        this.service = service;
        this.data = data;
    }

    @Override
    public int getId() {
        return 104924;
    }

    @Override
    public void execute() {
        for (T eachData : data) {
            try {
                boolean deletedOnServer = service.delete(eachData);
                if (deletedOnServer) {
                    repo.delete(eachData);
                    successCount++;
                }
            } catch (IOException io) {
                TanrabadApp.log(io);
            }
        }
    }

    public int getSuccessCount() {
        return successCount;
    }
}
