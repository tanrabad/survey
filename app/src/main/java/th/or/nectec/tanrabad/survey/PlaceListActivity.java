/*
 * Copyright (c) 2015 NECTEC
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

package th.or.nectec.tanrabad.survey;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

public class PlaceListActivity extends TanrabadActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager placePager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_list);

        setupViews();
        setupTabPager();
    }

    private void setupViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        placePager = (ViewPager) findViewById(R.id.place_pager);
        setSupportActionBar(toolbar);
    }

    private void setupTabPager() {
        PagerAdapter placePagerAdapter = new PlacePagerAdapter(getSupportFragmentManager(), PlaceListActivity.this, "sara");
        placePager.setAdapter(placePagerAdapter);
        tabLayout.setupWithViewPager(placePager);
    }


}
