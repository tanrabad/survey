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
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.text.Html;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.TanrabadEspressoTestBase;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;

@RunWith(AndroidJUnit4.class)
public class PlaceSearchActivityTest extends TanrabadEspressoTestBase {

    public ActivityTestRule<PlaceSearchActivity> mActivityTestRule
            = new ActivityTestRule<>(PlaceSearchActivity.class);
    PlaceSearchActivity mActivity;

    @Before
    public void setUp() {
        Intent intent = new Intent();
        mActivity = mActivityTestRule.launchActivity(intent);
    }

    @Test
    public void searchPlaceNotExistInDatabaseShouldFoundTextNoResultsFound() {
        onView(withHint(R.string.search_place_by_name_hint))
                .perform(replaceText("บางป่า"))
                .perform(ViewActions.pressImeActionButton());

        textDisplayed(Html.fromHtml(
                String.format(mActivity.getString(R.string.place_name_not_found),
                        "บางป่า")).toString());
    }

    @Test
    public void searchPlaceExistInDatabaseShouldFoundThisPlace() {
        onView(withHint(R.string.search_place_by_name_hint))
                .perform(replaceText("โต้"))
                .perform(ViewActions.pressImeActionButton());

        textDisplayed("หมู่บ้านพาลาซเซตโต้");
    }
}