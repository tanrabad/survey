package org.tanrabad.survey.presenter.authen.appauth;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.StringRes;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import org.tanrabad.survey.R;
import org.tanrabad.survey.presenter.authen.UserProfile;

class UserStateManager {

    public static final String TAG = "UserStateManager";

    private static final String EDIT_PROFILE_URL = "https://authen.tanrabad.org/iaamreg/edit-profile";
    private static final String REG_URL = "https://authen.tanrabad.org/iaamreg/";

    private UserProfile userProfile;
    private Boolean performedAction = false;

    UserStateManager(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    boolean isRequireAction() {
        Log.i(TAG, String.format("isDefinedEmail=%s, isEmailVerified=%s isActive=%s",
            userProfile.isDefinedEmail(),
            userProfile.isEmailVerified(),
            userProfile.isActive()));
        return !userProfile.isDefinedEmail()
            || !userProfile.isEmailVerified()
            || !userProfile.isActive();
    }

    @StringRes
    public int getUserStateRes() {
        if (!userProfile.isDefinedEmail())
            return R.string.state_undefined_email;
        else if (!userProfile.isEmailVerified())
            return R.string.state_unverified_email;
        else if (!userProfile.isActive())
            return R.string.state_not_approve;
        else
            return R.string.state_active;
    }

    void performRequireAction(Context context) {
        if (!userProfile.isDefinedEmail()) {
            Log.i(TAG, "Undefined email");
            performedAction = true;
            launchEditProfile(context);
        } else if (!userProfile.isEmailVerified() || !userProfile.isActive()) {
            Log.i(TAG, "Email not verify nor account active yet");
            performedAction = true;
            launchRegisterStage(context);
        } else {
            throw new IllegalStateException("Have no require action for current user");
        }
    }

    public Boolean isPerformedAction() {
        return performedAction;
    }

    private void launchEditProfile(Context context) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(ContextCompat.getColor(context, R.color.purple_dark));
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(context, Uri.parse(EDIT_PROFILE_URL));
    }

    private void launchRegisterStage(Context context) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(ContextCompat.getColor(context, R.color.purple_dark));
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(context, Uri.parse(REG_URL));
    }
}
