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
import android.support.annotation.Nullable;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import org.trb.authen.client.TRBAuthenUtil;
import org.trb.authen.client.TRBCallback;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.presenter.TanrabadActivity;
import th.or.nectec.tanrabad.survey.utils.android.CookieUtils;

public class AuthenActivity extends TanrabadActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authen);

        WebView webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new TBRWebViewClient());
        CookieUtils.clearCookies(this);
        try {
            webView.loadUrl(TRBAuthenUtil.getInstance().getAuthorizationUri());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class TBRWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("http://localhost/?")) {
                Uri uri = Uri.parse(url);
                String code = uri.getQueryParameter("code");
                //Toast.makeText(AuthenActivity.this, "code =" + code, Toast.LENGTH_SHORT).show();
                TRBAuthenUtil.getInstance().onAuthorizationResult(code, new TRBCallback() {
                    @Override
                    public void onPostLogin() {
                        String user = TRBAuthenUtil.getInstance().getUser();
                        Toast.makeText(AuthenActivity.this, "user =" + user, Toast.LENGTH_LONG).show();
                        finish();
                    }

                    @Override
                    public void onError(Exception e) {
                        throw new RuntimeException(e);
                    }

                });
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }
    }

}
