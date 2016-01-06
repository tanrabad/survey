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

package th.or.nectec.tanrabad.survey.repository;

import org.junit.Before;
import org.junit.Test;
import th.or.nectec.tanrabad.domain.building.BuildingRepositoryException;
import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.entity.Place;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class InMemoryBuildingRepositoryTest {

    private final Building towerA = new Building(UUID.randomUUID(), "Tower A");
    private final Building towerB = new Building(UUID.randomUUID(), "Tower B");
    private InMemoryBuildingRepository buildingRepo = InMemoryBuildingRepository.getInstance();
    private Place NationalPark = Place.withName("Bangkok Tower Park");

    @Before
    public void setUp() throws Exception {
        towerA.setPlace(NationalPark);
        towerB.setPlace(NationalPark);
        buildingRepo.save(towerA);
        buildingRepo.save(towerB);
    }

    @Test(expected = BuildingRepositoryException.class)
    public void testSaveExistBuildingMustThrowException() throws Exception {
        buildingRepo.save(towerA);
    }

    @Test
    public void testEveryCallGetInstanceMustGotSameInstance() throws Exception {
        assertEquals(buildingRepo, InMemoryBuildingRepository.getInstance());
    }

    @Test
    public void testFindBuildingByUUID() throws Exception {
        assertEquals(towerA, buildingRepo.findBuildingByUUID(towerA.getId()));
        assertEquals(towerB, buildingRepo.findBuildingByUUID(towerB.getId()));
        assertEquals(null, buildingRepo.findBuildingByUUID(UUID.randomUUID()));
    }

    @Test
    public void testUpdate() throws Exception {
        Building towerANewName = new Building(towerA.getId(), "Tower I");
        towerANewName.setPlace(NationalPark);

        buildingRepo.update(towerANewName);

        assertEquals(towerANewName, buildingRepo.findBuildingByUUID(towerA.getId()));
    }

    @Test(expected = BuildingRepositoryException.class)
    public void testUpdateNotExistBuildingMustThrowException() throws Exception {
        buildingRepo.update(new Building(UUID.randomUUID(), "New Building"));
    }

    @Test
    public void testFindBuildingInPlace() throws Exception {
        List<Building> buildingInPlace = buildingRepo.findBuildingInPlace(NationalPark.getId());

        assertTrue(buildingInPlace.size() == 2);
        assertTrue(buildingInPlace.contains(towerA));
        assertTrue(buildingInPlace.contains(towerB));
    }

    @Test
    public void testFindBuildingInPlaceByName() throws Exception {
        List<Building> buildingInPlace = buildingRepo.searchBuildingInPlaceByName(NationalPark.getId(), "A");

        assertTrue(buildingInPlace.size() == 1);
        assertTrue(buildingInPlace.contains(towerA));

        List<Building> emptyBuildingList = buildingRepo.searchBuildingInPlaceByName(NationalPark.getId(), "C");

        assertTrue(emptyBuildingList == null);
    }
}