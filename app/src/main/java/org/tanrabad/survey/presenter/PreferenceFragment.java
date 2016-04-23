package org.tanrabad.survey.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.preference.PreferenceFragmentCompat;
import com.google.android.gms.analytics.GoogleAnalytics;
import th.or.nectec.tanrabad.survey.R;

public class PreferenceFragment extends PreferenceFragmentCompat {

    public static final String FRAGMENT_TAG = "pref_fragment";
    private Context context;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.fragment_preference);
        SharedPreferences userPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        userPrefs.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if ("google_analytics".equals(key)) {
                    GoogleAnalytics.getInstance(context).setAppOptOut(sharedPreferences.getBoolean(key, false));
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);

    }
}
