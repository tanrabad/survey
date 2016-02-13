package th.or.nectec.tanrabad.survey.utils.showcase;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.support.v7.widget.Toolbar;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.SimpleShowcaseEventListener;
import th.or.nectec.tanrabad.survey.R;

public class ToolbarBasedShowcase implements Showcase {

    private final ShowcaseView.Builder showcaseBuilder;
    private OnShowcaseDismissListener onShowcaseDismissListener;

    public ToolbarBasedShowcase(Activity activity, @IdRes int toolbarId, @IdRes int viewId) {
        showcaseBuilder = new ShowcaseView.Builder(activity)
                .setStyle(R.style.CustomShowcaseTheme)
                .setTarget(new ToolbarActionItemTarget((Toolbar) activity.findViewById(toolbarId), viewId))
                .setContentTextPaint(ShowcaseFontStyle.getContentStyle(activity))
                .setContentTitlePaint(ShowcaseFontStyle.getTitleStyle(activity))
                .setShowcaseEventListener(new SimpleShowcaseEventListener() {
                    @Override
                    public void onShowcaseViewHide(ShowcaseView showcaseView) {
                        super.onShowcaseViewHide(showcaseView);
                        if (onShowcaseDismissListener != null)
                            onShowcaseDismissListener.onDismissListener(showcaseView);
                    }
                });
    }

    @Override
    public void setTitle(String title) {
        showcaseBuilder.setContentTitle(title);
    }

    @Override
    public void setMessage(String message) {
        showcaseBuilder.setContentText(message);
    }

    @Override
    public void display() {
        showcaseBuilder.build();
    }

    @Override
    public void setOnShowCaseDismissListener(OnShowcaseDismissListener onShowcaseDismissListener) {

        this.onShowcaseDismissListener = onShowcaseDismissListener;
    }
}
