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

package org.tanrabad.survey.utils.android;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class SoftKeyboard {

    public static void hideOn(Activity activity) {
        // Snippet in this method is under license CC BY-SA 3.0
        // original from answer at StackOverflow https://stackoverflow.com/q/7696791
        View currentFocus = activity.getCurrentFocus();
        if (currentFocus == null)
            currentFocus = new View(activity);
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
    }

}
