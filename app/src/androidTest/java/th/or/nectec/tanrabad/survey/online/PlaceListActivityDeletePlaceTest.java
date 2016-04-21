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

package th.or.nectec.tanrabad.survey.online;

import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.base.TanrabadEspressoTestBase;
import th.or.nectec.tanrabad.survey.presenter.PlaceListActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class PlaceListActivityDeletePlaceTest extends TanrabadEspressoTestBase {

    @Rule
    public ActivityTestRule<PlaceListActivity> mActivityTestRule
            = new IntentsTestRule<>(PlaceListActivity.class, false, false);
    PlaceListActivity mActivity;

    @Before
    public void setUp() {
        Intent intent = new Intent();
        mActivity = mActivityTestRule.launchActivity(intent);
    }

    @Test
    public void chooseDeletePlaceMenuThenChooseDeleteShouldGoPlaceListPage() {
        onView(withText("ชุมชนกอล์ฟวิว"))
                .perform(click());
        onView(withText("สำรวจ"))
                .perform(click());
        onView(withContentDescription("More options"))
                .perform(click());
        onView(withText(R.string.delete_place))
                .perform(click());

        onView(withText(R.string.delete))
                .perform(click());

        textDisplayed(R.string.define_place_survey);
    }

    @Test
    public void chooseDeletePlaceMenuThenChooseCancelShouldStayAtBuilingListPage() {
        onView(withText("ชุมชนกอล์ฟวิว"))
                .perform(click());
        onView(withText("สำรวจ"))
                .perform(click());
        onView(withContentDescription("More options"))
                .perform(click());
        onView(withText(R.string.delete_place))
                .perform(click());

        onView(withText(R.string.cancel))
                .perform(click());

        textDisplayed(R.string.define_building_survey);
    }

    @Test
    public void chooseDeletePlaceHaveBuildingsShouldFoundPromptPleaseDeleteBuildingInPlace() {
        onView(withText("หมู่บ้านทดสอบ"))
                .perform(click());
        onView(withText("สำรวจ"))
                .perform(click());
        onView(withContentDescription("More options"))
                .perform(click());
        onView(withText(R.string.delete_place))
                .perform(click());

        textDisplayed(R.string.please_delete_building_in_place);
    }

    @Test
    public void foundPromptPleaseDeleteBuildingInPlaceThenTapGotItShouldStayAtBuilingListPage() {
        onView(withText("หมู่บ้านทดสอบ"))
                .perform(click());
        onView(withText("สำรวจ"))
                .perform(click());
        onView(withContentDescription("More options"))
                .perform(click());
        onView(withText(R.string.delete_place))
                .perform(click());

        onView(withText(R.string.got_it))
                .perform(click());

        textDisplayed(R.string.define_building_survey);
    }
}
