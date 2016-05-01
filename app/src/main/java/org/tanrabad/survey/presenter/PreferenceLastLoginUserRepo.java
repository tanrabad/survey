package org.tanrabad.survey.presenter;

import android.content.Context;
import android.content.SharedPreferences;

import org.tanrabad.survey.TanrabadApp;
import org.tanrabad.survey.entity.User;
import org.tanrabad.survey.repository.BrokerUserRepository;

public class PreferenceLastLoginUserRepo implements AccountUtils.LastLoginUserRepo {

    private static final String PREF_NAME = "user";
    private static final String KEY_USER = "lastLogin";
    private static final String KEY_TIMESTAMP = "lastLoginTimeStamp";

    @Override
    public void userLogin(User user) {
        SharedPreferences.Editor editor = getUserPreference().edit();
        editor.putString(KEY_USER, user.getUsername());
        editor.putLong(KEY_TIMESTAMP, AccountUtils.currentTimer.getInMills());
        editor.apply();
    }

    private static SharedPreferences getUserPreference() {
        return TanrabadApp.getInstance().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public User getLastLoginUser() {
        SharedPreferences userPreference = getUserPreference();
        String username = userPreference.getString(KEY_USER, null);
        long timestampInMills = userPreference.getLong(KEY_TIMESTAMP, 0);
        long currentInMills = AccountUtils.currentTimer.getInMills();

        if (currentInMills - timestampInMills < AccountUtils.REMEMBER_LIMIT) {
            return BrokerUserRepository.getInstance().findByUsername(username);
        } else {
            return null;
        }
    }

    @Override
    public void clear() {
        getUserPreference().edit().clear().apply();
    }
}
