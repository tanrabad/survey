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

package th.or.nectec.tanrabad.survey.presenter.view;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricGradleTestRunner;
import th.or.nectec.tanrabad.survey.RobolectricTestBase;
import th.or.nectec.tanrabad.survey.utils.Torch;

import static android.view.View.GONE;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
public class TorchButtonTest extends RobolectricTestBase {

    private Torch mockedTorch = Mockito.mock(Torch.class);
    private TorchButton torchButton = new TorchButton(getContext(), null, 0, mockedTorch);

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