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

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import org.junit.Before;
import org.junit.Test;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.TanrabadEspressoTestBase;

import java.util.UUID;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.allOf;

public class ChooseBuildingFromTypeVillageCommunityListTest extends TanrabadEspressoTestBase {
    public ActivityTestRule<BuildingListActivity> mActivityTestRule = new ActivityTestRule<>(BuildingListActivity.class);
    BuildingListActivity mActivity;

    @Before
    public void setUp() {
        Intent intent = new Intent();
        intent.putExtra("place_uuid_arg", UUID.nameUUIDFromBytes("1abc".getBytes()).toString());
        intent.putExtra("username_arg", "sara");
        mActivity = mActivityTestRule.launchActivity(intent);
    }

    @Test
    public void testClickBuildingAddButtonShouldOpenBuildingAddPage() {
        onView(allOf(withId(R.id.empty_button), hasSibling(withText("เพิ่มอาคาร"))))
                .perform(click());
        textDisplayed("เพิ่มอาคาร");
        textDisplayed("หมู่บ้านพาลาซเซตโต้");
        onView(withId(R.id.card_title))
                .check(matches(withText(R.string.title_card_building_list)));
        onView(allOf(withId(R.id.building_count), withContentDescription(R.string.number_building_list)))
                .check(matches(withText("13")));
        onView(withText("214/43"))
                .perform(click());
        textDisplayed(R.string.title_activity_survey);
        onView(withId(R.id.building_name))
                .check(matches(withText("บ้านเลขที่ 214/43")));
        textDisplayed("หมู่บ้านพาลาซเซตโต้");
        textDisplayed(R.string.number_of_resident);
        textDisplayed(R.string.person);
        textDisplayed(R.string.save);
        pressBack();
    }


}
