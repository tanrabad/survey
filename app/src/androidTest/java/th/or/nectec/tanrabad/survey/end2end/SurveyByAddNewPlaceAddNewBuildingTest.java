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

package th.or.nectec.tanrabad.survey.end2end;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.base.TanrabadEspressoTestBase;
import th.or.nectec.tanrabad.survey.presenter.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class SurveyByAddNewPlaceAddNewBuildingTest extends TanrabadEspressoTestBase {

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
    public void startSurveyByAddNewPlaceNewBuildingThenSurveyWhenSuccessShouldFoundBuildingNameAtSurveyHistoryPage() {
        onView(withId(R.id.start_survey))
                .perform(click());
        onView(withId(R.id.add_place_menu))
                .perform(click());
        onView(withId(R.id.place_name))
                .perform(replaceText("โรงงานพรทิพา"));
        onView(withText("ศาสนสถาน"))
                .perform(click());
        onView(withText("โรงงาน"))
                .perform(click());
        onView(withId(R.id.address_select))
                .perform(click());
        onView(withText("นนทบุรี"))
                .perform(click());
        onView(withText("ปากเกร็ด"))
                .perform(click());
        onView(withText("ปากเกร็ด"))
                .perform(click());
        onView(withId(R.id.save))
                .perform(click());
        onView(withId(R.id.add_building_menu))
                .perform(click());
        onView(withId(R.id.building_name))
                .perform(replaceText("อาคารพรทิพา"));
        onView(withId(R.id.add_marker))
                .perform(click());
        onView(withText(R.string.save_location))
                .perform(click());
        onView(withId(R.id.save))
                .perform(click());
        onView(withId(R.id.resident_count))
                .perform(replaceText("4"));
        waitingFor(4000);
        onView(withId(R.id.save))
                .perform(click());

        textDisplayed("อาคารพรทิพา");
    }
}
