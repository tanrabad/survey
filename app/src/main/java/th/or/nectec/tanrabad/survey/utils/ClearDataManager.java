package th.or.nectec.tanrabad.survey.utils;

import android.content.Context;

import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.repository.persistence.SqlScript;
import th.or.nectec.tanrabad.survey.repository.persistence.SurveyLiteDatabase;
import th.or.nectec.tanrabad.survey.service.ServiceLastUpdatePreference;

public class ClearDataManager {
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
        ServiceLastUpdatePreference.clear(context);
    }
}

