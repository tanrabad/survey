/*
 * Copyright (c) 2016 NECTEC
 *   National Electronics and Computer Technology Center, Thailand
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
    private long time1;

    private String message;

    private Context mContext;
    public static final int DELAY = 500;

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
        long time2;

        if (time1 == 0) {
            time1 = System.currentTimeMillis();
            int toastTimeOut = (mTimeout == TIMEOUT_SHORT) ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG;
            Toast.makeText(mContext, message, toastTimeOut).show();
            return false;
        } else {
            time2 = System.currentTimeMillis();
        }

        long duration = time2 - time1;

        if (duration < DELAY) {
            return false;
        } else if (duration < mTimeout) {
            return true;
        } else {
            time1 = 0;
            onTwiceBackPressed();
        }

        return false;
    }
}
