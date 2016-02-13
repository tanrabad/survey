package th.or.nectec.tanrabad.survey.utils.showcase;

import com.github.amlcurran.showcaseview.ShowcaseView;

/**
 * Created by chncs23 on 13/2/2559.
 */
public interface Showcase {
    void setTitle(String title);

    void setMessage(String message);

    void display();

    void setOnShowCaseDismissListener(OnShowcaseDismissListener onShowcaseDismissListener);

    interface OnShowcaseDismissListener {
        void onDismissListener(ShowcaseView showcaseView);
    }
}
