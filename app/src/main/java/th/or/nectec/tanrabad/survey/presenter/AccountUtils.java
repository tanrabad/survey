package th.or.nectec.tanrabad.survey.presenter;


import th.or.nectec.tanrabad.entity.User;

class AccountUtils {
    private static User user;

    protected static User getUser() {
        if (AccountUtils.user == null)
            throw new NullPointerException("user is null, please set user before call this");
        return AccountUtils.user;
    }

    protected static void setUser(User user) {
        AccountUtils.user = user;
    }
}
