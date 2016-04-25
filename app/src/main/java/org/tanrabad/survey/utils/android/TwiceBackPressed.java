package org.tanrabad.survey.utils.android;

import android.content.Context;
import android.widget.Toast;
import org.tanrabad.survey.R;

public class TwiceBackPressed {

    private static final int DURATION_SHORT = 0;
    private static final int DURATION_LONG = 1;

    private static final int TIMEOUT_SHORT = 2500;
    private static final int TIMEOUT_LONG = 4000;
    private int mTimeout = TIMEOUT_SHORT;
    private int mDelay = 500;
    private long time1 = 0;
    private long time2 = 0;

    private String message;

    private Context mContext;

    public TwiceBackPressed(Context context) {
        mContext = context;
        message = mContext.getString(R.string.press_back_again_to_exit);
    }

    /**
     * @param duration set time duration when do double press. (short or long)
     */
    public void setToastDuration(int duration) {
        switch (duration) {
            case DURATION_LONG:
                mTimeout = TIMEOUT_LONG;
                break;
            case DURATION_SHORT:
            default:
                mTimeout = TIMEOUT_SHORT;
                break;
        }
    }

    /**
     * @param message set message to notify before press button again.
     */
    public void setToastMessage(String message) {
        this.message = message;
    }

    /**
     * @param stringResource set message to notify before press button again by using resource id.
     */
    public void setToastMessage(int stringResource) {
        message = mContext.getResources().getString(stringResource);
    }

    /**
     * @return return status that can continue your action after do twice pressed.
     */
    public boolean onTwiceBackPressed() {

        if (time1 == 0) {
            time1 = System.currentTimeMillis();
            int toastTimeOut = (mTimeout == TIMEOUT_SHORT) ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG;
            Toast.makeText(mContext, message, toastTimeOut).show();
            return false;
        } else {
            time2 = System.currentTimeMillis();
        }

        long duration = time2 - time1;

        if (duration < mDelay) {
            return false;
        } else if (duration < mTimeout) {
            return true;
        } else {
            time1 = 0;
            time2 = 0;
            onTwiceBackPressed();
        }

        return false;
    }
}
