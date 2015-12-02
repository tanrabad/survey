package th.or.nectec.tanrabad.survey.utils.alert;

import android.content.Context;


public class Alert {

    private static Context context;

    public static void init(Context context) {
        Alert.context = context;
    }

    public static AlertMessage lowLevel() {
        return new ToastAlertMessage(context);
    }

    public static AlertMessage mediumLevel() {
        return new ToastAlertMessage(context);
    }

    public static AlertMessage highLevel() {
        return new DialogAlertMessage(context);
    }

}
