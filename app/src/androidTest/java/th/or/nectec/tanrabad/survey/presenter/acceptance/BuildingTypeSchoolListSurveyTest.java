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
import android.support.test.rule.ActivityTestRule;
import org.junit.Before;
import org.junit.Test;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.TanrabadEspressoTestBase;
import th.or.nectec.tanrabad.survey.presenter.BuildingListActivity;

import java.util.UUID;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.*;

public class BuildingTypeSchoolListSurveyTest extends TanrabadEspressoTestBase {
    public ActivityTestRule<BuildingListActivity> mActivityTestRule
            = new ActivityTestRule<>(BuildingListActivity.class);
    BuildingListActivity mActivity;

    @Before
    public void setUp() {
        Intent intent = new Intent();
        intent.putExtra("place_uuid_arg", UUID.nameUUIDFromBytes("042ST".getBytes()).toString());
        mActivity = mActivityTestRule.launchActivity(intent);
    }

    @Test
    public void openBuildingTypeSchoolShouldNotfoundBuildinglist() {
        textDisplayed(R.string.define_building_survey);
        onView(withId(R.id.add_building_menu))
                .check(matches(isDisplayed()));
        textDisplayed("โรงเรียนเซนต์เมรี่");
        textDisplayed(R.string.title_card_building_list);
        textDisplayed(R.string.building_list_not_found);
    }
}
