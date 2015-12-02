package th.or.nectec.tanrabad.survey.validator;

import android.support.annotation.StringRes;

public class ValidatorException extends RuntimeException {

    private int messageID;

    public ValidatorException(@StringRes int messageID) {
        this.messageID = messageID;
    }

    @StringRes
    public int getMessageID() {
        return messageID;
    }
}
