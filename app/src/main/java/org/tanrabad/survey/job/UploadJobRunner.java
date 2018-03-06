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
import java.util.ArrayList;
import java.util.List;
import org.tanrabad.survey.R;
import org.tanrabad.survey.TanrabadApp;
import org.tanrabad.survey.repository.persistence.DbBuildingRepository;
import org.tanrabad.survey.repository.persistence.DbPlaceRepository;
import org.tanrabad.survey.repository.persistence.DbSurveyRepository;
import org.tanrabad.survey.service.BuildingRestService;
import org.tanrabad.survey.service.PlaceRestService;
import org.tanrabad.survey.service.SurveyRestService;
import org.tanrabad.survey.utils.alert.Alert;
import org.tanrabad.survey.utils.android.InternetConnection;

public class UploadJobRunner extends AbsJobRunner {

    private static final String PLACE = "สถานที่";
    private static final String SURVEY = "การสำรวจ";
    private static final String BUILDING = "อาคาร";
    private final Context context;

    private List<AbsUploadJob> uploadJobs = new ArrayList<>();

    private OnSyncFinishListener onSyncFinishListener;

    public UploadJobRunner() {
        context = TanrabadApp.getInstance();
    }

    @Override
    protected void onJobError(Job errorJob, Exception exception) {
        super.onJobError(errorJob, exception);

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
        StringBuilder message = new StringBuilder();
        for (AbsUploadJob uploadJob : uploadJobs) {
            if (!uploadJob.isUploaded())
                continue;

            String dataType = getDataType(uploadJob);
            if (uploadJob instanceof PostDataJob)
                message.append(
                    String.format(context.getString(R.string.upload_data_type), dataType))
                    .append(appendUploadSuccessMessage(uploadJob))
                    .append(appendUploadFailedMessage(uploadJob));
            else if (uploadJob instanceof PutDataJob)
                message.append(
                    String.format(context.getString(R.string.update_data_type), dataType))
                    .append(appendUploadSuccessMessage(uploadJob))
                    .append(appendUploadFailedMessage(uploadJob));
        }

        if (!TextUtils.isEmpty(message.toString()))
            Alert.mediumLevel().show(message.toString().trim());
    }


    private String getDataType(AbsUploadJob uploadJob) {
        String dataType = null;
        if (uploadJob.getId() == Builder.PLACE_POST_ID
                || uploadJob.getId() == Builder.PLACE_PUT_ID) {
            dataType = PLACE;
        } else if (uploadJob.getId() == Builder.BUILDING_POST_ID
                || uploadJob.getId() == Builder.BUILDING_PUT_ID) {
            dataType = BUILDING;
        } else if (uploadJob.getId() == Builder.SURVEY_POST_ID
                || uploadJob.getId() == Builder.SURVEY_PUT_ID) {
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

    public static class Builder {
        public static final int PLACE_POST_ID = 101;
        public static final int BUILDING_POST_ID = 102;
        public static final int SURVEY_POST_ID = 103;
        public static final int PLACE_PUT_ID = 201;
        public static final int BUILDING_PUT_ID = 202;
        public static final int SURVEY_PUT_ID = 203;

        public PostDataJob placePostDataJob = new PostDataJob<>(
                PLACE_POST_ID, new DbPlaceRepository(TanrabadApp.getInstance()), new PlaceRestService());
        public PostDataJob buildingPostDataJob = new PostDataJob<>(
                BUILDING_POST_ID, new DbBuildingRepository(TanrabadApp.getInstance()), new BuildingRestService());
        public PostDataJob surveyPostDataJob = new PostDataJob<>(
                SURVEY_POST_ID, new DbSurveyRepository(TanrabadApp.getInstance()), new SurveyRestService());
        public PutDataJob placePutDataJob = new PutDataJob<>(
                PLACE_PUT_ID, new DbPlaceRepository(TanrabadApp.getInstance()), new PlaceRestService());
        public PutDataJob buildingPutDataJob = new PutDataJob<>(
                BUILDING_PUT_ID, new DbBuildingRepository(TanrabadApp.getInstance()), new BuildingRestService());
        public PutDataJob surveyPutDataJob = new PutDataJob<>(
                SURVEY_PUT_ID, new DbSurveyRepository(TanrabadApp.getInstance()), new SurveyRestService());

        public List<Job> getJobs() {
            List<Job> jobs = new ArrayList<>();
            jobs.add(placePostDataJob);
            jobs.add(buildingPostDataJob);
            jobs.add(surveyPostDataJob);
            jobs.add(placePutDataJob);
            jobs.add(buildingPutDataJob);
            jobs.add(surveyPutDataJob);
            return jobs;
        }
    }
}
