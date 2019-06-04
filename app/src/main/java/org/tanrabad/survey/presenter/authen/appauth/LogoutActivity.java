package org.tanrabad.survey.presenter.authen.appauth;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationResponse;

import org.tanrabad.survey.BuildConfig;
import org.tanrabad.survey.TanrabadApp;
import org.tanrabad.survey.presenter.TanrabadActivity;

public class LogoutActivity extends TanrabadActivity {

    private AuthStateManager mStateManager;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mStateManager = AuthStateManager.getInstance(this);
        checkAuthorize();
    }

    private void checkAuthorize() {

        AuthState current = mStateManager.getCurrent();
        Log.d("LogoutActivity", String.format("state authorized=%s id_token=%s",
            current.isAuthorized(),
            current.getIdToken()));
        if (current.isAuthorized() && current.getIdToken() != null) {
            startLogoutProcess();
            return;
        }

        // the stored AuthState is incomplete, so check if we are currently receiving the result of
        // the authorization flow from the browser.
        AuthorizationResponse response = AuthorizationResponse.fromIntent(getIntent());
        AuthorizationException ex = AuthorizationException.fromIntent(getIntent());

        if (response != null || ex != null) {
            mStateManager.updateAfterAuthorization(response, ex);
        }
        if (response != null && response.authorizationCode != null) {
            // authorization code exchange is required
            mStateManager.updateAfterAuthorization(response, ex);
            startLogoutProcess();
        } else if (ex != null) {
            displayNotAuthorized("Authorization flow failed: " + ex.getMessage());
            TanrabadApp.log(ex);
        } else {
            displayNotAuthorized("No authorization state retained - reauthorization required");
            TanrabadApp.log(new IllegalStateException("No authorization state retained"));
        }
    }

    private void startLogoutProcess() {
        //LogoutRedirectActivity will be called after complete
        AuthStateManager authState = AuthStateManager.getInstance(this);
        authState.endSession(this);
    }

    private void displayNotAuthorized(String message) {
        if (BuildConfig.DEBUG)
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "ไม่สามารถออกจากระบบ\nกรุณาลองใหม่ภายหลัง", Toast.LENGTH_SHORT).show();
        finish();
    }
}
