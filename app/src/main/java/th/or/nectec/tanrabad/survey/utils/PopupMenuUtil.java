package th.or.nectec.tanrabad.survey.utils;

import android.view.Menu;

import java.lang.reflect.Method;

/**
 * Created by chncs23 on 19/4/2559.
 */
public class PopupMenuUtil {
    public static void showPopupMenuIcon(Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
