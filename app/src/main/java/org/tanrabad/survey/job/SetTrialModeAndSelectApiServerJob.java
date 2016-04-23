package org.tanrabad.survey.job;

import android.content.Context;
import org.tanrabad.survey.service.AbsRestService;
import org.tanrabad.survey.service.TrialModePreference;
import org.tanrabad.survey.BuildConfig;

public class SetTrialModeAndSelectApiServerJob implements Job {

    private static final String TEST_URL = "http://trb-test.igridproject.info/v1";
    TrialModePreference trialModePreference;
    private boolean isTrialMode;

    public SetTrialModeAndSelectApiServerJob(Context context, boolean isTrialMode) {
        this.trialModePreference = new TrialModePreference(context);
        this.isTrialMode = isTrialMode;
    }

    @Override
    public int id() {
        return 88888;
    }

    @Override
    public void execute() throws Exception {
        trialModePreference.setUsingTrialMode(isTrialMode);
        if (isTrialMode) {
            AbsRestService.setBaseApi(TEST_URL);
        } else {
            AbsRestService.setBaseApi(BuildConfig.API_URL);
        }
    }
}
