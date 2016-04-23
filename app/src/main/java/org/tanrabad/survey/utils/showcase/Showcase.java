package org.tanrabad.survey.utils.showcase;

import com.github.amlcurran.showcaseview.ShowcaseView;

public interface Showcase {
    void setTitle(String title);

    void setMessage(String message);

    void display();

    void setOnShowCaseDismissListener(OnShowcaseDismissListener onShowcaseDismissListener);

    interface OnShowcaseDismissListener {
        void onDismissListener(ShowcaseView showcaseView);
    }
}
