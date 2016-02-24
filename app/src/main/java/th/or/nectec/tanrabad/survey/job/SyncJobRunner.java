package th.or.nectec.tanrabad.survey.job;

import android.content.Context;
import android.util.Log;

import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.TanrabadApp;
import th.or.nectec.tanrabad.survey.utils.alert.Alert;

public class SyncJobRunner extends AbsJobRunner {

    private static final String PLACE = "สถานที่";
    private static final String SURVEY = "สำรวจ";
    private static String BUILDING = "อาคาร";
    private final SyncJobBuilder syncJobBuilder;
    private final Context context;
    String message = "";

    public SyncJobRunner() {
        syncJobBuilder = new SyncJobBuilder();
        syncJobBuilder.build(this);
        context = TanrabadApp.getInstance();
    }

    @Override
    protected void onJobError(Job errorJob, Exception exception) {
        super.onJobError(errorJob, exception);
        Log.e(errorJob.toString(), exception.getMessage());
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
        if (errorJobs() == finishedJobs()) {
            Alert.mediumLevel().show(R.string.upload_data_failure);
        } else {
            Alert.mediumLevel().show(message.trim());
        }
    }

    private void buildUploadStatusMessage(Job job, String dataType) {
        UploadJob uploadJob = (UploadJob) job;
        if ((uploadJob).getSuccessCount() == 0 && (uploadJob).getFailCount() == 0)
            return;

        if (job instanceof PostDataJob) {
            PostDataJob postDataJob = (PostDataJob) job;
            message += String.format(context.getString(R.string.upload_data_status),
                    dataType, postDataJob.getSuccessCount(), postDataJob.getFailCount()) + "\n";
        } else if (job instanceof PutDataJob) {
            PutDataJob putDataJob = (PutDataJob) job;
            message += String.format(context.getString(R.string.update_data_status),
                    dataType, putDataJob.getSuccessCount(), putDataJob.getFailCount()) + "\n";
        }
    }
}
