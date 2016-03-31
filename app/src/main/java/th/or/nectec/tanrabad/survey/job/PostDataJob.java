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

import java.io.IOException;
import java.util.List;

import th.or.nectec.tanrabad.survey.repository.ChangedRepository;
import th.or.nectec.tanrabad.survey.service.UploadRestService;

public class PostDataJob<T> extends UploadJob<T> {

    public PostDataJob(int jobId, ChangedRepository<T> changedRepository, UploadRestService<T> uploadRestService) {
        super(jobId, changedRepository, uploadRestService);
    }

    @Override
    public boolean uploadData(UploadRestService<T> uploadRestService, T data) throws IOException {
        return uploadRestService.post(data);
    }

    @Override
    public List<T> getUpdatedData(ChangedRepository changedRepository) {
        return changedRepository.getAdd();
    }
}
