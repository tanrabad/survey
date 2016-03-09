package th.or.nectec.tanrabad.survey.validator;

import android.support.annotation.StringRes;

public class EmptyNameException extends ValidatorException {
    public EmptyNameException(@StringRes int messageId) {
        super(messageId);
    }
}
