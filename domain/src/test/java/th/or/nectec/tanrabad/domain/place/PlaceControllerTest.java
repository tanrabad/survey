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

package th.or.nectec.tanrabad.domain.place;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.UUID;

import th.or.nectec.tanrabad.entity.Place;

public class PlaceControllerTest {
    public final String placeName = "New York";

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    Place place = Place.withName(placeName);
    private PlaceRepository placeRepository;
    private PlacePresenter placePresenter;
    private UUID placeUUID;
    private PlaceController placeController;

    @Before
    public void setUp() throws Exception {
        placeUUID = UUID.nameUUIDFromBytes("3zyx".getBytes());

        placeRepository = context.mock(PlaceRepository.class);
        placePresenter = context.mock(PlacePresenter.class);
    }

    @Test
    public void testFoundPlace() throws Exception {
        context.checking(new Expectations() {
            {
                allowing(placeRepository).findPlaceByUUID(placeUUID);
                will(returnValue(place));
                oneOf(placePresenter).displayPlace(place);
            }
        });
        PlaceController placeController = new PlaceController(placeRepository, placePresenter);
        placeController.showPlace(placeUUID);
    }

    @Test
    public void testNotFoundPlace() throws Exception {
        context.checking(new Expectations() {
            {
                allowing(placeRepository).findPlaceByUUID(placeUUID);
                will(returnValue(null));
                oneOf(placePresenter).alertPlaceNotFound();
            }
        });
        PlaceController placeController = new PlaceController(placeRepository, placePresenter);
        placeController.showPlace(placeUUID);
    }

}
