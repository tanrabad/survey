package th.or.nectec.tanrabad.survey.job;

import android.content.Context;

import java.io.IOException;

import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.TanrabadApp;
import th.or.nectec.tanrabad.survey.service.RestServiceException;
import th.or.nectec.tanrabad.survey.utils.alert.Alert;
import th.or.nectec.tanrabad.survey.utils.android.InternetConnection;

public class SyncJobRunner extends AbsJobRunner {

    private static final String PLACE = "สถานที่";
    private static final String SURVEY = "สำรวจ";
    private static final String BUILDING = "อาคาร";
    private final SyncJobBuilder syncJobBuilder;
    private final Context context;
    String syncStatusMsg = "";

    IOException ioException;
    RestServiceException restServiceException;

    public SyncJobRunner() {
        syncJobBuilder = new SyncJobBuilder();
        syncJobBuilder.build(this);
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
        if (job.equals(syncJobBuilder.placePostDataJob)) {
            buildUploadStatusMessage(job, PLACE);
        } else if (job.equals(syncJobBuilder.placePutDataJob)) {
            buildUploadStatusMessage(job, PLACE);
        } else if (job.equals(syncJobBuilder.buildingPostDataJob)) {
            buildUploadStatusMessage(job, BUILDING);
        } else if (job.equals(syncJobBuilder.buildingPutDataJob)) {
            buildUploadStatusMessage(job, BUILDING);
        } else if (job.equals(syncJobBuilder.surveyPostDataJob)) {
            buildUploadStatusMessage(job, SURVEY);
        } else if (job.equals(syncJobBuilder.surveyPutDataJob)) {
            buildUploadStatusMessage(job, SURVEY);
        }
    }

    @Override
    protected void onJobStart(Job startingJob) {
    }

    @Override
    protected void onRunFinish() {
        if (errorJobs() == finishedJobs())
            showErrorMessage();
        else
            Alert.mediumLevel().show(getSyncStatusMessage());
    }

    public String getSyncStatusMessage() {
        return syncStatusMsg.trim();
    }

    private void showErrorMessage() {
        if (ioException != null)
            Alert.mediumLevel().show(R.string.error_server_problem);
        else if (restServiceException != null)
            Alert.mediumLevel().show(R.string.error_rest_service);
    }

    private void buildUploadStatusMessage(Job job, String dataType) {
        UploadJob uploadJob = (UploadJob) job;
        if ((uploadJob).getSuccessCount() == 0 && (uploadJob).getFailCount() == 0)
            return;

        if (job instanceof PostDataJob)
            syncStatusMsg += String.format(context.getString(R.string.upload_data_type), dataType)
                    + appendUploadSuccessMessage(uploadJob) + appendUploadFailedMessage(uploadJob);
        else if (job instanceof PutDataJob)
            syncStatusMsg += String.format(context.getString(R.string.update_data_type), dataType)
                    + appendUploadSuccessMessage(uploadJob) + appendUploadFailedMessage(uploadJob);
    }

    private String appendUploadSuccessMessage(UploadJob uploadJob) {
        if (uploadJob.getSuccessCount() > 0)
            return String.format(context.getString(R.string.upload_data_success),
                    uploadJob.getSuccessCount());
        else
            return "";

    }

    private String appendUploadFailedMessage(UploadJob uploadJob) {
        if (uploadJob.getFailCount() > 0)
            return String.format(context.getString(R.string.upload_data_fail),
                    uploadJob.getFailCount()) + "\n";
        else
            return "\n";

    }
}
