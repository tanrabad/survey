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

package org.tanrabad.survey.presenter.authen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import org.tanrabad.survey.R;
import org.tanrabad.survey.TanrabadApp;
import org.tanrabad.survey.domain.organization.OrganizationRepository;
import org.tanrabad.survey.domain.user.UserRepository;
import org.tanrabad.survey.entity.Organization;
import org.tanrabad.survey.entity.User;
import org.tanrabad.survey.presenter.TanrabadActivity;
import org.tanrabad.survey.repository.BrokerOrganizationRepository;
import org.tanrabad.survey.repository.BrokerUserRepository;
import org.tanrabad.survey.utils.android.CookieUtils;
import org.trb.authen.client.TRBAuthenUtil;
import org.trb.authen.client.TRBCallback;
import org.trb.authen.model.UserProfile;

@SuppressLint("SetJavaScriptEnabled")
public class AuthenActivity extends TanrabadActivity {

    public static final int RESULT_ERROR = 1923;
    public static final String USERNAME = "username";

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
        webView.getSettings().setJavaScriptEnabled(true);
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

    @Override
    protected void onStop() {
        webView.clearCache(true);
        super.onStop();
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

        @Override public void onReceivedSslError(WebView v, SslErrorHandler handler, SslError er) {
            handler.proceed();
        }
    }

    private class AuthenticationCallback implements TRBCallback {

        private OrganizationRepository organizationRepository;
        private UserRepository userRepository;

        @Override
        public void onPostLogin() {
            UserProfile profile = TRBAuthenUtil.getInstance().getUserProfile();
            UserProfileMapper userProfileMapper = new UserProfileMapper(profile);

            Organization org = userProfileMapper.getOrganization();
            saveOrUpdate(org);

            User user = userProfileMapper.getUser();
            saveOrUpdate(user);

            Intent intent = new Intent();
            intent.putExtra(USERNAME, user.getUsername());
            setResult(RESULT_OK, intent);
            finish();
        }

        private void saveOrUpdate(Organization org) {
            organizationRepository = BrokerOrganizationRepository.getInstance();
            Organization organization = organizationRepository.findById(org.getOrganizationId());
            if (organization == null) {
                organizationRepository.save(org);
            } else {
                organizationRepository.update(org);
            }
        }

        private void saveOrUpdate(User user) {
            userRepository = BrokerUserRepository.getInstance();
            User userInRepo = userRepository.findByUsername(user.getUsername());
            if (userInRepo == null) {
                userRepository.save(user);
            } else {
                userRepository.update(user);
            }
        }

        @Override
        public void onError(Exception e) {
            TanrabadApp.log(e);

            setResult(RESULT_ERROR);
            finish();
        }

    }
}
