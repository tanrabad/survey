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

package org.tanrabad.survey.presenter;

import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.tanrabad.survey.base.TanrabadEspressoTestBase;
import th.or.nectec.tanrabad.survey.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;

@RunWith(AndroidJUnit4.class)
public class SurveyBuildingHistoryActivityTest extends TanrabadEspressoTestBase {
    @Rule
    public ActivityTestRule<SurveyBuildingHistoryActivity> mActivityTestRule
            = new IntentsTestRule<>(SurveyBuildingHistoryActivity.class, false, false);
    SurveyBuildingHistoryActivity mActivity;

    @Before
    public void setUp() {
        Intent intent = new Intent();
        intent.putExtra("place_uuid_arg", "9b27df17-4234-4b9b-b444-0dc3d637d1fe");
        intent.putExtra("username_arg", "dpc-user");
        mActivity = mActivityTestRule.launchActivity(intent);
    }

    @Test
    public void offlineShouldNotFoundPromptDeleteSurvey() {
        onView(withId(R.id.survey_container_count))
                .perform(longClick());

        onView(withText(R.string.delete_survey))
                .check(doesNotExist());
    }
}
