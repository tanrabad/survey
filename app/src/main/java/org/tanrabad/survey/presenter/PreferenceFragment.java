package org.tanrabad.survey.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.preference.PreferenceFragmentCompat;

import org.tanrabad.survey.BuildConfig;
import org.tanrabad.survey.R;
import org.tanrabad.survey.utils.tool.FabricTools;

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
                    FabricTools.getInstance(context).setAppOptOut(sharedPreferences.getBoolean(key, false));
                }
            }
        });

        findPreference("google_maps_key").setSummary(googleMapKey());
        findPreference("trb_authen_key").setSummary(trbAuthenKey());

    }


    private String trbAuthenKey() {
        try {
            return getString(R.string.blind_trb_authen_key,
                BuildConfig.TRB_AUTHEN_CLIENT_ID.substring(0, 6),
                BuildConfig.TRB_AUTHEN_CLIENT_ID.substring(47),
                BuildConfig.TRB_AUTHEN_CLIENT_SECRET.substring(5));
        } catch (IndexOutOfBoundsException ex) {
            return "ERROR!!";
        }
    }

    private String googleMapKey() {
        try {
            return getString(R.string.blind_google_maps_key,
                BuildConfig.GOOGLE_MAP_KEY.substring(0, 4),
                BuildConfig.GOOGLE_MAP_KEY.substring(35));
        } catch (IndexOutOfBoundsException ex) {
            return "ERROR!!";
        }
    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);

    }
}
