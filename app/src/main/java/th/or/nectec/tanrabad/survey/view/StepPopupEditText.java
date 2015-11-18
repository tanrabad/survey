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

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.utils.EditTextStepper;

public class StepPopupEditText extends EditText implements View.OnClickListener {
    public static final int BUTTON_SIZE_DP = 52;
    private EditTextPopup popUp;
    private EditTextStepper editTextStepper;

    public StepPopupEditText(Context context) {
        this(context, null);
    }

    public StepPopupEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public StepPopupEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupEditText();
        editTextStepper = new EditTextStepper(this);
        popUp = new EditTextPopup(context);
    }

    private void setupEditText() {
        setInputType(InputType.TYPE_CLASS_NUMBER);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissPopup();
            }
        });
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
        if (popUp.isShowing())
            popUp.dismiss();
    }

    private void showPopup() {
        popUp.showAsDropDown(this, getXoffset(), getYoffset());
        popUp.update(popUp.getPopupWidth(), popUp.getButtonSize());
    }

    private int getXoffset() {
        return 0 - popUp.getPopupWidth() / 2 + this.getWidth() / 2;
    }

    private int getYoffset() {
        return convertToPX(6);
    }

    private int convertToPX(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density + 0.5f);
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

    class EditTextPopup extends PopupWindow {

        public EditTextPopup(Context context) {
            super(context);
            init();
        }

        private void init() {
            setOutsideTouchable(true);
            setBackgroundDrawable(getDrawable(R.drawable.popup_bg));

            ImageButton minus = getButton(R.id.minus, R.mipmap.ic_minus_1);
            ImageButton plus = getButton(R.id.plus, R.mipmap.ic_plus_1);
            setContentView(getLayout(minus, plus));
        }

        private LinearLayout getLayout(ImageButton... plus) {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(getButtonSize(),
                    getButtonSize());
            LinearLayout layout = new LinearLayout(getContext());
            layout.setOrientation(LinearLayout.HORIZONTAL);
            for (ImageButton imageButton : plus) {
                layout.addView(imageButton, params);
            }
            return layout;
        }


        private int getPopupWidth() {
            return popUp.getButtonSize() * 2;
        }

        private int getButtonSize() {
            return convertToPX(BUTTON_SIZE_DP);
        }

        private int getButtonPadding() {
            return convertToPX(4);
        }

        private ImageButton getButton(@IdRes int id, @DrawableRes int drawable) {
            ImageButton button = new ImageButton(getContext());
            setButtonBackground(button);
            button.setImageDrawable(getDrawable(drawable));
            button.setId(id);
            button.setPadding(getButtonPadding(), getButtonPadding(), getButtonPadding(), getButtonPadding());
            button.setOnClickListener(StepPopupEditText.this);
            return button;
        }


        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        private Drawable getDrawable(@DrawableRes int drawable) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                return getResources().getDrawable(drawable, null);
            else
                //noinspection deprecation
                return getResources().getDrawable(drawable);
        }


        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        private void setButtonBackground(ImageButton button) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                button.setBackground(null);
        }

    }
}
