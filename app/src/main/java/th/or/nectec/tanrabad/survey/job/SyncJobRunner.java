package th.or.nectec.tanrabad.survey.job;

import android.content.Context;
import android.text.TextUtils;

import java.io.IOException;
import java.util.ArrayList;

import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.TanrabadApp;
import th.or.nectec.tanrabad.survey.service.RestServiceException;
import th.or.nectec.tanrabad.survey.utils.alert.Alert;
import th.or.nectec.tanrabad.survey.utils.android.InternetConnection;

public class SyncJobRunner extends AbsJobRunner {

    private static final String PLACE = "สถานที่";
    private static final String SURVEY = "การสำรวจ";
    private static final String BUILDING = "อาคาร";
    private final SyncJobBuilder syncJobBuilder;
    private final Context context;

    private int completelySuccessCount = 0;
    private int completelyFailCount = 0;
    private int dataUploadedCount = 0;

    private ArrayList<UploadJob> uploadJobs = new ArrayList<>();

    private IOException ioException;
    private RestServiceException restServiceException;
    private boolean isManualSync;

    public SyncJobRunner() {
        this(false);
    }

    public SyncJobRunner(boolean isManualSync) {
        this.isManualSync = isManualSync;
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
        if (job instanceof UploadJob) {
            UploadJob uploadJob = (UploadJob) job;
            uploadJobs.add(uploadJob);
            if (uploadJob.isUploadCompletelySuccess())
                completelySuccessCount++;
            if (uploadJob.isUploadCompletelyFail())
                completelyFailCount++;
            if (uploadJob.isUploadData())
                dataUploadedCount++;
        }
    }

    @Override
    protected void onJobStart(Job startingJob) {
    }

    @Override
    protected void onRunFinish() {
        if (uploadJobs.size() == 0)
            return;

        if (completelySuccessCount == dataUploadedCount) {
            showUploadResultMsg();
        } else if (completelyFailCount == dataUploadedCount) {
            showErrorMessage();
        } else {
            showUploadResultMsg();
        }
    }

    private void showErrorMessage() {
        if (!isManualSync)
            return;

        if (ioException != null)
            Alert.mediumLevel().show(R.string.error_connection_problem);
        else if (restServiceException != null) {
            Alert.mediumLevel().show(R.string.error_rest_service);
        }
    }

    private void showUploadResultMsg() {
        String message = "";
        for (UploadJob uploadJob : uploadJobs) {
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

    private String getDataType(UploadJob uploadJob) {
        String dataType = null;
        if (uploadJob.equals(syncJobBuilder.placePostDataJob)
                || uploadJob.equals(syncJobBuilder.placePutDataJob)) {
            dataType = PLACE;
        } else if (uploadJob.equals(syncJobBuilder.buildingPostDataJob)
                || uploadJob.equals(syncJobBuilder.buildingPutDataJob)) {
            dataType = BUILDING;
        } else if (uploadJob.equals(syncJobBuilder.surveyPostDataJob)
                || uploadJob.equals(syncJobBuilder.surveyPutDataJob)) {
            dataType = SURVEY;
        }
        return dataType;
    }

    private String appendUploadSuccessMessage(UploadJob uploadJob) {
        return uploadJob.getSuccessCount() > 0
                ? String.format(context.getString(R.string.upload_data_success), uploadJob.getSuccessCount())
                : "";
    }

    private String appendUploadFailedMessage(UploadJob uploadJob) {
        String space = (uploadJob.getSuccessCount() > 0) ? " " : "";
        String message = (uploadJob.getFailCount() > 0)
                ? String.format(context.getString(R.string.upload_data_fail), uploadJob.getFailCount()) + "\n"
                : "\n";
        return space + message;
    }
}
