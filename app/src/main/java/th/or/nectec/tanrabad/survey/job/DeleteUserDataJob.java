package th.or.nectec.tanrabad.survey.job;

import android.content.Context;

import th.or.nectec.tanrabad.survey.utils.UserDataManager;

public class DeleteUserDataJob implements Job {
    private Context context;

    public DeleteUserDataJob(Context context) {
        this.context = context;
    }

    @Override
    public int id() {
        return 77777;
    }

    @Override
    public void execute() throws Exception {
        UserDataManager.clearAll(context);
    }
}
