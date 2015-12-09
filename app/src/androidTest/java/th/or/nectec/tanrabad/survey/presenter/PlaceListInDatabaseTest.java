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

import android.content.ComponentName;
import android.content.Intent;

import android.support.annotation.StringRes;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.Matchers;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.TanrabadEspressoTestBase;

import java.util.UUID;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class PlaceListInDatabaseTest extends TanrabadEspressoTestBase {

    @Rule
    public ActivityTestRule<PlaceListActivity> mActivityTestRule
            = new IntentsTestRule<>(PlaceListActivity.class, false, false);
    PlaceListActivity mActivity;

    @Before
    public void setUp() {
        Intent intent = new Intent();
        mActivity = mActivityTestRule.launchActivity(intent);
    }

    @Test
    public void openDefineSurveyPlaceShouldFoundListPlace8List() {
        onView(allOf(ViewMatchers.withId(R.id.place_count)
                , withContentDescription(R.string.number_place_list_in_database)))
                .check(matches(containText("8")));
    }

    @Test
    public void chooseTypeVillageCommunityShouldFound2List() {
        changePlaceTypeFilterTo(R.string.village_community);

        onView(allOf(withId(R.id.place_count)
                , withContentDescription(R.string.number_place_list_in_database)))
                .check(matches(containText("2")));
        textDisplayed("หมู่บ้านพาลาซเซตโต้");
        textDisplayed("ชุมชนกอล์ฟวิว");
    }

    @Test
    public void chooseTypeFactoryShouldNotFoundListPlace() {
        changePlaceTypeFilterTo(R.string.factory);

        textDisplayed(R.string.places_not_found);
    }

    @Test
    public void clickAddPlaceNotChooseType() {
        onView(withId(R.id.add_place_menu))
                .perform(click());

        Intents.intended(
                hasComponent(new ComponentName(mActivity, PlaceFormActivity.class)
        ));
    }

    @Test
    public void clickAddPlaceTypeFactory() {
        changePlaceTypeFilterTo(R.string.factory);


        onView(withId(R.id.add_place_menu))
                .perform(click());
        Intents.intended(Matchers.allOf(
                hasComponent(new ComponentName(mActivity, PlaceFormActivity.class)),
                hasExtra(PlaceFormActivity.PLACE_TYPE_ID_ARG, Place.TYPE_FACTORY)
        ));
    }

    @Test
    public void clickAddPlaceTypeSchool() {
        changePlaceTypeFilterTo(R.string.school);

        onView(withId(R.id.add_place_menu))
                .perform(click());
        Intents.intended(Matchers.allOf(
                hasComponent(new ComponentName(mActivity, PlaceFormActivity.class)),
                hasExtra(PlaceFormActivity.PLACE_TYPE_ID_ARG, Place.TYPE_SCHOOL)
        ));
    }

    @Test
    public void clickPlaceShouldOpenDefindBuildingPage() {
        onView(withText("ชุมชนกอล์ฟวิว"))
                .perform(click());
        onView(withText("สำรวจ"))
                .perform(click());

        Intents.intended(Matchers.allOf(
                hasComponent(new ComponentName(mActivity, BuildingListActivity.class)),
                hasExtra(BuildingListActivity.PLACE_UUID_ARG, UUID.nameUUIDFromBytes("67UIP".getBytes()).toString())
        ));
    }

    private void changePlaceTypeFilterTo(@StringRes int placeType) {
        onView(withId(R.id.place_filter))
                .perform(click());
        onView(withText(placeType))
                .perform(click());
    }
}
