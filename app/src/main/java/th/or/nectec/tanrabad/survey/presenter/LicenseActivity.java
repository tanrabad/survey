package th.or.nectec.tanrabad.survey.presenter;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.artitk.licensefragment.RecyclerViewLicenseFragment;
import com.artitk.licensefragment.model.License;
import com.artitk.licensefragment.model.LicenseID;

import java.util.ArrayList;

import th.or.nectec.tanrabad.survey.R;

import static com.artitk.licensefragment.model.LicenseType.APACHE_LICENSE_20;
import static com.artitk.licensefragment.model.LicenseType.MIT_LICENSE;

public class LicenseActivity extends TanrabadActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        RecyclerViewLicenseFragment licenseFragment = RecyclerViewLicenseFragment.newInstance(getLicense());
        licenseFragment.addCustomLicense(getCustomLicenses());

        showLicenseFragment(licenseFragment);
    }

    private void showLicenseFragment(RecyclerViewLicenseFragment licenseFragment) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("licenses");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.replace(R.id.container, licenseFragment, "licenses");
        ft.addToBackStack(null);
        ft.commit();
    }

    private ArrayList<Integer> getLicense() {
        ArrayList<Integer> licenseIds = new ArrayList<>();
        licenseIds.add(LicenseID.OKHTTP);
        licenseIds.add(LicenseID.LICENSE_FRAGMENT);
        return licenseIds;
    }

    private ArrayList<License> getCustomLicenses() {
        ArrayList<License> licenses = new ArrayList<>();
        licenses.add(new License(this, "Joda-time", APACHE_LICENSE_20, "2002-2016", "joda.org"));
        licenses.add(new License(this, "LoganSquare", APACHE_LICENSE_20, "2015", "BlueLine Labs, Inc."));
        licenses.add(new License(this, "ShowcaseView", APACHE_LICENSE_20, "2012-2014", "Alex Curran (@amlcurran)"));
        licenses.add(new License(this, "JumpingBeans", APACHE_LICENSE_20, "2012-2014",
                "Frakbot (Sebastiano Poggi and Francesco Pontillo)"));
        licenses.add(new License(this, "RecyclerViewHeader", APACHE_LICENSE_20, "2015", "Bartosz Lipiński"));
        licenses.add(new License(this, "Calligraphy", APACHE_LICENSE_20, "2013", "Christopher Jenkins"));
        licenses.add(new License(this, "Mockito", MIT_LICENSE, "2007", "Mockito contributors"));
        licenses.add(new License(this, "WireMock", APACHE_LICENSE_20, "2014", "Tom Akehurst"));
        licenses.add(new License(this, "Google Maps Android API utility library", APACHE_LICENSE_20, "2013",
                "Google Inc."));
        licenses.add(new License(this, "Android Design Support Library", APACHE_LICENSE_20, "2005-2011",
                "The Android Open Source Project"));
        licenses.add(new License(this, "Android Annotations Support Library", APACHE_LICENSE_20, "2005-2011",
                "The Android Open Source Project"));
        licenses.add(new License(this, "Android Compatibility Library v7", APACHE_LICENSE_20, "2005-2011",
                "The Android Open Source Project"));
        return licenses;
    }
}
