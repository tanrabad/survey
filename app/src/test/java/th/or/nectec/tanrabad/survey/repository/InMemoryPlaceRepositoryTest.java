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

package th.or.nectec.tanrabad.survey.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import th.or.nectec.tanrabad.domain.place.PlaceRepositoryException;
import th.or.nectec.tanrabad.entity.Place;

import static org.junit.Assert.*;

public class InMemoryPlaceRepositoryTest {

    private InMemoryPlaceRepository placeRepository = InMemoryPlaceRepository.getInstance();
    private Place hospital = Place.withName("โรงพยาบาลธรรมศาสตร์เฉลิมพระเกียรติ");
    private Place school = Place.withName("โรงเรียนบางปะอิน \"ราชานุเคราะห์ ๑\"");

    @Before
    public void setUp() throws Exception {
        placeRepository.save(hospital);
        placeRepository.save(school);
    }

    @After
    public void tearDown() {
        placeRepository.delete(hospital);
        placeRepository.delete(school);
    }

    @Test(expected = PlaceRepositoryException.class)
    public void testSaveExistPlaceMustThrowException() throws Exception {
        placeRepository.save(hospital);
    }

    @Test
    public void testEveryCallGetInstanceMustGotSameInstance() throws Exception {
        assertEquals(placeRepository, InMemoryPlaceRepository.getInstance());
    }

    @Test
    public void testFindPlaceByUuid() throws Exception {
        assertEquals(hospital, placeRepository.findByUuid(hospital.getId()));
        assertEquals(school, placeRepository.findByUuid(school.getId()));
        assertEquals(null, placeRepository.findByUuid(UUID.randomUUID()));
    }

    @Test
    public void testUpdate() throws Exception {
        Place racha1NewName = new Place(school.getId(), "โรงเรียนบางปะอิน");
        placeRepository.update(racha1NewName);
        assertEquals(racha1NewName, placeRepository.findByUuid(school.getId()));
    }

    @Test
    public void testDelete() throws Exception {
        assertTrue(placeRepository.delete(school));
    }

    @Test
    public void testDeleteNotExistPlace() throws Exception {
        assertFalse(placeRepository.delete(new Place(UUID.randomUUID(), "New Place")));
    }

    @Test(expected = PlaceRepositoryException.class)
    public void testUpdateNotExistPlaceMustThrowException() throws Exception {
        placeRepository.update(new Place(UUID.randomUUID(), "New Place"));
    }

    @Test
    public void testFindPlaceByName() throws Exception {
        List<Place> searchPlace = placeRepository.findByName("๑");
        assertEquals(1, searchPlace.size());
        assertTrue(searchPlace.contains(school));

        List<Place> emptyPlaceList = placeRepository.findByName("C");
        assertTrue(emptyPlaceList == null);
    }
}
