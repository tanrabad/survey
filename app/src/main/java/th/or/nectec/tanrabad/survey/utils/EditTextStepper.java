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

package th.or.nectec.tanrabad.survey.utils;

import android.text.InputType;
import android.view.View;
import android.widget.EditText;

public class EditTextStepper {

    public static void stepUp(View view) {
        new EditTextStepper().step(view, 1, true);
    }

    public static void stepDown(View view) {
        new EditTextStepper().step(view, -1, true);
    }

    public void step(View view, int valueToStep, boolean unsigned) {
        if (view == null || !(view instanceof EditText))
            return;
        EditText editText = (EditText) view;
        if (!isNumberType(editText))
            return;
        int value = currentValueOf(editText) + valueToStep;
        if (unsigned && value < 0) {
            value = 0;
        }
        editText.setText(String.valueOf(value));
        editText.setSelection(editText.getText().length());
    }

    private int currentValueOf(EditText editText) {
        String currentString = editText.getText().toString().trim();
        try {
            return Integer.parseInt(currentString);
        } catch (NumberFormatException nfe) {
            return 0;
        }
    }

    private boolean isNumberType(EditText view) {
        return view.getInputType() == InputType.TYPE_CLASS_NUMBER;
    }
}
