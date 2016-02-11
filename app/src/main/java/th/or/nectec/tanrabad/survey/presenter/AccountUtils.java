package th.or.nectec.tanrabad.survey.presenter;


import th.or.nectec.tanrabad.entity.User;
import th.or.nectec.tanrabad.survey.TanrabadApp;

public class AccountUtils {
    private static User user;

    public static boolean canAddOrEditVillage() {
        return getUser().getHealthRegionCode().equals("dpc-13");
    }

    public static User getUser() {
        if (AccountUtils.user == null)
            throw new NullPointerException("user is null, please set user before call this");
        return AccountUtils.user;
    }

    public static void setUser(User user) {
        AccountUtils.user = user;
        if (TanrabadApp.action() != null)
            TanrabadApp.action().login(AccountUtils.user);
    }
}
