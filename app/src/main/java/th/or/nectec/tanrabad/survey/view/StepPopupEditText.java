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

package th.or.nectec.tanrabad.survey.view;

import android.content.Context;
import android.graphics.Rect;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.utils.EditTextStepper;

public class StepPopupEditText extends EditText implements View.OnClickListener {
    private PopupWindow popUp;
    private EditTextStepper editTextStepper;

    public StepPopupEditText(Context context) {
        this(context, null);
    }

    public StepPopupEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public StepPopupEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setInputType(InputType.TYPE_CLASS_NUMBER);
        initPopup();
        editTextStepper = new EditTextStepper(this);
    }

    private void initPopup() {
        popUp = new PopupWindow(this);
        LinearLayout layout = new LinearLayout(getContext());
        Button plus = new Button(getContext());
        plus.setId(R.id.plus);
        plus.setText("+");
        plus.setOnClickListener(this);
        Button minus = new Button(getContext());
        minus.setId(R.id.minus);
        minus.setText("-");
        minus.setOnClickListener(this);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(48 * 2,
                48 * 2);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.addView(plus, params);
        layout.addView(minus, params);
        popUp.setContentView(layout);

    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (focused) {
            showPopup();
        } else {
            dismissPopup();
        }
    }

    private void dismissPopup() {
        popUp.dismiss();
    }

    private void showPopup() {
        popUp.showAsDropDown(this, getXoffsetToCenter(), 0);
        popUp.update(50 * 2 * 2, 50 * 2);
        //popUp.showAtLocation(getRootView(), Gravity.BOTTOM, 10,10);
        //popUp.update(50, 50, 300, 80);
    }

    private int getXoffsetToCenter() {
        return 0 - getHalfPopupWidth() + getHalfWidth();
    }

    private int getHalfPopupWidth() {
        return 48 * 2 * 2 / 2;
    }

    private int getHalfWidth() {
        return this.getWidth() / 2;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.plus:
                editTextStepper.step(1);
                break;
            case R.id.minus:
                editTextStepper.step(-1);
                break;
        }
    }
}
