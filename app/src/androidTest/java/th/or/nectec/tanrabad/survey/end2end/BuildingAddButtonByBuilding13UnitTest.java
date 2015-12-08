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
import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.TanrabadEspressoTestBase;
import th.or.nectec.tanrabad.survey.presenter.BuildingListActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

public class BuildingAddButtonByBuilding13UnitTest extends TanrabadEspressoTestBase {
    public ActivityTestRule<BuildingListActivity> mActivityTestRule = new ActivityTestRule<>(BuildingListActivity.class);
    BuildingListActivity mAtivity;

    @Before
    public void setUp() {
        Intent intent = new Intent();
        intent.putExtra("place_uuid_arg", UUID.nameUUIDFromBytes("1abc".getBytes()).toString());
        intent.putExtra("username_arg", "sara");
        mAtivity = mActivityTestRule.launchActivity(intent);
    }

    @Test
    public void testClickBuildingAddButtonShouldOpenBuildingAddPage() {
        String placeName = "หมู่บ้านพาลาซเซตโต้";
        String houseNo = "13/7";

//        textDisplayed("เพิ่มอาคาร");
        textDisplayed(placeName);
        onView(withId(R.id.card_title))
                .check(matches(withText(R.string.title_card_building_list)));
        onView(allOf(withId(R.id.building_count), withContentDescription(R.string.number_building_list)))
                .check(matches(withText("13 อาคาร")));
        onView(withText(R.string.add_building))
                .perform(click());
        textDisplayed("เพิ่มอาคาร");
        textDisplayed(placeName);
        textDisplayed(R.string.save);
        textDisplayed(R.string.house_no);
        textDisplayed(R.string.building_location);
        textDisplayed(R.string.define_building_location);
        onView(withId(R.id.building_name))
                .perform(replaceText(houseNo));
        onView(withId(R.id.add_marker))
                .perform(click());
        onView(withText(R.string.save_location))
                .perform(click());
        onView(withId(R.id.building_name))
                .check(matches(withText(houseNo)));
        textDisplayed(R.string.edit_location);
        onView(withText(R.string.save))
                .perform(click());
        textDisplayed(R.string.title_activity_survey);
        onView(withId(R.id.place_name))
                .check(matches(withText(placeName)));
        onView(withId(R.id.building_name))
                .check(matches(withText("บ้านเลขที่" + " " + houseNo)));
        pressBack();
        textDisplayed(R.string.define_building_survey);
        textDisplayed(houseNo);
    }
}
