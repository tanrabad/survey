package th.or.nectec.tanrabad.survey.utils.showcase;


import android.app.Activity;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.github.amlcurran.showcaseview.ShowcaseView;
import th.or.nectec.tanrabad.survey.R;

public abstract class BaseShowcase implements Showcase {

    private ShowcaseView.Builder builder;
    private Activity activity;

    public BaseShowcase(Activity activity) {
        this.activity = activity;
        builder = new ShowcaseView.Builder(activity)
                .setStyle(R.style.CustomShowcaseTheme)
                .withNewStyleShowcase()
                .setContentTextPaint(ShowcaseFontStyle.getContentStyle(activity))
                .setContentTitlePaint(ShowcaseFontStyle.getTitleStyle(activity));
    }

    public ShowcaseView.Builder getBuilder() {
        return builder;
    }

    @Override
    public void setTitle(String title) {
        builder.setContentTitle(title);
    }

    @Override
    public void setMessage(String message) {
        builder.setContentText(message);
    }

    @Override
    public void display() {
        if (new ShowcasePreference(activity).get()) {
            RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            lps.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            int margin = ((Number) (activity.getResources().getDisplayMetrics().density * 12)).intValue();
            lps.setMargins(margin, margin, margin, margin);

            ShowcaseView build = builder.build();
            build.setButtonPosition(lps);
        }
    }
}