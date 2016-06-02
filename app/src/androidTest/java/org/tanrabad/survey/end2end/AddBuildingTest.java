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
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.tanrabad.survey.R;
import org.tanrabad.survey.base.TanrabadEspressoTestBase;
import org.tanrabad.survey.presenter.BuildingFormActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class AddBuildingTest extends TanrabadEspressoTestBase {

    public ActivityTestRule<BuildingFormActivity> mActivityTestRule
            = new ActivityTestRule<>(BuildingFormActivity.class);
    BuildingFormActivity mActivity;

    @Before
    public void setUp() {
        Intent intent = new Intent();
        intent.putExtra("place_uuid_arg", "e5ce769e-f397-4409-bec2-818f7bd02464");
        intent.setAction(Intent.ACTION_INSERT);
        mActivity = mActivityTestRule.launchActivity(intent);
    }

    @Test
    public void typeBuildingNameAndLocationThenTouchSaveShouldFoundSurveyPage() {

        String buildingName = "ตึก1 สีม่วง";
        onView(withId(R.id.building_name))
                .perform(replaceText(buildingName));
        onView(withId(R.id.add_marker))
                .perform(click());
        onView(withId(R.id.map_container))
                .perform(longClick());
        onView(withText(R.string.save_location))
                .perform(click());
        onView(withId(R.id.save))
                .perform(click());

        onView(withId(R.id.place_name))
                .check(matches(withText("ชุมชนกอล์ฟวิว")));
        onView(withId(R.id.building_name))
                .check(matches(containText(buildingName)));
    }
}
