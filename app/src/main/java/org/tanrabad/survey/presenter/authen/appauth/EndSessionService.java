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
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.util.Log;

import net.openid.appauth.AuthState;

import org.tanrabad.survey.TanrabadApp;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

class EndSessionService {

    private static final String URL = "https://authen.tanrabad.org/oxauth/restv1/end_session" +
        "?id_token_hint=%s&post_logout_redirect_uri=trb-survey://localhost/signout";

    protected final OkHttpClient client = new OkHttpClient();
    private Context context;

    EndSessionService(Context context) {
        this.context = context;
    }

    void end(AuthState state) {
        String url = String.format(URL, state.getIdToken()).trim();

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(context, Uri.parse(url));
    }
}
