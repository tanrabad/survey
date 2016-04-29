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

import org.tanrabad.survey.repository.ChangedRepository;
import org.tanrabad.survey.service.RestServiceException;
import org.tanrabad.survey.service.UploadRestService;
import org.tanrabad.survey.utils.collection.CursorList;

import java.io.IOException;
import java.util.List;

public abstract class AbsUploadJob<T> implements Job {
    private final int jobId;
    protected int successCount = 0;
    protected int ioExceptionCount = 0;
    protected int restServiceExceptionCount = 0;
    private UploadRestService<T> uploadRestService;
    private ChangedRepository<T> changedRepository;
    private IOException ioException;
    private RestServiceException restServiceException;

    public AbsUploadJob(int jobId, ChangedRepository<T> changedRepository, UploadRestService<T> uploadRestService) {
        this.jobId = jobId;
        this.changedRepository = changedRepository;
        this.uploadRestService = uploadRestService;
    }

    public boolean isUploadData() {
        return getSuccessCount() > 0 || getFailCount() > 0;
    }

    public int getFailCount() {
        return getIoExceptionCount() + getRestServiceExceptionCount();
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

    @Override
    public int getId() {
        return jobId;
    }

    @Override
    public void execute() throws IOException {
        List<T> changedList = getUpdatedData(changedRepository);
        if (changedList == null)
            return;
        for (T t : changedList) {
            try {
                if (uploadData(uploadRestService, t)) {
                    changedRepository.markUnchanged(t);
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
        CursorList.close(changedList);
        throwBufferException();
    }

    public abstract List<T> getUpdatedData(ChangedRepository<T> changedRepository);

    public abstract boolean uploadData(UploadRestService<T> uploadRestService, T data) throws IOException;

    private void throwBufferException() throws IOException {
        if (ioException != null) {
            throw ioException;
        } else if (restServiceException != null) {
            throw restServiceException;
        }
    }

    public boolean isUploadCompletelySuccess() {
        return getFailCount() == 0 && getSuccessCount() > 0;
    }

    public boolean isUploadCompletelyFail() {
        return getFailCount() > 0 && getSuccessCount() == 0;
    }
}
