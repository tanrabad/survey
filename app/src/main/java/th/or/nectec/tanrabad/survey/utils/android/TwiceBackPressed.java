package th.or.nectec.tanrabad.survey.utils.android;

import android.content.Context;
import android.widget.Toast;

import th.or.nectec.tanrabad.survey.R;

public class TwiceBackPressed {

    public static final int DURATION_SHORT = 0;
    public static final int DURATION_LONG = 1;

    private static final int TIMEOUT_SHORT = 2500;
    private static final int TIMEOUT_LONG = 4000;
    int mTimeout = TIMEOUT_SHORT;
    int mDelay = 500;
    long time1 = 0;
    long time2 = 0;

    String mMessage;

    Context mContext;

    public TwiceBackPressed(Context context) {
        mContext = context;
        mMessage = mContext.getString(R.string.press_back_again_to_exit);
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
        mMessage = message;
    }

    /**
     * @param stringResource set message to notify before press button again by using resource id.
     */
    public void setToastMessage(int stringResource) {
        mMessage = mContext.getResources().getString(stringResource);
    }

    /**
     * @return return status that can continue your action after do twice pressed.
     */
    public boolean onTwiceBackPressed() {

        if (time1 == 0) {
            time1 = System.currentTimeMillis();
            int toastTimeOut = (mTimeout == TIMEOUT_SHORT) ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG;
            Toast.makeText(mContext, mMessage, toastTimeOut).show();
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
