package org.tanrabad.survey.presenter.authen.appauth;

import android.os.Bundle;
import android.support.annotation.Nullable;
import org.tanrabad.survey.presenter.TanrabadActivity;

public class LogoutActivity extends TanrabadActivity {

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //LogoutRedirectActivity will be called after complete
        AuthStateManager authState = AuthStateManager.getInstance(this);
        authState.endSession(this);
        authState.clear();
    }
}
