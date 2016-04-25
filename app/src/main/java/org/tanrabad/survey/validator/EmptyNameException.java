package org.tanrabad.survey.validator;

import android.support.annotation.StringRes;

class EmptyNameException extends ValidatorException {
    public EmptyNameException(@StringRes int messageId) {
        super(messageId);
    }
}
