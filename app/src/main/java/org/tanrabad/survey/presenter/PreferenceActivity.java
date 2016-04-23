package org.tanrabad.survey.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import th.or.nectec.tanrabad.survey.R;

public class PreferenceActivity extends TanrabadActivity {

    public static void open(Activity activity) {
        Intent intent = new Intent(activity, PreferenceActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        PreferenceFragment preferenceFragment = (PreferenceFragment) getSupportFragmentManager()
                .findFragmentByTag(PreferenceFragment.FRAGMENT_TAG);
        if (preferenceFragment == null) {
            preferenceFragment = new PreferenceFragment();
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, preferenceFragment, PreferenceFragment.FRAGMENT_TAG)
                .commit();
    }
}
