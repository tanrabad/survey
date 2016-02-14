package th.or.nectec.tanrabad.survey.utils.showcase;

import android.content.Context;
import android.content.SharedPreferences;

public class ShowcasePreference {
    public static final String PREF_NAME = "showcase";
    public static final String NEED_SHOWCASE_OPTION = "need-showcase";
    private Context context;

    public ShowcasePreference(Context context) {
        this.context = context;
    }

    public void save(boolean needShowcase) {
        SharedPreferences.Editor spEditor = getSharedPreferences().edit();
        spEditor.putBoolean(NEED_SHOWCASE_OPTION, needShowcase);
        spEditor.apply();
    }

    private SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public boolean get() {
        return getSharedPreferences().getBoolean(NEED_SHOWCASE_OPTION, true);

    }
}
