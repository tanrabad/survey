package org.tanrabad.survey.utils.showcase;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.support.v7.widget.Toolbar;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.SimpleShowcaseEventListener;

public class ToolbarBasedShowcase extends BaseShowcase {

    private OnShowcaseDismissListener onShowcaseDismissListener;

    public ToolbarBasedShowcase(Activity activity, Toolbar toolbar, @IdRes int viewId) {
        super(activity);
        getBuilder().setTarget(new ToolbarActionItemTarget(toolbar, viewId))
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
