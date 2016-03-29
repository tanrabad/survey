package th.or.nectec.tanrabad.survey.job;

import android.content.Context;

import th.or.nectec.tanrabad.survey.BuildConfig;
import th.or.nectec.tanrabad.survey.presenter.LoginActivity;
import th.or.nectec.tanrabad.survey.service.AbsRestService;
import th.or.nectec.tanrabad.survey.service.TrialModePreference;

public class SetTrialModeAndSelectApiServerJob implements Job {

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
            AbsRestService.setBaseApi(LoginActivity.TEST_URL);
        } else {
            AbsRestService.setBaseApi(BuildConfig.API_URL);
        }
    }
}
