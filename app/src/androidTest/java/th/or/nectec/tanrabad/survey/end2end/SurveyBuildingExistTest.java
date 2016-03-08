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
        onView(withText("หมู่บ้านพาลาซเซตโต้"))
                .perform(click());
        onView(withText("สำรวจ"))
                .perform(click());
        onView(withText("214/43"))
                .perform(click());
        onView(withId(R.id.resident_count))
                .perform(replaceText("4"));
        waitingFor(4000);
        onView(withId(R.id.save))
                .perform(click());
        onView(withId(R.id.survey_more_building_button))
                .perform(click());
        onView(withText("214/44"))
                .perform(click());
        onView(withId(R.id.resident_count))
                .perform(replaceText("3"));
        waitingFor(4000);
        onView(withId(R.id.save))
                .perform(click());

        textDisplayed("214/44");
        textDisplayed("214/43");
    }
}
