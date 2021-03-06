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

import android.content.Context;
import android.graphics.Rect;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import org.tanrabad.survey.R;
import org.tanrabad.survey.utils.EditTextStepper;
import org.tanrabad.survey.utils.android.BackgroundSetter;
import org.tanrabad.survey.utils.android.DpCalculator;
import org.tanrabad.survey.utils.android.ResourceUtils;

public class StepPopupEditText extends AppCompatEditText {
    protected EditTextStepper editTextStepper;
    private StepperPopup popUp;

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
        popUp = new EditTextPopup(this);

    }

    private void setupEditText() {
        setInputType(InputType.TYPE_CLASS_NUMBER);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                popUp.display();
            }
        });
    }

    @Override
    public Parcelable onSaveInstanceState() {
        popUp.hide();
        return super.onSaveInstanceState();
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (!isShown()) return; //to prevent force close on orientation change

        if (focused) {
            popUp.display();
        } else {
            popUp.hide();
        }
    }

    interface StepperPopup {
        void display();

        void hide();

    }

    private static class EditTextPopup extends PopupWindow implements StepperPopup, View.OnClickListener {

        private static final int BUTTON_HEIGHT = 64;
        private static final int BUTTON_WIDTH = 80;
        private static final int BUTTON_PADDING = 4;
        private static final int POPUP_PADDING = 2;
        private final Context context;
        private final StepPopupEditText editText;

        public EditTextPopup(StepPopupEditText editText) {
            super(editText.getContext());
            this.context = editText.getContext();
            this.editText = editText;
            init();
        }

        private void init() {
            setOutsideTouchable(true);
            setBackgroundDrawable(ResourceUtils.from(context).getDrawable(R.drawable.bg_popup));

            ImageButton minus = getButton(R.id.minus, R.mipmap.ic_minus_1);
            ImageButton plus = getButton(R.id.plus, R.mipmap.ic_plus_1);
            BackgroundSetter.set(minus, R.drawable.bg_popup_button_left);
            BackgroundSetter.set(plus, R.drawable.bg_popup_button_right);
            setContentView(getLayout(minus, plus));
        }

        private ImageButton getButton(@IdRes int id, @DrawableRes int drawable) {
            ImageButton button = new ImageButton(context);
            button.setImageDrawable(ResourceUtils.from(context).getDrawable(drawable));
            button.setId(id);
            button.setPadding(getButtonPadding(), getButtonPadding(), getButtonPadding(), getButtonPadding());
            button.setOnClickListener(this);

            return button;
        }

        private LinearLayout getLayout(ImageButton... imageButtons) {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(getButtonWidth(), getButtonHeight());
            LinearLayout layout = new LinearLayout(context);
            layout.setPadding(getPopupPadding(), getPopupPadding(), getPopupPadding(), getPopupPadding());
            layout.setOrientation(LinearLayout.HORIZONTAL);
            for (ImageButton imageButton : imageButtons) {
                layout.addView(imageButton, params);
            }
            return layout;
        }

        private int getButtonPadding() {
            return DpCalculator.from(context).toPx(BUTTON_PADDING);
        }

        private int getButtonWidth() {
            return DpCalculator.from(context).toPx(BUTTON_WIDTH);
        }

        private int getButtonHeight() {
            return DpCalculator.from(context).toPx(BUTTON_HEIGHT);
        }

        private int getPopupPadding() {
            return DpCalculator.from(context).toPx(POPUP_PADDING);
        }

        @Override
        public void display() {
            if (!isShowing()) {
                showAsDropDown(editText, getXoffset(), getYoffset());
                update(getPopupWidth(), getPopupHeight());
            }
        }

        @Override
        public void hide() {
            if (isShowing())
                dismiss();
        }

        private int getXoffset() {
            return 0 - getPopupWidth() / 2 + editText.getWidth() / 2;
        }

        private int getYoffset() {
            return DpCalculator.from(context).toPx(8);
        }

        private int getPopupWidth() {
            return getButtonWidth() * 2 + getPopupPadding() * 2;
        }

        private int getPopupHeight() {
            return getButtonHeight() + getPopupPadding() * 2;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.plus:
                    editText.editTextStepper.step(1);
                    break;
                case R.id.minus:
                    editText.editTextStepper.step(-1);
                    break;
            }
        }

    }

}
