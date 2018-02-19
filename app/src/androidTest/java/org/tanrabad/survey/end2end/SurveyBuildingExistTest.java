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

package org.tanrabad.survey.end2end;

import android.content.Intent;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.tanrabad.survey.R;
import org.tanrabad.survey.base.TanrabadEspressoTestBase;
import org.tanrabad.survey.presenter.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class SurveyBuildingExistTest extends TanrabadEspressoTestBase {

    public ActivityTestRule<MainActivity> mActivityTestRule
            = new ActivityTestRule<>(MainActivity.class);
    MainActivity mActivity;

    @Before
    public void setUp() {
        Intent intent = new Intent();
        intent.putExtra("isUiTesting", true);
        mActivity = mActivityTestRule.launchActivity(intent);
    }

    @Test
    public void startSurveyWhenFinishShouldFoundBuildingNameAtSurveyHistoryPage() {
        onView(withId(R.id.start_survey))
                .perform(click());

        ViewInteraction tabView = onView(
            allOf(childAtPosition(
                childAtPosition(
                    withId(R.id.tab_layout),
                    0),
                1),
                isDisplayed()));
        tabView.perform(click());
        waitingFor(3000);
        //manual เลือก หมู่บ้านพาลาซเซตโต้


        onView(withText("หมู่บ้านพาลาซเซตโต้"))
                .perform(click());
        onView(withText("สำรวจ"))
                .perform(click());
        onView(withText("214/43"))
                .perform(click());
        onView(withId(R.id.resident_count))
                .perform(replaceText("4"));
        onView(withId(R.id.save))
                .perform(click());
        onView(withId(R.id.survey_more_building_button))
                .perform(click());
        onView(withText("214/44"))
                .perform(click());
        onView(withId(R.id.resident_count))
                .perform(replaceText("3"));
        onView(withId(R.id.save))
                .perform(click());

        textDisplayed("214/44");
        textDisplayed("214/43");
    }

    private static Matcher<View> childAtPosition(
        final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                    && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
