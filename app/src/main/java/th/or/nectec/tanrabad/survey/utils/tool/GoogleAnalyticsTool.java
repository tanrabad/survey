package th.or.nectec.tanrabad.survey.utils.tool;

import android.content.Context;
import android.util.Log;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders.EventBuilder;
import com.google.android.gms.analytics.Tracker;
import th.or.nectec.tanrabad.entity.User;
import th.or.nectec.tanrabad.survey.BuildConfig;
import th.or.nectec.tanrabad.survey.R;

public class GoogleAnalyticsTool extends FabricTools {

    private static GoogleAnalyticsTool instance;
    private final Tracker tracker;

    private GoogleAnalyticsTool(Context context) {
        super(context);
        tracker = buildTracker(context);
        suppressLintWarning(context);
    }

    private void suppressLintWarning(Context context) {
        //lint warning UnusedResource
        if (BuildConfig.DEBUG) {
            Log.d("GA", context.getString(R.string.ga_trackingId));
            Log.d("GA", context.getString(R.string.gcm_defaultSenderId));
            Log.d("GA", context.getString(R.string.google_app_id));
        }
    }

    private Tracker buildTracker(Context context) {
        GoogleAnalytics analytics = GoogleAnalytics.getInstance(context);
        analytics.setDryRun(BuildConfig.DEBUG);
        Tracker tracker = analytics.newTracker(R.xml.global_tracker);
        tracker.setAppVersion(context.getString(R.string.app_version));
        tracker.setAppName(context.getString(R.string.app_name));
        tracker.enableAutoActivityTracking(true);
        tracker.enableExceptionReporting(true);
        tracker.enableAdvertisingIdCollection(true);
        return tracker;

    }

    public static GoogleAnalyticsTool getInstance(Context context) {
        if (instance == null) {
            instance = new GoogleAnalyticsTool(context);
        }
        return instance;
    }

    @Override
    public void login(User user) {
        super.login(user);
        tracker.set("&uid", String.valueOf(user.hashCode()));
        tracker.send(new EventBuilder()
                .setCategory("UX")
                .setAction("User Login")
                .build());
    }


}
