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

package org.tanrabad.survey.presenter.view;

import android.annotation.SuppressLint;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.text.InputType;
import android.widget.EditText;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.tanrabad.survey.utils.EditTextStepper;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
@SuppressLint("SetTextI18n")
public class EditTextStepperTest {

    private EditText editText;
    private EditTextStepper editTextStepper;

    @Before
    public void setUp() throws Exception {
        editText = new EditText(InstrumentationRegistry.getContext());
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editTextStepper = new EditTextStepper(editText);
    }

    @Test
    public void testStepUp() throws Exception {
        editText.setText("34");
        editTextStepper.step(1);
        assertEquals("35", editText.getText().toString());
        editTextStepper.step(1);
        assertEquals("36", editText.getText().toString());
        editTextStepper.step(5);
        assertEquals("41", editText.getText().toString());
        editTextStepper.step(100);
        assertEquals("141", editText.getText().toString());
    }

    @Test
    public void testStepDownUnsigned() throws Exception {
        editText.setText("34");
        editTextStepper.setUnsignedValue(true);
        editTextStepper.step(-1);
        assertEquals("33", editText.getText().toString());
        editTextStepper.step(-50);
        assertEquals("0", editText.getText().toString());
    }

    @Test
    public void testStepDownSigned() throws Exception {
        editText.setText("34");
        editTextStepper.setUnsignedValue(false);
        editTextStepper.step(-1);
        assertEquals("33", editText.getText().toString());
        editTextStepper.step(-50);
        assertEquals("-17", editText.getText().toString());
    }

    @Test(expected = EditTextStepper.NotSupportEditTextInputTypeException.class)
    public void testSupportOnlyNumberClassInputType() {
        new EditTextStepper(new EditText(InstrumentationRegistry.getContext()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullPointerExceptionIfEditTextIsNull() throws Exception {
        new EditTextStepper(null);
    }

    @Test
    public void testStaticMethod() throws Exception {
        editText.setText("34");
        EditTextStepper.stepUp(editText);
        assertEquals("35", editText.getText().toString());
        EditTextStepper.stepUp(editText);
        assertEquals("36", editText.getText().toString());

        editText.setText("1");
        EditTextStepper.stepDown(editText);
        assertEquals("0", editText.getText().toString());
        EditTextStepper.stepDown(editText);
        assertEquals("0", editText.getText().toString());
    }
}
