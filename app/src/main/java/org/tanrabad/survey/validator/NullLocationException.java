package org.tanrabad.survey.validator;

import android.support.annotation.StringRes;

class NullLocationException extends ValidatorException {
    public NullLocationException(@StringRes int messageId) {
        super(messageId);
    }
}
