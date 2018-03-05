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

package org.tanrabad.survey.presenter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.tanrabad.survey.BuildConfig;

import java.util.ArrayList;
import java.util.List;


class PlacePagerAdapter extends FragmentPagerAdapter {

    private final Context context;
    private final List<TanrabadTabFragment> fragments = new ArrayList<>();

    public PlacePagerAdapter(Context context, FragmentManager fm, String username) {
        super(fm);
        this.context = context;
        fragments.add(PlaceNearbyListFragment.newInstance());
        fragments.add(PlaceSurveyListFragment.newInstance(username));
        if (BuildConfig.DEBUG) {
            fragments.add(PlaceListFragment.newInstance());
        }
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return context.getString(fragments.get(position).title());
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }
}
