package th.or.nectec.tanrabad.survey.utils.alert;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;

import th.or.nectec.tanrabad.survey.R;

class DialogAlertMessage implements AlertMessage {
    private Context context;

    public DialogAlertMessage(Context context) {
        this.context = context;
    }

    @Override
    public void show(String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setView(R.layout.dialog_message_layout);
        alertDialog.setPositiveButton(R.string.got_it, null);
        AlertDialog dialog = alertDialog.show();
        TextView messageView = (TextView) dialog.findViewById(R.id.dialog_message);
        messageView.setText(message);
    }

    @Override
    public void show(@StringRes int messageID) {
        show(context.getResources().getString(messageID));
    }
}
