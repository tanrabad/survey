package org.tanrabad.survey.presenter.authen.appauth;

import android.content.Context;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import org.tanrabad.survey.R;
import org.tanrabad.survey.presenter.authen.UserProfile;

class UserStateManager {

    private static final String EDIT_PROFILE_URL = "https://authen.tanrabad.org/iaamreg/edit-profile";
    private static final String REG_URL = "https://authen.tanrabad.org/iaamreg/";

    private UserProfile userProfile;
    private Boolean performedAction = false;

    UserStateManager(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    boolean isRequireAction() {
        return !userProfile.isEmailVerified() || !userProfile.isActive();
    }

    void performRequireAction(Context context) {
        if (!userProfile.isEmailVerified()) {
            performedAction = true;
            launchEditProfile(context);
        } else if (!userProfile.isActive()) {
            performedAction = true;
            launchInactivePage(context);
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

    private void launchInactivePage(Context context) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(ContextCompat.getColor(context, R.color.purple_dark));
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(context, Uri.parse(REG_URL));
    }
}
