package th.or.nectec.tanrabad.survey.validator;

import android.support.annotation.StringRes;

public class NullLocationException extends ValidatorException {
    public NullLocationException(@StringRes int messageID) {
        super(messageID);
    }
}
