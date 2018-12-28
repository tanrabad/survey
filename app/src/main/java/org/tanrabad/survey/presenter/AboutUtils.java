package org.tanrabad.survey.presenter;


import android.app.Activity;
import android.content.Intent;

import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;

public class AboutUtils {

    public static void showLicense(Activity activity) {
        Intent intent = new Intent(activity, OssLicensesMenuActivity.class);
        activity.startActivity(intent);
    }
}
