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

package org.tanrabad.survey.presenter;

import android.content.ComponentName;
import android.content.Intent;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.tanrabad.survey.base.TanrabadEspressoTestBase;
import org.tanrabad.survey.entity.lookup.PlaceType;
import org.tanrabad.survey.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class PlaceFormActivityByPutExtraPlaceTypeSchoolTest extends TanrabadEspressoTestBase {

    @Rule
    public ActivityTestRule<PlaceFormActivity> mActivityTestRule
            = new IntentsTestRule<>(PlaceFormActivity.class, false, false);
    PlaceFormActivity mActivity;

    @Before
    public void setUp() {
        Intent intent = new Intent();
        intent.putExtra("place_category_id_arg", PlaceType.SCHOOL);
        mActivity = mActivityTestRule.launchActivity(intent);
    }

    @Test
    public void openAddPlacePageByPutExtraPlaceTypeSchoolShouldPlaceTypeSelectorEqualsTypeSchool() {
        onView(withText("สถานศึกษา"))
                .check(matches(isDisplayed()));
    }

    @Test
    public void touchSaveByNotEnterDataShouldFoundPromptPleaseDefinePlaceName() {
        onView(withId(R.id.save))
                .perform(click());

        textDisplayed(R.string.please_define_place_name);
    }

    @Test
    public void touchSaveByTypeJustPlaceNameThenTouchSaveShouldFoundPromptPleaseDefineAddress() {
        onView(withId(R.id.place_name))
                .perform(replaceText("โรงเรียนอนุบาลเนคเทค"));
        onView(withId(R.id.save))
                .perform(click());

        textDisplayed(R.string.please_define_place_sub_type);
    }

    @Test
    public void touchDefineLocationShouldOpenIntentMarkPlaceLocationPage() {
        onView(withId(R.id.add_marker))
                .perform(click());

        Intents.intended(
                hasComponent(new ComponentName(mActivity, MapMarkerActivity.class)
                ));
    }

    @Test
    public void addPlaceNameSamePlaceNameInDatabaseShouldFoundPromptCannotSaveThisPlace() {


        onView(withId(R.id.place_name))
                .perform(replaceText(" วัดป่าภูก้อน "));
        onView(withId(R.id.address_select))
                .perform(click());
        onView(withText("นนทบุรี"))
                .perform(click());
        onView(withText("บางกรวย"))
                .perform(click());
        onView(withText("บางกรวย"))
                .perform(click());
        onView(withText("สถานศึกษา"))
                .perform(click());
        onView(withText("ศาสนสถาน"))
                .perform(click());
        onView(withId(R.id.place_subtype_selector))
            .perform(click());
        onView(withText("วัด"))
            .perform(click());
        onView(withId(R.id.add_marker))
                .perform(click());
        onView(withId(R.id.map_container))
                .perform(longClick());
        onView(withId(R.id.save_marker_menu))
                .perform(click());
        onView(withId(R.id.save))
                .perform(click());

        textDisplayed(R.string.cant_save_same_place_name);
    }
}
