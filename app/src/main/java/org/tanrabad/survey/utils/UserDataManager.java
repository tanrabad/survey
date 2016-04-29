package org.tanrabad.survey.utils;

import android.content.Context;

import org.tanrabad.survey.R;
import org.tanrabad.survey.repository.persistence.SqlScript;
import org.tanrabad.survey.repository.persistence.SurveyLiteDatabase;
import org.tanrabad.survey.service.ApiSyncInfoPreference;

public class UserDataManager {
    public static void clearAll(Context context) {
        clearPreferences(context);
        clearDatabase(context);
    }

    public static void clearDatabase(Context context) {
        SqlScript.readAndExecute(
                context,
                SurveyLiteDatabase.getInstance(context).getWritableDatabase(),
                R.raw.delete);
    }

    public static void clearPreferences(Context context) {
        ApiSyncInfoPreference.clear(context);
    }
}

