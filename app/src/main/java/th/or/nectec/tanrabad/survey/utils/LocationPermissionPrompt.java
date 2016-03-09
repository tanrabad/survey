package th.or.nectec.tanrabad.survey.utils;

import android.Manifest;
import android.app.Activity;
import android.support.v4.app.ActivityCompat;

import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.utils.prompt.AlertDialogPromptMessage;
import th.or.nectec.tanrabad.survey.utils.prompt.PromptMessage;

public class LocationPermissionPrompt {

    private static final int REQUEST_CODE_ASK_PERMISSIONS = 777;

    public static void show(final Activity activity) {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(activity,
                Manifest.permission_group.LOCATION)) {

            PromptMessage promptMessage = new AlertDialogPromptMessage(activity);
            promptMessage.setOnConfirm(activity.getResources().getString(R.string.ok),
                    new PromptMessage.OnConfirmListener() {
                        @Override
                        public void onConfirm() {
                            ActivityCompat.requestPermissions(activity,
                                    new String[]{Manifest.permission_group.LOCATION},
                                    REQUEST_CODE_ASK_PERMISSIONS);
                        }
                    });
            promptMessage.setOnCancel(activity.getResources().getString(R.string.cancel),
                    new PromptMessage.OnCancelListener() {
                        @Override
                        public void onCancel() {
                            activity.finish();
                        }
                    });
            promptMessage.show(null, "คุณจำเป็นต้องเปิดใช้สิทธิ์การเข้าถึงตำแหน่งปัจจุบัน");
            return;
        }
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission_group.LOCATION},
                REQUEST_CODE_ASK_PERMISSIONS);

    }
}
