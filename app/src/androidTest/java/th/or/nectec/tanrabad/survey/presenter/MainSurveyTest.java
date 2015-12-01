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

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class MainSurveyTest extends TanrabadEspressoTestBase {
    public ActivityTestRule<MainActivity> mActivityTestRule
            = new ActivityTestRule<>(MainActivity.class);

    MainActivity mActivity;
    @Before
    public void setUp() {
        Intent intent = new Intent();
        intent.putExtra("isUiTesting",true);
        mActivity = mActivityTestRule.launchActivity(intent);
    }
    @Test
    public void testClickMagnifyingGlassShoutOpenDefinePlaceSurvey() {
        textDisplayed(R.string.press_to_start_survey);
        textDisplayed(R.string.find_place_by_recent_survey);
        onView(withId(R.id.start_survey))
                .perform(click());
        textDisplayed(R.string.define_place_survey);
        textDisplayed(R.string.find_place_by_database);
        pressBack();
    }
}
