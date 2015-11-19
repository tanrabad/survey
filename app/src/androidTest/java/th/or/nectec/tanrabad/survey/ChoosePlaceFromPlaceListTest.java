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

package th.or.nectec.tanrabad.survey;


import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class ChoosePlaceFromPlaceListTest extends TANRABADInstrumentationBaseTest {
    public ActivityTestRule<PlaceListActivity> mActivityTestRule =
            new ActivityTestRule<>(PlaceListActivity.class);
    PlaceListActivity mAtivity;

    @Before
    public void setUp() {
        Intent intent = new Intent();
        mAtivity = mActivityTestRule.launchActivity(intent);
    }

    @Test
    public void testChoosePlaceThenBuildingNotFound() {
        onView(withText("ชุมชนกอล์ฟวิว"))
                .perform(click());
        onView(withText("ชุมชนกอล์ฟวิว"))
                .check(matches(isDisplayed()));
        onView(withId(R.id.text_show_title_building_list))
                .check(matches(withText("รายชื่ออาคาร")));
        onView(withId(R.id.building_count))
                .check(matches(withText("0")));
        pressBack();
    }

    @Test
    public void testChoosePlaceThenFound3Building() {
        onView(withText("หมู่บ้านพาลาซเซตโต้"))
                .perform(click());
        onView(withText("หมู่บ้านพาลาซเซตโต้"))
                .check(matches(isDisplayed()));
        onView(withId(R.id.text_show_title_building_list))
                .check(matches(withText("รายชื่ออาคาร")));
        onView(withId(R.id.building_count))
                .check(matches(withText("3")));
        onView(withText("214/43"))
                .check(matches(isDisplayed()));
        onView(withText("214/44"))
                .check(matches(isDisplayed()));
        onView(withText("214/45"))
                .check(matches(isDisplayed()));
        pressBack();
    }
}
