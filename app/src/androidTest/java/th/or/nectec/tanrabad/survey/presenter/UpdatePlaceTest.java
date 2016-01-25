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

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.TanrabadEspressoTestBase;

import java.util.UUID;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class UpdatePlaceTest extends TanrabadEspressoTestBase {

    @Rule
    public ActivityTestRule<PlaceFormActivity> mActivityTestRule
            = new IntentsTestRule<>(PlaceFormActivity.class, false, false);
    PlaceFormActivity mActivity;

    @Before
    public void setUp() {
        Intent intent = new Intent();
        intent.putExtra("place_uuid_arg", "37ebde70-aa49-4c06-b102-53a022d46370".toString());
        intent.putExtra("place_category_id_arg", Place.TYPE_HOSPITAL);
        mActivity = mActivityTestRule.launchActivity(intent);
    }

    @Test
    public void editPlaceNameAddressTypePlaceDefineLocationThenSaveShouldNotFoundCanNotSave() {
        onView(withId(R.id.place_name))
                .check(matches(withText("ธรรมศาสตร์")));
        onView(withId(R.id.place_name))
                .perform(replaceText("อนุบาลชมพู"));
        onView(withId(R.id.address_select))
                .perform(click());
        onView(withText("บางสีทอง"))
                .perform(click());
        onView(withText("โรงพยาบาล"))
                .perform(click());
        onView(withText("โรงเรียน"))
                .perform(click());
        onView(withId(R.id.add_marker))
                .perform(click());
        onView(withId(R.id.save_marker_menu))
                .perform(click());

        onView(withId(R.id.save))
                .perform(click());
    }
}
