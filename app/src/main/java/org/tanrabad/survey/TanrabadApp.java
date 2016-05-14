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

package org.tanrabad.survey;

import android.app.Application;
import android.content.Context;
import cat.ereza.customactivityoncrash.CustomActivityOnCrash;
import com.onesignal.OneSignal;
import org.tanrabad.survey.presenter.LoginActivity;
import org.tanrabad.survey.utils.android.ResourceUtils;
import org.tanrabad.survey.utils.tool.ActionLogger;
import org.tanrabad.survey.utils.tool.ExceptionLogger;
import org.tanrabad.survey.utils.tool.GoogleAnalyticsTool;
import org.trb.authen.client.TRBAuthenUtil;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class TanrabadApp extends Application {

    public static final String TRB_AUTHEN_ENDPOINT = "https://authen.tanrabad.org/";
    private static ExceptionLogger exceptionLogger;
    private static ActionLogger actionLogger;
    private static TanrabadApp instance;

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
        setupAuthenUtils();
        setupCrashActivity();
        setupAnalysisTools();
        setupDefaultFont();
        setupNotification();
    }

    private void setupNotification() {
        OneSignal.startInit(this)
                .init();
        OneSignal.enableInAppAlertNotification(false);
        OneSignal.enableNotificationsWhenActive(true);
        ResourceUtils.from(this).getDrawable(R.drawable.ic_onesignal_large_icon_default);
        ResourceUtils.from(this).getDrawable(R.drawable.ic_stat_onesignal_default);
    }


    private void setupAuthenUtils() {
        TRBAuthenUtil authenUtil = TRBAuthenUtil.getInstance();
        authenUtil.initTRBAuthenUtil(this, TRB_AUTHEN_ENDPOINT, BuildConfig.TRB_AUTHEN_CLIENT_ID,
                BuildConfig.TRB_AUTHEN_CLIENT_SECRET);
    }

    private void setupCrashActivity() {
        //must call this before setup fabric tools
        CustomActivityOnCrash.install(this);
        CustomActivityOnCrash.setRestartActivityClass(LoginActivity.class);
        CustomActivityOnCrash.setLaunchErrorActivityWhenInBackground(false);
        CustomActivityOnCrash.setShowErrorDetails(BuildConfig.DEBUG);
        CustomActivityOnCrash.setEnableAppRestart(true);
        CustomActivityOnCrash.setDefaultErrorActivityDrawable(R.drawable.sad_cloud);
    }

    private void setupAnalysisTools() {
        GoogleAnalyticsTool logger = GoogleAnalyticsTool.getInstance(this);
        exceptionLogger = logger;
        actionLogger = logger;
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
