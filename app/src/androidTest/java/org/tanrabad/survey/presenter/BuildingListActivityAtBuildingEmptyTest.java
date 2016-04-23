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

import android.content.ComponentName;
import android.content.Intent;
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
import org.tanrabad.survey.base.TanrabadEspressoTestBase;
import th.or.nectec.tanrabad.survey.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;

@RunWith(AndroidJUnit4.class)
public class BuildingListActivityAtBuildingEmptyTest extends TanrabadEspressoTestBase {

    private static final String PLACE_UUID = "e5ce769e-f397-4409-bec2-818f7bd02464";
    @Rule
    public ActivityTestRule<BuildingListActivity> mActivityTestRule
            = new IntentsTestRule<>(BuildingListActivity.class, false, false);
    BuildingListActivity mActivity;

    @Before
    public void setUp() {
        Intent intent = new Intent();
        intent.putExtra("place_uuid_arg", PLACE_UUID);
        mActivity = mActivityTestRule.launchActivity(intent);
    }

    @Test
    public void openGoftViewShouldNotfoundBuildingInGoftViewPlace() {
        textDisplayed(R.string.define_building_survey);
        textDisplayed("ชุมชนกอล์ฟวิว");
        textDisplayed(R.string.building_list_not_found);
    }

    @Test
    public void clickAddBuildingShouldOpenIntendAddBuildingPage() {
        onView(withId(R.id.add_building_menu))
                .perform(click());

        Intents.intended(Matchers.allOf(
                hasComponent(new ComponentName(mActivity, BuildingFormActivity.class)),
                hasExtra(BuildingFormActivity.PLACE_UUID_ARG, PLACE_UUID)
        ));
    }

    @Test
    public void openPlaceAtEmptyBuildingShouldNotFoundButtonEditBuilding() {
        onView(withId(R.id.edit_building))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }

    @Test
    public void ifOfflineThenTapDeletePlaceShouldFoundPromptConnectInternet() {
        onView(withContentDescription("More options"))
                .perform(click());
        onView(withText(R.string.delete_place))
                .perform(click());

        onView(withText(R.string.please_enable_internet_before_delete))
                .check(matches(isDisplayed()));

    }

    @Test
    public void ifOfflineTouchMoreOptionsShouldFoundEditPlaceMenu() {
        onView(withContentDescription("More options"))
                .perform(click());

        onView(withText(R.string.edit_place))
                .check(matches(isDisplayed()));
    }

    @Test
    public void ifOfflineTouchMoreOptionsThenChooseEditPlaceMenuShouldFoundBuildingFormPage() {
        onView(withContentDescription("More options"))
                .perform(click());
        onView(withText(R.string.edit_place))
                .perform(click());

        onView(withText(R.string.edit_place))
                .check(matches(isDisplayed()));
    }
}
