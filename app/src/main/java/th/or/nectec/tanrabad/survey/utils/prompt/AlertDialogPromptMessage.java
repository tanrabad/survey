package th.or.nectec.tanrabad.survey.utils.prompt;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

public class AlertDialogPromptMessage implements PromptMessage {

    AlertDialog.Builder alertDialog;

    public AlertDialogPromptMessage(Context context) {
        alertDialog = new AlertDialog.Builder(context);
    }

    @Override
    public void show(String title, String message) {
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.show();
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
