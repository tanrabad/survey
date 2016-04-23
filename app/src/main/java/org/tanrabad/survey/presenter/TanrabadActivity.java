/*
 * Copyright (c) 2016 NECTEC
 *   National Electronics and Computer Technology Center, Thailand
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tanrabad.survey.presenter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import org.tanrabad.survey.utils.alert.Alert;
import org.tanrabad.survey.utils.showcase.ShowcaseFactory;
import org.tanrabad.survey.R;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

@SuppressLint("Registered")
public class TanrabadActivity extends AppCompatActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setupShowcaseFactory();
    }

    private void setupShowcaseFactory() {
        ShowcaseFactory.init(this);
    }

    protected void setupHomeButton() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    protected boolean isUiTesting() {
        return getIntent().getBooleanExtra("isUiTesting", false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupAlertFactory();
        setupShowcaseFactory();
    }

    private void setupAlertFactory() {
        Alert.init(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
}
