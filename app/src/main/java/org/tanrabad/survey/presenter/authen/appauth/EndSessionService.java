/*
 * Copyright (c) 2019 NECTEC
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

package org.tanrabad.survey.presenter.authen.appauth;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import net.openid.appauth.AuthState;
import net.openid.appauth.browser.BrowserDescriptor;
import net.openid.appauth.browser.BrowserSelector;
import okhttp3.OkHttpClient;
import org.tanrabad.survey.R;

class EndSessionService {

    private static final String TAG = "EndSessionService";

    private static final String URL = "https://authen.tanrabad.org/oxauth/restv1/end_session"
        + "?id_token_hint=%s&post_logout_redirect_uri=trb-survey://localhost/signout";

    protected final OkHttpClient client = new OkHttpClient();
    private Context context;
    private BrowserDescriptor browser;

    EndSessionService(Context context) {
        this.context = context;
        browser = BrowserSelector.select(context, AppAuthPresenter.browserMatcher);
    }

    void end(AuthState state) {
        Log.i(TAG, "End session of Token id=" + state.getIdToken());
        String url = String.format(URL, state.getIdToken()).trim();


        //CustomTabManager customTabManager = new CustomTabManager(context);
        //customTabManager.bind(browser.packageName);
        Log.i(TAG, "Browser package=" + browser.packageName);
        //CustomTabsIntent.Builder builder = customTabManager.createTabBuilder();

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(ContextCompat.getColor(context, R.color.purple));
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.intent.setPackage(browser.packageName);
        customTabsIntent.intent.putExtra(Intent.EXTRA_REFERRER,
            Uri.parse("android-app://" + context.getPackageName()));
        customTabsIntent.intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        customTabsIntent.launchUrl(context, Uri.parse(url));
    }
}
