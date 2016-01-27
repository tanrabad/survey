package th.or.nectec.tanrabad.survey.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;
import th.or.nectec.tanrabad.survey.R;

public class SnackToast {
    @SuppressLint("InflateParams")
    public static Toast make(Context context, String message, int duration) {
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.BOTTOM, 0, 0);
        toast.setDuration(duration);
        TextView toastMsg = (TextView) ((Activity) context).getLayoutInflater().inflate(R.layout.view_custom_toast, null);
        toastMsg.setText(message);
        toast.setView(toastMsg);
        return toast;
    }
}
