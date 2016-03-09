package th.or.nectec.tanrabad.survey.validator;

import android.support.annotation.StringRes;

public class NullAddressException extends ValidatorException {
    public NullAddressException(@StringRes int messageId) {
        super(messageId);
    }
}
