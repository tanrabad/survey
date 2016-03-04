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
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import org.trb.authen.client.TRBAuthenUtil;
import org.trb.authen.client.TRBCallback;
import org.trb.authen.model.UserProfile;
import th.or.nectec.tanrabad.entity.User;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.presenter.TanrabadActivity;
import th.or.nectec.tanrabad.survey.utils.android.CookieUtils;

public class AuthenActivity extends TanrabadActivity {

    private WebView webView;
    private WebChromeClient webChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            setProgress(newProgress * 1000);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authen);

        CookieUtils.clearCookies(this);
        webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new AuthenWebViewClient());
        webView.setWebChromeClient(webChromeClient);
        loadAuthenticationPage();
    }

    private void loadAuthenticationPage() {
        try {
            String authenUrl = TRBAuthenUtil.getInstance().getAuthorizationUri();
            webView.loadUrl(authenUrl);
        } catch (Exception expect) {
            expect.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class AuthenWebViewClient extends WebViewClient {

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
            UserProfile profile = TRBAuthenUtil.getInstance().getUserProfile();
            User user = new UserMapper(profile).getUser();
            finish();
        }

        @Override
        public void onError(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
