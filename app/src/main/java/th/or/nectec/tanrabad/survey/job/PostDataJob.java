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

import th.or.nectec.tanrabad.survey.repository.ChangedRepository;
import th.or.nectec.tanrabad.survey.service.RestServiceException;
import th.or.nectec.tanrabad.survey.service.UploadRestService;

import java.io.IOException;
import java.util.List;

public class PostDataJob<T> implements Job {

    public static final int ID = 90000;
    IOException ioException;
    RestServiceException restServiceException;
    private ChangedRepository<T> changedRepository;
    private UploadRestService<T> uploadRestService;
    private int successCount = 0;
    private int ioExceptionCount = 0;
    private int restServiceExceptionCount = 0;

    public PostDataJob(ChangedRepository<T> changedRepository, UploadRestService<T> uploadRestService) {
        this.changedRepository = changedRepository;
        this.uploadRestService = uploadRestService;
    }

    @Override
    public int id() {
        return ID;
    }

    @Override
    public void execute() throws Exception {
        List<T> addList = changedRepository.getAdd();
        if (addList == null)
            return;
        for (T eachData : addList) {
            try {
                if (uploadRestService.post(eachData)) {
                    changedRepository.markUnchanged(eachData);
                    successCount++;
                }
            } catch (IOException exception) {
                ioException = exception;
                ioExceptionCount++;
            } catch (RestServiceException exception) {
                restServiceException = exception;
                restServiceExceptionCount++;
            }
        }

        throwBufferException();
    }

    private void throwBufferException() throws IOException {
        if (ioException != null) {
            throw ioException;
        } else if (restServiceException != null) {
            throw restServiceException;
        }
    }

    public int getSuccessCount() {
        return successCount;
    }

    public int getIoExceptionCount() {
        return ioExceptionCount;
    }

    public int getRestServiceExceptionCount() {
        return restServiceExceptionCount;
    }
}
