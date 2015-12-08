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
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.TanrabadEspressoTestBase;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class PlaceListInDatabaseTest extends TanrabadEspressoTestBase {
    public ActivityTestRule<PlaceListActivity> mActivityTestRule
            = new ActivityTestRule<>(PlaceListActivity.class);
    PlaceListActivity mActivity;

    @Before
    public void setUp() {
        Intent intent = new Intent();
        mActivity = mActivityTestRule.launchActivity(intent);
    }

    @Test
    public void openDefineSurveyPlaceShouldFoundListPlace8List() {
        onView(allOf(ViewMatchers.withId(R.id.place_count)
                , withContentDescription(R.string.number_place_list_in_database)))
                .check(matches(withText("8 รายการ")));
    }

    @Test
    public void chooseTypeVillageCommunityShouldFound2List() {
        clickPlaceTypeNotDefine();
        onView(withText(R.string.village_community))
                .perform(click());

        onView(allOf(withId(R.id.place_count)
                , withContentDescription(R.string.number_place_list_in_database)))
                .check(matches(withText("2 รายการ")));
        textDisplayed("หมู่บ้านพาลาซเซตโต้");
        textDisplayed("ชุมชนกอล์ฟวิว");
    }

    @Test
    public void chooseTypeFactoryShouldNotFoundListPlace() {
        clickPlaceTypeNotDefine();
        onView(withText(R.string.factory))
                .perform(click());

        textDisplayed(R.string.places_not_found);
    }

    private void clickPlaceTypeNotDefine() {
        onView(withText(R.string.not_define_place_type))
                .perform(click());
    }
}
