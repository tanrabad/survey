/*
 * Copyright (c) 2015 NECTEC
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

package th.or.nectec.tanrabad.survey.presenter.view;

import android.content.Context;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import org.joda.time.DateTime;
import th.or.nectec.tanrabad.survey.utils.time.CurrentTimer;
import th.or.nectec.tanrabad.survey.utils.time.JodaCurrentTime;
import th.or.nectec.tanrabad.survey.utils.time.TimePrettyPrinterFactory;

/**
 * Modified form RelativeTimeTextView of Kiran Rao in Android-Ago Project
 */
public class RelativeTimeAgoTextView extends TextView implements TimeAgoView {
    private long referenceTime;
    private Handler handler = new Handler();
    private UpdateTimeRunnable updateTimeTask;
    private boolean isUpdateTaskRunning = false;
    private CurrentTimer currentTimer = new JodaCurrentTime();

    public RelativeTimeAgoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RelativeTimeAgoTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopTaskForPeriodicallyUpdatingRelativeTime();
    }

    private void stopTaskForPeriodicallyUpdatingRelativeTime() {
        if (isUpdateTaskRunning) {
            handler.removeCallbacks(updateTimeTask);
            isUpdateTaskRunning = false;
        }
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.referenceTime = referenceTime;
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState ss = (SavedState) state;
        referenceTime = ss.referenceTime;
        super.onRestoreInstanceState(ss.getSuperState());
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startTaskForPeriodicallyUpdatingRelativeTime();

    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == GONE || visibility == INVISIBLE) {
            stopTaskForPeriodicallyUpdatingRelativeTime();
        } else {
            startTaskForPeriodicallyUpdatingRelativeTime();
        }
    }

    private void startTaskForPeriodicallyUpdatingRelativeTime() {
        handler.post(updateTimeTask);
        isUpdateTaskRunning = true;
    }

    @Override
    public void setTime(DateTime dateTime) {
        setReferenceTime(dateTime.getMillis());
    }

    public void setReferenceTime(long referenceTime) {
        this.referenceTime = referenceTime;
        stopTaskForPeriodicallyUpdatingRelativeTime();
        updateTimeTask = new UpdateTimeRunnable(this.referenceTime);
        startTaskForPeriodicallyUpdatingRelativeTime();
        updateTextDisplay();
    }

    private void updateTextDisplay() {
        setText(getRelativeTimeDisplayString());
    }

    private CharSequence getRelativeTimeDisplayString() {
        TimePrettyPrinterFactory timePrettyPrinterFactory = new TimePrettyPrinterFactory(currentTimer);
        return timePrettyPrinterFactory.print(this.referenceTime);
    }

    public static class SavedState extends BaseSavedState {

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        private long referenceTime;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            referenceTime = in.readLong();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeLong(referenceTime);
        }
    }

    private class UpdateTimeRunnable implements Runnable {

        private long referenceTime;

        UpdateTimeRunnable(long referenceTime) {
            this.referenceTime = referenceTime;
        }

        @Override
        public void run() {
            long difference = System.currentTimeMillis() - referenceTime;
            long interval = DateUtils.SECOND_IN_MILLIS;
            if (difference > DateUtils.DAY_IN_MILLIS) {
                interval = DateUtils.DAY_IN_MILLIS;
            } else if (difference > DateUtils.HOUR_IN_MILLIS) {
                interval = DateUtils.HOUR_IN_MILLIS;
            } else if (difference > DateUtils.MINUTE_IN_MILLIS) {
                interval = DateUtils.MINUTE_IN_MILLIS;
            }
            updateTextDisplay();
            handler.postDelayed(this, interval);
        }

    }
}
