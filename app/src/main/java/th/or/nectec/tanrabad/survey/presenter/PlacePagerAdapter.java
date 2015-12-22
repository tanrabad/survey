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

package th.or.nectec.tanrabad.survey.presenter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import th.or.nectec.tanrabad.survey.R;


public class PlacePagerAdapter extends FragmentPagerAdapter {

    private Context context;
    private final PlaceListInDatabaseFragment placeListInDatabaseFragment;
    private final PlaceSurveyListFragment placeSurveyListFragment;

    public PlacePagerAdapter(FragmentManager fm, Context context, String username) {
        super(fm);
        this.context = context;

        placeListInDatabaseFragment = PlaceListInDatabaseFragment.newInstance();
        placeSurveyListFragment = PlaceSurveyListFragment.newInstance(username);
    }

    @Override
    public int getCount() {
        return 2;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return placeListInDatabaseFragment;
            case 1:
                return placeSurveyListFragment;
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getResources().getString(R.string.find_place_by_database);
            case 1:
                return context.getResources().getString(R.string.find_place_by_recent_survey);
            default:
                return null;
        }
    }

    public void refreshPlaceListData() {
        placeListInDatabaseFragment.loadPlaceList();
    }
}
