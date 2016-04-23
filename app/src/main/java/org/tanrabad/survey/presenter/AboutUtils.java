package org.tanrabad.survey.presenter;


import android.app.Activity;
import android.content.Intent;

public class AboutUtils {

    public static void showLicense(Activity activity) {
        Intent intent = new Intent(activity, LicenseActivity.class);
        activity.startActivity(intent);
    }
}
