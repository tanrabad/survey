package org.tanrabad.survey.repository;

import android.content.Context;

import org.tanrabad.survey.R;
import org.tanrabad.survey.TanrabadApp;
import org.tanrabad.survey.job.UploadJobRunner;
import org.tanrabad.survey.repository.persistence.SqlScript;
import org.tanrabad.survey.repository.persistence.SurveyLiteDatabase;
import org.tanrabad.survey.service.ApiSyncInfoPreference;

public class AppDataManager implements DataManager {

    @Override
    public void syncAndClearData() {
        try {
            UploadJobRunner.Builder builder = new UploadJobRunner.Builder();
            builder.placePostDataJob.execute();
            builder.buildingPostDataJob.execute();
            builder.surveyPostDataJob.execute();
            builder.placePutDataJob.execute();
            builder.buildingPutDataJob.execute();
            builder.surveyPutDataJob.execute();
            AppDataManager.clearAll(TanrabadApp.getInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clearAll(Context context) {
        clearPreferences(context);
        clearDatabase(context);
    }

    public static void clearDatabase(Context context) {
        SqlScript.readAndExecute(
                context,
            new SurveyLiteDatabase(context).getWritableDatabase(),
                R.raw.delete);
    }

    public static void clearPreferences(Context context) {
        ApiSyncInfoPreference.clear(context);
    }
}

