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

package th.or.nectec.tanrabad.survey;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;


@RunWith(AndroidJUnit4.class)
public class SurveyTest {
    public ActivityTestRule<SurveyActivity> mActivityTestRule = new ActivityTestRule<>(SurveyActivity.class);
    SurveyActivity mActivity;

    @Before
    public void setUp(){
        Intent intent = new Intent();
        intent.addCategory(".SurveyActivity");
        mActivity = mActivityTestRule.launchActivity(intent);
    }

    @Test
    public void newSurvey(){
        onView(withText("บ้านพาลาซเซตโต้"))
                .check(matches(isDisplayed()));
        onView(withId(R.id.building_name))
                .check(matches(withText("214/44")));
        onView(withId(R.id.place_name))
                .check(matches(withText("บ้านพาลาซเซตโต้")));
        onView(withId(R.id.resident_count))
                .perform(replaceText("5"));
        onView(allOf(withId(R.id.total_container), withContentDescription("น้ำใช้ภายในอาคาร")))
                .perform(replaceText("4"));
        onView(allOf(withId(R.id.found_larvae_container), withContentDescription("น้ำใช้ภายในอาคาร")))
                .perform(replaceText("1"));
        onView(allOf(withId(R.id.total_container), withContentDescription("น้ำดื่มภายในอาคาร")))
                .perform(replaceText("10"));
        onView(allOf(withId(R.id.found_larvae_container), withContentDescription("น้ำดื่มภายในอาคาร")))
                .perform(replaceText("1"));
        onView(allOf(withId(R.id.total_container), withContentDescription("แจกันภายในอาคาร")))
                .perform(replaceText("4"));
        onView(allOf(withId(R.id.found_larvae_container), withContentDescription("แจกันภายในอาคาร")))
                .perform(replaceText("4"));
        onView(allOf(withId(R.id.total_container), withContentDescription("ที่รองกันมดภายในอาคาร")))
                .perform(replaceText("8"));
        onView(allOf(withId(R.id.found_larvae_container), withContentDescription("ที่รองกันมดภายในอาคาร")))
                .perform(replaceText("0"));
        onView(allOf(withId(R.id.total_container), withContentDescription("จานรองกระถางภายในอาคาร")))
                .perform(replaceText("4"));
        onView(allOf(withId(R.id.found_larvae_container), withContentDescription("จานรองกระถางภายในอาคาร")))
                .perform(replaceText("1"));
        onView(allOf(withId(R.id.total_container), withContentDescription("อ่างบัว/ไม้น้ำภายในอาคาร")))
                .perform(replaceText("3"));
        onView(allOf(withId(R.id.found_larvae_container), withContentDescription("อ่างบัว/ไม้น้ำภายในอาคาร")))
                .perform(replaceText("1"));
        onView(allOf(withId(R.id.total_container), withContentDescription("ภาชนะที่ไม่ใช้ภายในอาคาร")))
                .perform(replaceText("2"));
        onView(allOf(withId(R.id.found_larvae_container), withContentDescription("ภาชนะที่ไม่ใช้ภายในอาคาร")))
                .perform(replaceText("1"));
        onView(allOf(withId(R.id.total_container), withContentDescription("ยางรถยนต์เก่าภายในอาคาร")))
                .perform(replaceText("5"));
        onView(allOf(withId(R.id.found_larvae_container), withContentDescription("ยางรถยนต์เก่าภายในอาคาร")))
                .perform(replaceText("2"));
        onView(allOf(withId(R.id.total_container), withContentDescription("กากใบพืชภายในอาคาร")))
                .perform(replaceText("1"));
        onView(allOf(withId(R.id.found_larvae_container), withContentDescription("ยางรถยนต์เก่าภายในอาคาร")))
                .perform(replaceText("1"));
        onView(allOf(withId(R.id.total_container), withContentDescription("อื่นๆ (ที่ใช้ประโยชน์)ภายในอาคาร")))
                .perform(replaceText("3"));
        onView(allOf(withId(R.id.found_larvae_container), withContentDescription("อื่นๆ (ที่ใช้ประโยชน์)ภายในอาคาร")))
                .perform(replaceText("1"));

        waitingFor(50000);

    }

    protected void waitingFor(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
