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

package th.or.nectec.tanrabad.survey.utils;

import android.text.InputType;
import android.widget.EditText;

public class EditTextStepper {

    private EditText editText;
    private boolean unsignedValue = true;

    public EditTextStepper(EditText editText) {
        validate(editText);
        this.editText = editText;
    }

    private void validate(EditText editText) {
        if (editText == null)
            throw new IllegalArgumentException("EditText must not be null");
        if (!isNumberType(editText))
            throw new NotSupportEditTextInputTypeException("EditText's inputType must be CLASS NUMBER ");
    }

    private boolean isNumberType(EditText view) {
        return view.getInputType() == InputType.TYPE_CLASS_NUMBER;
    }

    public static void stepUp(EditText view) {
        new EditTextStepper(view).step(1);
    }

    public void step(int valueToStep) {
        int value = currentValueOf(editText) + valueToStep;
        if (unsignedValue && value < 0) {
            value = 0;
        }
        editText.setText(String.valueOf(value));
        editText.setSelection(editText.getText().length());
    }

    private int currentValueOf(EditText editText) {
        try {
            String currentString = editText.getText().toString().trim();
            return Integer.parseInt(currentString);
        } catch (NumberFormatException nfe) {
            return 0;
        }
    }

    public static void stepDown(EditText view) {
        new EditTextStepper(view).step(-1);
    }

    public EditTextStepper setUnsignedValue(boolean unsignedValue) {
        this.unsignedValue = unsignedValue;
        return this;
    }

    public static class NotSupportEditTextInputTypeException extends RuntimeException {
        public NotSupportEditTextInputTypeException(String message) {
            super(message);
        }
    }
}
