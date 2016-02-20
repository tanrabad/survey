/*
 * Copyright (c) 2016 NECTEC
 *   National Electronics and Computer Technology Center, Thailand
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package th.or.nectec.tanrabad.survey;

import android.app.Application;
import android.content.Context;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import th.or.nectec.tanrabad.survey.utils.tool.ActionLogger;
import th.or.nectec.tanrabad.survey.utils.tool.ExceptionLogger;
import th.or.nectec.tanrabad.survey.utils.tool.FabricTools;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class TanrabadApp extends Application {

    private static ExceptionLogger exceptionLogger;
    private static ActionLogger actionLogger;
    private static TanrabadApp instance;
    private static Tracker tracker;

    public static Tracker tracker() {
        return tracker;
    }

    public static ActionLogger action() {
        return actionLogger;
    }

    public static Context getInstance() {
        return instance;
    }

    public static void log(Exception e) {
        if (exceptionLogger != null) exceptionLogger.log(e);
    }

    public static void log(String message) {
        if (exceptionLogger != null) exceptionLogger.log(message);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        setupAnalysisTools();
        setupTracker();
        setupDefaultFont();
    }

    private void setupAnalysisTools() {
        FabricTools fabricTools = FabricTools.getInstance(this);
        exceptionLogger = fabricTools;
        actionLogger = fabricTools;
    }

    private void setupTracker() {
        GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
        tracker = analytics.newTracker(BuildConfig.BUILD_TYPE.equals("beta")
                ? R.xml.global_tracker
                : R.xml.analytics_global_config);
        tracker.setAppId(getString(R.string.google_app_id));
        tracker.setAppVersion(getString(R.string.app_version));
        tracker.setAppName(getString(R.string.app_name));
        tracker.setSessionTimeout(15 * 60 * 1000);
        tracker.enableAutoActivityTracking(true);

        if (exceptionLogger != null) {
            exceptionLogger.log(getString(R.string.ga_trackingId));
            exceptionLogger.log(getString(R.string.gcm_defaultSenderId));
            exceptionLogger.log(getString(R.string.google_app_id));
        }
    }

    private void setupDefaultFont() {
        // must call follow snippet on each activity to make it work!
        //  @Override
        //  protected void attachBaseContext(Context newBase) {
        //      super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
        //  }
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/ThaiSansNeue-Regular.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
