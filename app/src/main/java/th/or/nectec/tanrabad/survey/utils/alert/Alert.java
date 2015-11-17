package th.or.nectec.tanrabad.survey.utils.alert;

import android.content.Context;

/**
 * Created by N. Choatravee on 17/11/2558.
 */
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
        return new ToastAlertMessage(context);
    }

}
