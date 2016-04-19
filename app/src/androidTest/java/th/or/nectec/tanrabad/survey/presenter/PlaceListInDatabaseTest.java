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
import th.or.nectec.tanrabad.entity.lookup.PlaceType;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.base.TanrabadEspressoTestBase;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
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
        onView(allOf(ViewMatchers.withId(R.id.place_count),
                withContentDescription(R.string.number_place_list_in_database)))
                .check(matches(containText("10")));
    }

    @Test
    public void chooseTypeVillageCommunityShouldFound2List() {
        changePlaceTypeFilterTo(R.string.village_community);

        onView(allOf(withId(R.id.place_count),
                withContentDescription(R.string.number_place_list_in_database)))
                .check(matches(containText("3")));
        textDisplayed("หมู่บ้านพาลาซเซตโต้");
        textDisplayed("ชุมชนกอล์ฟวิว");
        textDisplayed("หมู่บ้านทดสอบ");

    }

    private void changePlaceTypeFilterTo(@StringRes int placeType) {
        onView(withId(R.id.place_filter))
                .perform(click());
        onView(withText(placeType))
                .perform(click());
    }

    @Test
    public void chooseTypeFactoryShouldNotFoundListPlace() {
        changePlaceTypeFilterTo(R.string.factory);

        textDisplayed(R.string.places_not_found);
    }

    @Test
    public void touchAddPlaceNotChooseType() {
        onView(withId(R.id.add_place_menu))
                .perform(click());

        Intents.intended(
                hasComponent(new ComponentName(mActivity, PlaceFormActivity.class)
                ));
    }

    @Test
    public void touchAddPlaceTypeFactory() {
        changePlaceTypeFilterTo(R.string.factory);

        onView(withId(R.id.add_place_menu))
                .perform(click());
        Intents.intended(Matchers.allOf(
                hasComponent(new ComponentName(mActivity, PlaceFormActivity.class)),
                hasExtra(PlaceFormActivity.PLACE_TYPE_ID_ARG, PlaceType.FACTORY)
        ));
    }

    @Test
    public void touchAddPlaceTypeSchool() {
        changePlaceTypeFilterTo(R.string.school);

        onView(withId(R.id.add_place_menu))
                .perform(click());
        Intents.intended(Matchers.allOf(
                hasComponent(new ComponentName(mActivity, PlaceFormActivity.class)),
                hasExtra(PlaceFormActivity.PLACE_TYPE_ID_ARG, PlaceType.SCHOOL)
        ));
    }

    @Test
    public void touchPlaceShouldOpenDefineBuildingPage() {
        onView(withText("โบสถ์เซนต์เมรี่"))
                .perform(click());
        onView(withText("สำรวจ"))
                .perform(click());

        Intents.intended(Matchers.allOf(
                hasComponent(new ComponentName(mActivity, SurveyBuildingHistoryActivity.class)),
                hasExtra(SurveyBuildingHistoryActivity.PLACE_UUID_ARG, "febb0058-3007-41ae-91d8-de2c3160c935")
        ));

        Intents.intended(Matchers.allOf(
                hasComponent(new ComponentName(mActivity, BuildingListActivity.class)),
                hasExtra(BuildingListActivity.PLACE_UUID_ARG, "febb0058-3007-41ae-91d8-de2c3160c935")
        ));
    }

    @Test
    public void touchSearhShouldOpenPlaceSearchPage() {
        onView(withId(R.id.action_search))
                .perform(click());

        Intents.intended(
                hasComponent(new ComponentName(mActivity, PlaceSearchActivity.class)
                ));
    }

    @Test
    public void offlineShouldNotFoundPromptDeletePlace() {
        changePlaceTypeFilterTo(R.string.village_community);
        onView(withText("ชุมชนกอล์ฟวิว"))
                .perform(longClick());

        onView(withText(R.string.delete_place))
                .check(doesNotExist());
    }
}
