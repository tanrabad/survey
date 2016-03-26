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


public abstract class UploadJob implements Job {
    protected int successCount = 0;
    protected int ioExceptionCount = 0;
    protected int restServiceExceptionCount = 0;

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

    public boolean isUploadCompletelySuccess() {
        return getFailCount() == 0 && getSuccessCount() > 0;
    }

    public boolean isUploadCompletelyFail() {
        return getFailCount() > 0 && getSuccessCount() == 0;
    }
}
