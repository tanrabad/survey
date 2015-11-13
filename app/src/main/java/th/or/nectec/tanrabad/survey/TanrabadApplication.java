package th.or.nectec.tanrabad.survey;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class TanrabadApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/ThaiSansNeue-Regular.otf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
    }
}
