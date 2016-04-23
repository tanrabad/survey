package org.tanrabad.survey.utils.prompt;

public interface PromptMessage {
    void show(String title, String message);

    void setOnConfirm(String confirmMessage, OnConfirmListener onConfirmListener);

    void setOnCancel(String cancelMessage, OnCancelListener onCancelListener);

    interface OnConfirmListener {
        void onConfirm();
    }

    interface OnCancelListener {
        void onCancel();
    }
}
