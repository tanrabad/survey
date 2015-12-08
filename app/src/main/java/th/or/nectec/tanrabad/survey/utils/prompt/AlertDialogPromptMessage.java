package th.or.nectec.tanrabad.survey.utils.prompt;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.TextView;

import th.or.nectec.tanrabad.survey.R;

public class AlertDialogPromptMessage implements PromptMessage {

    AlertDialog.Builder alertDialog;

    public AlertDialogPromptMessage(Context context) {
        alertDialog = new AlertDialog.Builder(context);
    }

    @Override
    public void show(String title, String message) {
        alertDialog.setTitle(title);
        if (TextUtils.isEmpty(message)) {
            alertDialog.show();
        } else {
            alertDialog.setView(R.layout.dialog_message_layout);
            AlertDialog dialog = alertDialog.show();
            TextView messageView = (TextView) dialog.findViewById(R.id.dialog_message);
            messageView.setText(message);
        }
    }

    @Override
    public void setOnConfirm(String confirmMessage, final OnConfirmListener onConfirmListener) {
        alertDialog.setPositiveButton(confirmMessage, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onConfirmListener.onConfirm();
            }
        });
    }

    @Override
    public void setOnCancel(String cancelMessage, final OnCancelListener onCancelListener) {
        alertDialog.setNegativeButton(cancelMessage, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (onCancelListener != null)
                    onCancelListener.onCancel();
            }
        });
    }
}
