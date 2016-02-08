package th.or.nectec.tanrabad.survey.utils.alert;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;
import th.or.nectec.tanrabad.survey.utils.SnackToast;

class SnackToastAlertMessage implements AlertMessage {
    private Context context;

    public SnackToastAlertMessage(Context context) {
        this.context = context;
    }

    @Override
    public void show(String message) {
        SnackToast.make(context, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void show(@StringRes int messageID) {
        SnackToast.make(context, context.getString(messageID), Toast.LENGTH_LONG).show();
    }
}
