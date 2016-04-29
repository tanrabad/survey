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

import android.content.Context;
import android.text.TextUtils;
import org.tanrabad.survey.R;
import org.tanrabad.survey.TanrabadApp;
import org.tanrabad.survey.service.RestServiceException;
import org.tanrabad.survey.utils.alert.Alert;
import org.tanrabad.survey.utils.android.InternetConnection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UploadJobRunner extends AbsJobRunner {

    private static final String PLACE = "สถานที่";
    private static final String SURVEY = "การสำรวจ";
    private static final String BUILDING = "อาคาร";
    private final Context context;

    private List<AbsUploadJob> uploadJobs = new ArrayList<>();

    private IOException ioException;
    private RestServiceException restServiceException;
    private boolean isManualSync;
    private OnSyncFinishListener onSyncFinishListener;

    public UploadJobRunner() {
        this(false);
    }

    public UploadJobRunner(boolean isManualSync) {
        this.isManualSync = isManualSync;
        context = TanrabadApp.getInstance();
    }

    @Override
    protected void onJobError(Job errorJob, Exception exception) {
        super.onJobError(errorJob, exception);
        if (exception instanceof IOException)
            ioException = (IOException) exception;
        else if (exception instanceof RestServiceException)
            restServiceException = (RestServiceException) exception;

        if (InternetConnection.isAvailable(context)) TanrabadApp.log(exception);
    }

    @Override
    protected void onJobDone(Job job) {
        super.onJobDone(job);
        if (job instanceof AbsUploadJob) {
            AbsUploadJob uploadJob = (AbsUploadJob) job;
            uploadJobs.add(uploadJob);

        }
    }

    @Override
    protected void onJobStart(Job startingJob) {
    }

    @Override
    protected void onRunFinish() {
        if (onSyncFinishListener != null)
            onSyncFinishListener.onSyncFinish();

        if (uploadJobs.size() == 0) {
            return;
        }

        showUploadResultMsg();
    }

    private void showUploadResultMsg() {
        String message = "";
        for (AbsUploadJob uploadJob : uploadJobs) {
            if (!uploadJob.isUploadData())
                continue;

            String dataType = getDataType(uploadJob);
            if (uploadJob instanceof PostDataJob)
                message += String.format(context.getString(R.string.upload_data_type), dataType)
                        + appendUploadSuccessMessage(uploadJob) + appendUploadFailedMessage(uploadJob);
            else if (uploadJob instanceof PutDataJob)
                message += String.format(context.getString(R.string.update_data_type), dataType)
                        + appendUploadSuccessMessage(uploadJob) + appendUploadFailedMessage(uploadJob);
        }

        if (!TextUtils.isEmpty(message))
            Alert.mediumLevel().show(message.trim());
    }


    private String getDataType(AbsUploadJob uploadJob) {
        String dataType = null;
        if (uploadJob.getId() == UploadJobBuilder.PLACE_POST_ID
                || uploadJob.getId() == UploadJobBuilder.PLACE_PUT_ID) {
            dataType = PLACE;
        } else if (uploadJob.getId() == UploadJobBuilder.BUILDING_POST_ID
                || uploadJob.getId() == UploadJobBuilder.BUILDING_PUT_ID) {
            dataType = BUILDING;
        } else if (uploadJob.getId() == UploadJobBuilder.SURVEY_POST_ID
                || uploadJob.getId() == UploadJobBuilder.SURVEY_PUT_ID) {
            dataType = SURVEY;
        }
        return dataType;
    }

    private String appendUploadSuccessMessage(AbsUploadJob uploadJob) {
        return uploadJob.getSuccessCount() > 0
                ? String.format(context.getString(R.string.upload_data_success), uploadJob.getSuccessCount())
                : "";
    }

    private String appendUploadFailedMessage(AbsUploadJob uploadJob) {
        String space = (uploadJob.getSuccessCount() > 0) ? " " : "";
        String message = (uploadJob.getFailCount() > 0)
                ? String.format(context.getString(R.string.upload_data_fail), uploadJob.getFailCount()) + "\n"
                : "\n";
        return space + message;
    }

    public UploadJobRunner setOnSyncFinishListener(OnSyncFinishListener onSyncFinishListener) {
        this.onSyncFinishListener = onSyncFinishListener;
        return this;
    }

    public interface OnSyncFinishListener {
        void onSyncFinish();
    }
}
