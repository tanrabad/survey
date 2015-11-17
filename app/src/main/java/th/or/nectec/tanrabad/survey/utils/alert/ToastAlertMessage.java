package th.or.nectec.tanrabad.survey.utils.alert;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

class ToastAlertMessage implements AlertMessage {
    private Context context;

    public ToastAlertMessage(Context context) {
        this.context = context;
    }

    @Override
    public void show(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void show(@StringRes int messageID) {
        Toast.makeText(context, messageID, Toast.LENGTH_LONG).show();
    }
}
