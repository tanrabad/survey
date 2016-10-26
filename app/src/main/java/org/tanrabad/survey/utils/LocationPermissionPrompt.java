package org.tanrabad.survey.utils;

import android.Manifest;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;

import org.tanrabad.survey.utils.prompt.AlertDialogPromptMessage;
import org.tanrabad.survey.utils.prompt.PromptMessage;
import org.tanrabad.survey.R;
import org.tanrabad.survey.utils.prompt.PromptMessage.OnCancelListener;
import org.tanrabad.survey.utils.prompt.PromptMessage.OnConfirmListener;

public class LocationPermissionPrompt {

    private static final int REQUEST_CODE_ASK_PERMISSIONS = 777;
    public static final String PERMISSION_TO_REQUEST = Manifest.permission.ACCESS_FINE_LOCATION;

    public static void show(final Activity activity) {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, PERMISSION_TO_REQUEST)) {
            PromptMessage promptMessage = new AlertDialogPromptMessage(activity);
            promptMessage.setOnConfirm(getString(activity, R.string.ok), new OnConfirmListener() {
                @Override
                public void onConfirm() {
                    ActivityCompat.requestPermissions(activity,
                            new String[]{PERMISSION_TO_REQUEST}, REQUEST_CODE_ASK_PERMISSIONS);
                }
            });
            promptMessage.setOnCancel(getString(activity, R.string.cancel), new OnCancelListener() {
                @Override
                public void onCancel() {
                    activity.finish();
                }
            });
            promptMessage.show(null, "คุณจำเป็นต้องเปิดใช้สิทธิ์การเข้าถึงตำแหน่งปัจจุบัน");
            return;
        }
        ActivityCompat.requestPermissions(activity,
                new String[]{PERMISSION_TO_REQUEST}, REQUEST_CODE_ASK_PERMISSIONS);

    }

    @NonNull
    private static String getString(Activity activity, @StringRes int stringId) {
        return activity.getResources().getString(stringId);
    }
}
