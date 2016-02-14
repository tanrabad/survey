package th.or.nectec.tanrabad.survey.utils.showcase;


import android.app.Activity;
import android.support.annotation.IdRes;
import android.support.v7.widget.Toolbar;

public class ShowcaseFactory {
    private static Activity activity;

    public static void init(Activity activity) {
        ShowcaseFactory.activity = activity;
    }

    public static ToolbarBasedShowcase toolbarShowCase(Toolbar toolbar, @IdRes int viewId) {
        return new ToolbarBasedShowcase(activity, toolbar, viewId);
    }

    public static ViewBasedShowcase viewShowcase(@IdRes int viewId) {
        return new ViewBasedShowcase(activity, viewId);
    }
}
