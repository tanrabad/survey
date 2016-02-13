package th.or.nectec.tanrabad.survey.utils.showcase;

import android.app.Activity;
import android.support.annotation.IdRes;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.SimpleShowcaseEventListener;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

public class ViewBasedShowcase implements Showcase {

    private final ShowcaseView.Builder showcaseBuilder;
    private Showcase.OnShowcaseDismissListener onShowcaseDismissListener;

    public ViewBasedShowcase(Activity activity, @IdRes int viewId) {
        showcaseBuilder = BaseShowcase.build(activity)
                .setTarget(new ViewTarget(viewId, activity))
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
