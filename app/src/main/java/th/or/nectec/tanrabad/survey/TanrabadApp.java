/*
 * Copyright (c) 2015 NECTEC
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
import th.or.nectec.tanrabad.survey.utils.tool.ActionLogger;
import th.or.nectec.tanrabad.survey.utils.tool.CrashLogger;
import th.or.nectec.tanrabad.survey.utils.tool.FabricTools;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class TanrabadApp extends Application {

    private static CrashLogger crashLogger;
    private static ActionLogger actionLogger;
    private static TanrabadApp tanrabadApp;

    public static TanrabadApp instance() {
        return tanrabadApp;
    }

    public static ActionLogger action() {
        return actionLogger;
    }

    public static CrashLogger error() {
        return crashLogger;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        tanrabadApp = this;
        setupAnalysisTools();
        setupDefaultFont();
    }

    private void setupAnalysisTools() {
        FabricTools fabricTools = FabricTools.getInstance(this);
        crashLogger = fabricTools;
        actionLogger = fabricTools;
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
