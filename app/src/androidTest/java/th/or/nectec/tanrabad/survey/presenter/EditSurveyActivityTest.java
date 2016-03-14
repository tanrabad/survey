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

import android.content.ComponentName;
import android.content.Intent;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.base.TanrabadEspressoTestBase;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class EditSurveyActivityTest extends TanrabadEspressoTestBase {

    @Rule
    public ActivityTestRule<SurveyActivity> mActivityTestRule
            = new IntentsTestRule<>(SurveyActivity.class, false, false);
    SurveyActivity mActivity;

    @Before
    public void setUp() {
        Intent intent = new Intent();
        intent.putExtra("building_uuid", "6a2fc9f1-3489-4a73-a425-b3d4e5b0e34b");
        intent.putExtra("username_arg", "dpc-user");
        mActivity = mActivityTestRule.launchActivity(intent);
    }

    @Test
    public void editSurveyThenTouchSaveShouldOpenIntentSurveyBuildingHistoryPage() {
        onView(withId(R.id.resident_count))
                .perform(replaceText("9"));

        onView(withId(R.id.save))
                .perform(click());

        Intents.intended(Matchers.allOf(
                hasComponent(new ComponentName(mActivity, SurveyBuildingHistoryActivity.class)),
                hasExtra(SurveyBuildingHistoryActivity.PLACE_UUID_ARG, "9b27df17-4234-4b9b-b444-0dc3d637d1fe")
        ));
    }
}

