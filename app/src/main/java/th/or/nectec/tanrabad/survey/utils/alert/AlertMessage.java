package th.or.nectec.tanrabad.survey.utils.alert;

import android.support.annotation.StringRes;

public interface AlertMessage {
    void show(String message);

    void show(@StringRes int messageID);
}
