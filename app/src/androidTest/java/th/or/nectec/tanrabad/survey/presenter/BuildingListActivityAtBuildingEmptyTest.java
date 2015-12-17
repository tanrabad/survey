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
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.Matchers;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.TanrabadEspressoTestBase;

import java.util.UUID;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class BuildingListActivityAtBuildingEmptyTest extends TanrabadEspressoTestBase {

    @Rule
    public ActivityTestRule<BuildingListActivity> mActivityTestRule
            = new IntentsTestRule<>(BuildingListActivity.class, false, false);
    BuildingListActivity mActivity;

    @Before
    public void setUp() {
        Intent intent = new Intent();
        intent.putExtra("place_uuid_arg", UUID.nameUUIDFromBytes("67UIP".getBytes()).toString());
        mActivity = mActivityTestRule.launchActivity(intent);
    }

    @Test
    public void openGoftViewShouldNotfoundBuildingInGoftViewPlace() {
        textDisplayed(R.string.define_building_survey);
        textDisplayed("ชุมชนกอล์ฟวิว");
        textDisplayed(R.string.building_list_not_found);
    }

    @Test
    public void clickAddBuildingShouldOpenIntendAddBuildingPage() {
        onView(withId(R.id.add_building_menu))
                .perform(click());

        Intents.intended(Matchers.allOf(
                hasComponent(new ComponentName(mActivity, BuildingFormActivity.class)),
                hasExtra(BuildingFormActivity.PLACE_UUID_ARG, UUID.nameUUIDFromBytes("67UIP".getBytes()).toString())
        ));
    }
}
