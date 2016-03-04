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

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import th.or.nectec.tanrabad.survey.utils.Torch;

import static android.view.View.GONE;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class TorchButtonTest {

    private Torch mockedTorch = Mockito.mock(Torch.class);
    private TorchButton torchButton = new TorchButton(InstrumentationRegistry.getTargetContext(),
            null, android.R.attr.imageButtonStyle, mockedTorch);

    @Test
    public void testGoneWhenTorchIsNotAvailable() throws Exception {
        when(mockedTorch.isAvailable()).thenReturn(false);

        assertEquals(GONE, torchButton.getVisibility());
    }

    @Test
    public void testClickThenTorchMustTurnOn() throws Exception {
        when(mockedTorch.isTurningOn()).thenReturn(false);

        torchButton.performClick();

        verify(mockedTorch).turnOn();
    }

    @Test
    public void testClickWhenTorchIsTurningOnThenTorchShouldTurnOff() throws Exception {
        when(mockedTorch.isTurningOn()).thenReturn(true);

        torchButton.performClick();

        verify(mockedTorch).turnOff();
    }

}
