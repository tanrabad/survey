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

package th.or.nectec.tanrabad.survey.presenter.view;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.TanrabadApp;
import th.or.nectec.tanrabad.survey.utils.CameraFlashLight;
import th.or.nectec.tanrabad.survey.utils.Torch;
import th.or.nectec.tanrabad.survey.utils.time.JodaCurrentTime;

public class TorchButton extends ImageButton {

    private final Torch torch;
    private Handler uiThread;
    long startTime = 0;

    public TorchButton(Context context) {
        this(context, null);
    }

    public TorchButton(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.imageButtonStyle);
    }

    public TorchButton(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, CameraFlashLight.getInstance(context));
    }

    public TorchButton(Context context, AttributeSet attrs, int defStyleAttr, Torch torch) {
        super(context, attrs, defStyleAttr);
        this.torch = torch;
        setupView();


    }

    private void setupView() {
        int size = getContext().getResources().getDimensionPixelOffset(R.dimen.list_icon_size);
        int padding = getContext().getResources().getDimensionPixelOffset(R.dimen.image_button_padding);
        setMinimumHeight(size);
        setMinimumWidth(size);
        setPadding(padding, padding, padding, padding);
        setImageResource(R.drawable.torch_off);
        setBackgroundResource(R.drawable.bg_button_flat_oval);
        setContentDescription(getContext().getString(R.string.torch));

        if (!isInEditMode()) {

            hideIfNotAvailable();
        }
    }

    private void hideIfNotAvailable() {
        uiThread = new Handler(Looper.getMainLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!torch.isAvailable()) {
                    uiThread.post(new Runnable() {
                        @Override
                        public void run() {
                            setVisibility(View.GONE);
                            setEnabled(false);
                        }
                    });
                }
            }
        }).run();
    }

    @Override
    public boolean performClick() {
        toggleTorchLight();
        return true;
    }

    private void toggleTorchLight() {
        setEnabled(false);
        if (torch.isTurningOn()) {
            turnOff();
        } else {
            turnOn();
        }
        setEnabled(true);
    }

    private void turnOff() {
        TanrabadApp.action().useTorch(calculateUsageDuration());
        torch.turnOff();
        setImageResource(R.drawable.torch_off);
    }

    private void turnOn() {
        startTime = new JodaCurrentTime().getInMills();
        torch.turnOn();
        setImageResource(R.drawable.torch_on);
    }

    private int calculateUsageDuration() {
        return (int) ((new JodaCurrentTime().getInMills() - startTime) / 1000);
    }

    public void safeTurnOff() {
        if (torch.isAvailable() && torch.isTurningOn())
            turnOff();
    }
}
