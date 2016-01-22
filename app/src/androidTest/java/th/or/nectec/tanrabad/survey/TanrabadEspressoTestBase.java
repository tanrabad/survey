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

import org.hamcrest.Matcher;
import org.junit.Rule;
import th.or.nectec.tanrabad.survey.repository.persistence.SurveyDbTestRule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;

public class TanrabadEspressoTestBase {
    @Rule
    public SurveyDbTestRule dbTestRule = new SurveyDbTestRule();

    protected void waitingFor(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected Matcher containText(String text) {
        return withText(containsString(text));
    }

    protected void clickSurveyButton() {
        onView(withText((R.string.survey)))
                .perform(click());
    }

    protected void clickCancelButton() {
        onView(withText((R.string.cancel)))
                .perform(click());
    }

    protected void textDisplayed(String text) {
        onView(withText(text))
                .check(matches(isDisplayed()));
    }

    protected void textDisplayed(int text) {
        onView(withText(text))
                .check(matches(isDisplayed()));
    }
}
