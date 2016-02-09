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
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.entity.field.Location;

import java.util.UUID;

public class PlaceSaverTest {
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    private UUID placeUUID;
    private String placeName;
    private int placeType;
    private Location placeLocation;

    private PlaceValidator placeValidator;
    private PlaceSavePresenter placeSavePresenter;
    private PlaceRepository placeRepository;
    private Place place;

    @Before
    public void setUp() {
        placeSavePresenter = context.mock(PlaceSavePresenter.class);
        placeValidator = context.mock(PlaceValidator.class);
        placeRepository = context.mock(PlaceRepository.class);

        placeUUID = UUID.nameUUIDFromBytes("1stu".getBytes());
        placeName = "victoria";
        placeType = Place.TYPE_FACTORY;
        placeLocation = new Location(51.500152, -0.126236);

        place = new Place(placeUUID, placeName);
        place.setType(placeType);
        place.setLocation(placeLocation);
    }

    @Test
    public void testSavePlace() throws Exception {

        context.checking(new Expectations() {
            {
                allowing(PlaceSaverTest.this.placeValidator).setPlaceRepository(placeRepository);
                allowing(PlaceSaverTest.this.placeValidator).validate(with(place));
                will(returnValue(true));
                oneOf(PlaceSaverTest.this.placeRepository).save(with(place));
                will(returnValue(true));
                oneOf(PlaceSaverTest.this.placeSavePresenter).displaySaveSuccess();
            }
        });
        PlaceSaver placeSaver = new PlaceSaver(placeRepository, placeValidator, placeSavePresenter);
        placeSaver.save(place);
    }

    @Test
    public void testUpdatePlace() throws Exception {

        context.checking(new Expectations() {
            {
                allowing(PlaceSaverTest.this.placeValidator).setPlaceRepository(placeRepository);
                allowing(PlaceSaverTest.this.placeValidator).validate(with(place));
                will(returnValue(true));
                oneOf(PlaceSaverTest.this.placeRepository).update(with(place));
                will(returnValue(true));
                oneOf(PlaceSaverTest.this.placeSavePresenter).displayUpdateSuccess();
            }
        });
        PlaceSaver placeSaver = new PlaceSaver(placeRepository, placeValidator, placeSavePresenter);
        placeSaver.update(place);
    }

    @Test
    public void testSaveFail() throws Exception {

        context.checking(new Expectations() {
            {
                allowing(PlaceSaverTest.this.placeValidator).setPlaceRepository(placeRepository);
                allowing(placeValidator).validate(place);
                will(returnValue(false));
                never(placeRepository);
                oneOf(placeSavePresenter).displaySaveFail();
            }
        });
        PlaceSaver placeSaver = new PlaceSaver(placeRepository, placeValidator, placeSavePresenter);
        placeSaver.save(place);
    }

    @Test
    public void testUpdateFail() throws Exception {

        context.checking(new Expectations() {
            {
                allowing(PlaceSaverTest.this.placeValidator).setPlaceRepository(placeRepository);
                allowing(placeValidator).validate(place);
                will(returnValue(false));
                never(placeRepository);
                oneOf(placeSavePresenter).displayUpdateFail();
            }
        });
        PlaceSaver placeSaver = new PlaceSaver(placeRepository, placeValidator, placeSavePresenter);
        placeSaver.update(place);
    }
}