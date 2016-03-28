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

package th.or.nectec.tanrabad.survey.end2end;

import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.base.TanrabadEspressoTestBase;
import th.or.nectec.tanrabad.survey.presenter.BuildingListActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

public class BuildingListActivityDisplayedTextSurveyedTest extends TanrabadEspressoTestBase {

    @Rule
    public ActivityTestRule<BuildingListActivity> mActivityTestRule
            = new IntentsTestRule<>(BuildingListActivity.class, false, false);
    BuildingListActivity mActivity;

    @Before
    public void setUp() {
        Intent intent = new Intent();
        intent.putExtra("place_uuid_arg", "9b27df17-4234-4b9b-b444-0dc3d637d1fe");
        mActivity = mActivityTestRule.launchActivity(intent);
    }

    @Test
    public void surveyBuildingThenOpenBuildingListPageShouldFoundTextSurveyedAtThisBuilding() {
        onView(withText("โบสถ์ริมรั้ว"))
                .perform(click());
        onView(withId(R.id.resident_count))
                .perform(replaceText("4"));
        onView(withId(R.id.save))
                .perform(click());
        onView(withId(R.id.survey_more_building_button))
                .perform(click());

        onView(allOf(withId(R.id.surveyed),
                hasSibling(withText("โบสถ์ริมรั้ว"))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void surveyedPeriod7DaysShouldShowTextSurveyedAtThisBuilding() {
        onView(allOf(withId(R.id.surveyed),
                hasSibling(withText("ศาลาใหญ่"))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void surveyedMoreThan7DaysShouldNotShowTextSurveyedAtThisBuilding() {
        onView(allOf(withId(R.id.surveyed),
                hasSibling(withText("ลานหน้าศาลากลาง"))))
                .check(matches(not(isDisplayed())));
    }

    @Test
    public void surveyByOtherUserShouldNotShowTextSurveyedAtThisBuilding() {
        onView(allOf(withId(R.id.surveyed),
                hasSibling(withText("เมรุ"))))
                .check(matches(not(isDisplayed())));
    }
}
