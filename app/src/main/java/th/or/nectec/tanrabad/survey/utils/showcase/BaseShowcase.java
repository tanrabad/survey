package th.or.nectec.tanrabad.survey.utils.showcase;


import android.app.Activity;
import com.github.amlcurran.showcaseview.ShowcaseView;
import th.or.nectec.tanrabad.survey.R;

public class BaseShowcase {
    public static ShowcaseView.Builder build(Activity activity) {
        ShowcaseView.Builder builder = new ShowcaseView.Builder(activity)
                .setStyle(R.style.CustomShowcaseTheme)
                .setContentTextPaint(ShowcaseFontStyle.getContentStyle(activity))
                .setContentTitlePaint(ShowcaseFontStyle.getTitleStyle(activity));
        return builder;
    }
}
