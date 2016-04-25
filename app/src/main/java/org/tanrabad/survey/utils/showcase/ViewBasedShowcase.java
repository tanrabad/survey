package org.tanrabad.survey.utils.showcase;

import android.app.Activity;
import android.support.annotation.IdRes;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.SimpleShowcaseEventListener;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

public class ViewBasedShowcase extends BaseShowcase {
    private Showcase.OnShowcaseDismissListener onShowcaseDismissListener;

    public ViewBasedShowcase(Activity activity, @IdRes int viewId) {
        super(activity);
        getBuilder().setTarget(new ViewTarget(viewId, activity))
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
    public void setOnShowCaseDismissListener(OnShowcaseDismissListener onShowcaseDismissListener) {
        this.onShowcaseDismissListener = onShowcaseDismissListener;
    }
}
