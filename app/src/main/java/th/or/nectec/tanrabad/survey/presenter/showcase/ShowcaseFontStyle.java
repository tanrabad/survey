package th.or.nectec.tanrabad.survey.presenter.showcase;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextPaint;

public class ShowcaseFontStyle {

    public static TextPaint getTitleStyle(Context context) {
        TextPaint textPaint = new TextPaint();
        textPaint.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/ThaiSansNeue-Bold.otf"));
        textPaint.setAntiAlias(true);
        return textPaint;
    }

    public static TextPaint getContentStyle(Context context) {
        TextPaint textPaint = new TextPaint();
        textPaint.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/ThaiSansNeue-Regular.otf"));
        textPaint.setAntiAlias(true);
        return textPaint;
    }
}
