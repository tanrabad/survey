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
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.core.AllOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.tanrabad.survey.base.TanrabadEspressoTestBase;
import org.tanrabad.survey.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class SurveyActivityTest extends TanrabadEspressoTestBase {

    @Rule
    public ActivityTestRule<SurveyActivity> mActivityTestRule
            = new IntentsTestRule<>(SurveyActivity.class, false, false);
    SurveyActivity mActivity;

    @Before
    public void setUp() {
        Intent intent = new Intent();
        intent.putExtra("building_uuid", "f5bfd399-8fb2-4a69-874a-b40495f7786f");
        intent.putExtra("username_arg", "dpc-user");
        mActivity = mActivityTestRule.launchActivity(intent);
    }

    @Test
    public void openSurveyHouseShouldFoundHouseData() {
        onView(withId(R.id.building_name))
                .check(matches(containText("214/43")));
        onView(withId(R.id.place_name))
                .check(matches(withText("หมู่บ้านพาลาซเซตโต้")));
    }

    @Test
    public void touchSaveByNotTypeResidentShouldFoundPromptPleaseEnterResidentThenTouchGotItShouldStayOn() {
        onView(withId(R.id.save))
                .perform(click());
        textDisplayed(R.string.please_enter_resident);
        onView(withText(R.string.got_it))
                .perform(click());

        onView(withId(R.id.building_name))
                .check(matches(containText("214/43")));
    }

    @Test
    public void touchPressBackShouldFoundPromptAbortSurveyThenTouchNoShouldStayOn() {
        pressBack();
        onView(containText("214/43"))
                .check(matches(isDisplayed()));
        onView(withText(R.string.no))
                .perform(click());

        onView(withId(R.id.building_name))
                .check(matches(containText("214/43")));
    }

    @Test
    public void surveySuccessThenTouchSaveShouldOpenIntentSurveyBuildingHistoryPage() {
        onView(withId(R.id.resident_count))
                .perform(replaceText("9"));

        onView(container(Location.INDOOR, "น้ำใช้", Field.TOTAL))
                .perform(replaceText("100"));
        onView(container(Location.OUTDOOR, "น้ำดื่ม", Field.FOUND))
                .perform(replaceText("20"));
        waitingFor(2000);

        onView(withId(R.id.save))
                .perform(click());

        Intents.intended(Matchers.allOf(
                hasComponent(new ComponentName(mActivity, SurveyBuildingHistoryActivity.class)),
                hasExtra(SurveyBuildingHistoryActivity.PLACE_UUID_ARG, "935b9aeb-6522-461e-994f-f9e9006c4a33")
        ));
    }

    private Matcher<View> container(Location location, String containerType, Field field) {
        return AllOf.allOf(
                withParent(withParent(withParent(withId(location.locationLayoutId)))),
                withId(field.editTextId),
                withContentDescription(containerType)
        );
    }

    enum Location {
        INDOOR(R.id.indoor_container),
        OUTDOOR(R.id.outdoor_container);

        final int locationLayoutId;

        Location(int locationLayoutId) {
            this.locationLayoutId = locationLayoutId;
        }
    }

    enum Field {
        TOTAL(R.id.total_container),
        FOUND(R.id.found_larvae_container);

        final int editTextId;

        Field(int editTextId) {
            this.editTextId = editTextId;
        }
    }
}
