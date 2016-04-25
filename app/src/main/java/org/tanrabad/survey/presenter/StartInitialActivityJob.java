package org.tanrabad.survey.presenter;

import android.app.Activity;
import org.tanrabad.survey.job.Job;

class StartInitialActivityJob implements Job {

    private final Activity activity;

    public StartInitialActivityJob(Activity activity) {
        this.activity = activity;
    }

    @Override
    public int id() {
        return 99999;
    }

    @Override
    public void execute() throws Exception {
        InitialActivity.open(activity);
        activity.finish();
    }
}
