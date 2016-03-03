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

package th.or.nectec.tanrabad.survey.presenter.authen;

import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import org.trb.authen.client.TRBAuthenUtil;
import org.trb.authen.client.TRBCallback;
import org.trb.authen.model.UserProfile;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.presenter.TanrabadActivity;
import th.or.nectec.tanrabad.survey.utils.android.CookieUtils;

public class AuthenActivity extends TanrabadActivity {

    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authen);

        CookieUtils.clearCookies(this);
        webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new AuthenWebViewClient());
        loadAuthenticationPage(webView);
    }

    private void loadAuthenticationPage(WebView webView) {
        try {
            String authenUrl = TRBAuthenUtil.getInstance().getAuthorizationUri();
            webView.loadUrl(authenUrl);
        } catch (Exception expect) {
            expect.printStackTrace();
        }
    }

    public class AuthenWebViewClient extends WebViewClient {

        private static final String PARAM_CODE = "code";
        private static final String AUTHORIZED_CALLBACK_URL = "http://localhost/?";

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith(AUTHORIZED_CALLBACK_URL)) {
                Uri uri = Uri.parse(url);
                String code = uri.getQueryParameter(PARAM_CODE);
                TRBAuthenUtil.getInstance().onAuthorizationResult(code, new AuthenticationCallback());
                webView.stopLoading();
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onReceivedSslError(WebView view, @NonNull SslErrorHandler handler, SslError error) {
            handler.proceed();
        }
    }

    private class AuthenticationCallback implements TRBCallback {
        @Override
        public void onPostLogin() {
            UserProfile user = TRBAuthenUtil.getInstance().getUserProfile();
            Toast.makeText(AuthenActivity.this, "user =" + user, Toast.LENGTH_LONG).show();
            finish();
        }

        @Override
        public void onError(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
