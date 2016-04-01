package th.or.nectec.tanrabad.survey.presenter;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.google.android.gms.analytics.GoogleAnalytics;

import th.or.nectec.tanrabad.survey.R;

public class PreferenceFragment extends android.preference.PreferenceFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        addPreferencesFromResource(R.xml.fragment_preference);
        SharedPreferences userPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        userPrefs.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if ("google_analytics".equals(key)) {
                    GoogleAnalytics.getInstance(getActivity()).setAppOptOut(sharedPreferences.getBoolean(key, false));
                }
            }
        });
    }
}
