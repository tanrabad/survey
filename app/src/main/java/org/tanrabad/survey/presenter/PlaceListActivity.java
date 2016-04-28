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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.tanrabad.survey.R;
import uk.co.chrisjenx.calligraphy.CalligraphyUtils;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

public class PlaceListActivity extends TanrabadActivity {

    private TabLayout placeListTabLayout;
    private ViewPager placePager;
    private PlacePagerAdapter placePagerAdapter;
    private Toolbar toolbar;

    public static void open(Activity activity) {
        Intent intent = new Intent(activity, PlaceListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_list);
        setupViews();
        setupHomeButton();
        setupTabPager();
        changeTabsFont();

        //displayShowcase();
    }

    private void setupViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        placeListTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        placePager = (ViewPager) findViewById(R.id.place_pager);
        setSupportActionBar(toolbar);
    }

    private void setupTabPager() {
        placePagerAdapter = new PlacePagerAdapter(
                getSupportFragmentManager(),
                PlaceListActivity.this,
                AccountUtils.getUser().getUsername());
        placePager.setAdapter(placePagerAdapter);
        placeListTabLayout.setupWithViewPager(placePager);
    }

    private void changeTabsFont() {
        ViewGroup vg = (ViewGroup) placeListTabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    CalligraphyUtils.applyFontToTextView(
                            (TextView) tabViewChild,
                            TypefaceUtils.load(this.getAssets(), "fonts/ThaiSansNeue-Regular.otf"));
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                PlaceSearchActivity.open(this);
                break;
            case android.R.id.home:
                MainActivity.open(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        placePagerAdapter.getItem(0).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainActivity.open(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_activity_place_search, menu);
        return super.onCreateOptionsMenu(menu);
    }
}