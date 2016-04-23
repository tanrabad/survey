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

package org.tanrabad.survey.validator;

import org.junit.Test;
import org.mockito.Mockito;
import th.or.nectec.tanrabad.domain.place.PlaceRepository;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.entity.field.Location;
import th.or.nectec.tanrabad.entity.lookup.PlaceSubType;
import th.or.nectec.tanrabad.entity.lookup.PlaceType;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class UpdatePlaceValidatorTest {

    @Test(expected = EmptyNameException.class)
    public void testEmptyName() throws Exception {
        UpdatePlaceValidator savePlaceValidator = new UpdatePlaceValidator();
        Place place = new Place(UUID.randomUUID(), null);

        savePlaceValidator.validate(place);
    }

    @Test(expected = NullAddressException.class)
    public void testAddressNull() throws Exception {
        UpdatePlaceValidator savePlaceValidator = new UpdatePlaceValidator();
        Place place = new Place(UUID.randomUUID(), "5555");

        savePlaceValidator.validate(place);
    }

    @Test(expected = ValidatorException.class)
    public void testSamePlaceNameAndPlaceTypeAndPlaceAddressException() throws Exception {
        PlaceRepository placeRepository = Mockito.mock(PlaceRepository.class);
        Mockito.when(placeRepository.find()).thenReturn(stubPlacesList());

        UpdatePlaceValidator savePlaceValidator = new UpdatePlaceValidator();
        savePlaceValidator.setPlaceRepository(placeRepository);
        savePlaceValidator.validate(stubPlace());
    }

    @Test
    public void testPlaceNameNotSame() throws Exception {
        PlaceRepository placeRepository = Mockito.mock(PlaceRepository.class);
        Mockito.when(placeRepository.find()).thenReturn(stubPlacesList());
        Place place = new Place(UUID.randomUUID(), "ทดสอบ123");
        place.setSubdistrictCode("130202");
        place.setLocation(stubLocation());
        UpdatePlaceValidator savePlaceValidator = new UpdatePlaceValidator();
        savePlaceValidator.setPlaceRepository(placeRepository);

        assertEquals(true, savePlaceValidator.validate(place));
    }

    private ArrayList<Place> stubPlacesList() {
        ArrayList<Place> places = new ArrayList<>();
        places.add(stubPlace());
        places.add(stubPlaceWorship());
        return places;
    }

    private Location stubLocation() {
        return new Location(1, 1);
    }

    private Place stubPlace() {
        Place testPlace = Place.withName("ทดสอบ");
        testPlace.setSubdistrictCode("130202");
        testPlace.setLocation(stubLocation());
        testPlace.setType(PlaceType.HOSPITAL);
        return testPlace;
    }

    private Place stubPlaceWorship() {
        Place testPlace = Place.withName("วัดสาม");
        testPlace.setSubdistrictCode("130202");
        testPlace.setLocation(stubLocation());
        testPlace.setType(PlaceType.WORSHIP);
        testPlace.setSubType(PlaceSubType.TEMPLE);
        return testPlace;
    }

    @Test
    public void testSameNameAndTypeDifferentAddressCanSave() throws Exception {
        PlaceRepository placeRepository = Mockito.mock(PlaceRepository.class);
        Mockito.when(placeRepository.find()).thenReturn(stubPlacesList());
        Place place = new Place(UUID.randomUUID(), "ทดสอบ123");
        place.setSubdistrictCode("130203");
        place.setLocation(stubLocation());
        UpdatePlaceValidator savePlaceValidator = new UpdatePlaceValidator();
        savePlaceValidator.setPlaceRepository(placeRepository);

        assertEquals(true, savePlaceValidator.validate(place));
    }

    @Test
    public void testSameNameAndAddressDifferentTypeCanSave() throws Exception {
        PlaceRepository placeRepository = Mockito.mock(PlaceRepository.class);
        Mockito.when(placeRepository.find()).thenReturn(stubPlacesList());
        Place place = stubPlace();
        place.setType(PlaceType.FACTORY);
        UpdatePlaceValidator savePlaceValidator = new UpdatePlaceValidator();
        savePlaceValidator.setPlaceRepository(placeRepository);

        assertEquals(true, savePlaceValidator.validate(place));
    }

    @Test
    public void testDifferenceNameAndTypeAndSameAddressCanSave() throws Exception {
        PlaceRepository placeRepository = Mockito.mock(PlaceRepository.class);
        Mockito.when(placeRepository.find()).thenReturn(stubPlacesList());
        Place place = stubPlace();
        place.setName("ทดสอบ789");
        place.setType(PlaceType.SCHOOL);
        UpdatePlaceValidator savePlaceValidator = new UpdatePlaceValidator();
        savePlaceValidator.setPlaceRepository(placeRepository);

        assertEquals(true, savePlaceValidator.validate(place));
    }

    @Test
    public void testSameTypeAndAddressAndDifferenceNameCanSave() throws Exception {
        PlaceRepository placeRepository = Mockito.mock(PlaceRepository.class);
        Mockito.when(placeRepository.find()).thenReturn(stubPlacesList());
        Place place = stubPlace();
        place.setName("ทดสอบ234");
        UpdatePlaceValidator savePlaceValidator = new UpdatePlaceValidator();
        savePlaceValidator.setPlaceRepository(placeRepository);

        assertEquals(true, savePlaceValidator.validate(place));
    }

    @Test
    public void testSameNameDifferentAddressAndTypeCanSave() throws Exception {
        PlaceRepository placeRepository = Mockito.mock(PlaceRepository.class);
        Mockito.when(placeRepository.find()).thenReturn(stubPlacesList());
        Place place = stubPlace();
        place.setSubdistrictCode("130203");
        place.setType(PlaceType.SCHOOL);
        UpdatePlaceValidator savePlaceValidator = new UpdatePlaceValidator();
        savePlaceValidator.setPlaceRepository(placeRepository);

        assertEquals(true, savePlaceValidator.validate(place));
    }

    @Test
    public void testSameNameAndTypeAndDifferenceAddressCanSave() throws Exception {
        PlaceRepository placeRepository = Mockito.mock(PlaceRepository.class);
        Mockito.when(placeRepository.find()).thenReturn(stubPlacesList());
        Place place = stubPlace();
        place.setSubdistrictCode("130203");
        UpdatePlaceValidator savePlaceValidator = new UpdatePlaceValidator();
        savePlaceValidator.setPlaceRepository(placeRepository);

        assertEquals(true, savePlaceValidator.validate(place));
    }

    @Test
    public void testSameAndDifferenceNameAndAddressCanSave() throws Exception {
        PlaceRepository placeRepository = Mockito.mock(PlaceRepository.class);
        Mockito.when(placeRepository.find()).thenReturn(stubPlacesList());
        Place place = stubPlace();
        place.setName("ทดสอบ345");
        place.setSubdistrictCode("130203");
        UpdatePlaceValidator savePlaceValidator = new UpdatePlaceValidator();
        savePlaceValidator.setPlaceRepository(placeRepository);

        assertEquals(true, savePlaceValidator.validate(place));
    }

    @Test
    public void testSameNameAndAddressAndDifferenceSubTypeOfWorshipCanSave() throws Exception {
        PlaceRepository placeRepository = Mockito.mock(PlaceRepository.class);
        Mockito.when(placeRepository.find()).thenReturn(stubPlacesList());
        Place place = stubPlaceWorship();
        place.setSubType(PlaceSubType.CHURCH);
        SavePlaceValidator savePlaceValidator = new SavePlaceValidator();
        savePlaceValidator.setPlaceRepository(placeRepository);

        assertEquals(true, savePlaceValidator.validate(place));
    }
}
