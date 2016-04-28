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
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.tanrabad.survey.base.TanrabadEspressoTestBase;
import org.tanrabad.survey.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class MainStartSurveyTest extends TanrabadEspressoTestBase {

    @Rule
    public IntentsTestRule<MainActivity> mActivityTestRule
            = new IntentsTestRule<>(MainActivity.class, false, false);
    MainActivity mActivity;

    @Before
    public void setUp() {
        Intent intent = new Intent();
        intent.putExtra("isUiTesting", true);
        mActivity = mActivityTestRule.launchActivity(intent);
    }

    @Test
    public void openMainPageShouldFoundMagnifyingGlassIsDisplay() {
        onView(withId(R.id.start_survey))
                .check(matches(isDisplayed()));
        textDisplayed(R.string.touch_to_start_survey);
    }

    @Test
    public void tapStartSurveyShouldOpenPlaceListPage() {
        onView(withId(R.id.start_survey))
                .perform(ViewActions.click());

        Intents.intended(Matchers.allOf(
                hasComponent(new ComponentName(mActivity, PlaceListActivity.class))
        ));
    }

    @Test
    public void ifOfflineShouldNotFoundSyncButton() {
        onView(withId(R.id.sync_data))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }
}
