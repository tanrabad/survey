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

package th.or.nectec.tanrabad.survey.presenter.acceptance;

import android.content.Intent;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import org.junit.Before;
import org.junit.Test;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.TanrabadEspressoTestBase;
import th.or.nectec.tanrabad.survey.presenter.SurveyActivity;

import java.util.UUID;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class SurveyBuildingTypeVillageCommunityTest extends TanrabadEspressoTestBase {
    public ActivityTestRule<SurveyActivity> mActivityTestRule
            = new ActivityTestRule<>(SurveyActivity.class);
    SurveyActivity mActivity;

    @Before
    public void setUp() {
        Intent intent = new Intent();
        intent.putExtra("building_uuid", UUID.nameUUIDFromBytes("1xyz".getBytes()).toString());
        intent.putExtra("username_arg", "sara");
        mActivity = mActivityTestRule.launchActivity(intent);
    }

    @Test
    public void openSurveyHouseShouldFoundHouseData() {
        textDisplayed(R.string.title_activity_survey);
        onView(withId(R.id.torch))
                .check(matches(isDisplayed()));
        onView(withId(R.id.save))
                .check(matches(isDisplayed()));
        onView(withId(R.id.building_name))
                .check(matches(withText("บ้านเลขที่ 214/43")));
        onView(withId(R.id.place_name))
                .check(matches(withText("หมู่บ้านพาลาซเซตโต้")));
        textDisplayed(R.string.number_of_resident);
        textDisplayed(R.string.person);
        textDisplayed(R.string.indoor);
        textDisplayed(R.string.outdoor);
        onView(withId(R.id.save))
                .perform(click());
        textDisplayed(R.string.please_enter_resident);
        onView(withText(R.string.got_it))
                .perform(click());
    }
}
