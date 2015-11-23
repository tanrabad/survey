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

package th.or.nectec.tanrabad.survey.view;

import android.content.Context;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormat;
import org.joda.time.format.PeriodFormatter;

/**
 * Modified form RelativeTimeTextView of Kiran Rao in Android-Ago Project
 */
public class RelativeTimeAgoTextView extends TextView implements TimeAgoView {
    private long referenceTime;
    private Handler handler = new Handler();
    private UpdateTimeRunnable updateTimeTask;
    private boolean isUpdateTaskRunning = false;
    private PeriodFormatter periodFormatter = PeriodFormat.getDefault();

    public RelativeTimeAgoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RelativeTimeAgoTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
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
        Period period = new Period(referenceTime, System.currentTimeMillis());
        return periodFormatter.print(period);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startTaskForPeriodicallyUpdatingRelativeTime();

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopTaskForPeriodicallyUpdatingRelativeTime();
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
    public void setPeriodFormatter(PeriodFormatter periodFormatter) {
        this.periodFormatter = periodFormatter;
    }

    @Override
    public void setTime(DateTime dateTime) {
        setReferenceTime(dateTime.getMillis());
    }

    public static class SavedState extends BaseSavedState {

        private long referenceTime;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeLong(referenceTime);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };

        private SavedState(Parcel in) {
            super(in);
            referenceTime = in.readLong();
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
            if (difference > DateUtils.MINUTE_IN_MILLIS) {
                interval = DateUtils.MINUTE_IN_MILLIS;
            } else if (difference > DateUtils.WEEK_IN_MILLIS) {
                interval = DateUtils.WEEK_IN_MILLIS;
            } else if (difference > DateUtils.DAY_IN_MILLIS) {
                interval = DateUtils.DAY_IN_MILLIS;
            } else if (difference > DateUtils.HOUR_IN_MILLIS) {
                interval = DateUtils.HOUR_IN_MILLIS;
            }
            updateTextDisplay();
            handler.postDelayed(this, interval);
        }

    }
}
