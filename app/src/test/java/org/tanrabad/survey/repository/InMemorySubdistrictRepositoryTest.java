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

package org.tanrabad.survey.repository;

import android.support.annotation.NonNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.tanrabad.survey.entity.lookup.Subdistrict;

import java.util.List;

import static org.junit.Assert.*;

public class InMemorySubdistrictRepositoryTest {

    private InMemorySubdistrictRepository subdistrictRepository = InMemorySubdistrictRepository.getInstance();


    @Before
    public void setUp() throws Exception {
        subdistrictRepository.save(stubSubdistrict());
    }

    protected Subdistrict stubSubdistrict() {
        Subdistrict subdistrict = new Subdistrict();
        subdistrict.setCode("100101");
        subdistrict.setName("พระบรมมหาราชวัง");
        subdistrict.setDistrictCode("1001");
        return subdistrict;
    }

    @After
    public void tearDown() {
        subdistrictRepository.delete(stubSubdistrict());
    }

    @Test(expected = InMemorySubdistrictRepository.SubdistrictRepositoryException.class)
    public void testSaveExistSubdistrictMustThrowException() throws Exception {
        subdistrictRepository.save(stubSubdistrict());
    }

    @Test
    public void testEveryCallGetInstanceMustGotSameInstance() throws Exception {
        assertEquals(subdistrictRepository, InMemorySubdistrictRepository.getInstance());
    }

    @Test
    public void testFindBySubdistrictCode() throws Exception {
        assertEquals(stubSubdistrict(), subdistrictRepository.findByCode(stubSubdistrict().getCode()));
        assertEquals(null, subdistrictRepository.findByCode("777000"));
    }

    @Test
    public void testUpdate() throws Exception {
        Subdistrict subdistrictNewName = stubSubdistrict();
        subdistrictNewName.setName("บางเขน");
        subdistrictRepository.update(subdistrictNewName);
        assertEquals(subdistrictNewName, subdistrictRepository.findByCode(stubSubdistrict().getCode()));
    }

    @Test
    public void testDelete() throws Exception {
        subdistrictRepository.save(stubOtherSubdistrict());
        assertTrue(subdistrictRepository.delete(stubOtherSubdistrict()));
    }

    @NonNull
    protected Subdistrict stubOtherSubdistrict() {
        Subdistrict subdistrict = new Subdistrict();
        subdistrict.setCode("401010");
        return subdistrict;
    }

    @Test(expected = InMemorySubdistrictRepository.SubdistrictRepositoryException.class)
    public void testDeleteNotExistSubdistrict() throws Exception {
        assertFalse(subdistrictRepository.delete(stubOtherSubdistrict()));
    }

    @Test(expected = InMemorySubdistrictRepository.SubdistrictRepositoryException.class)
    public void testUpdateNotExistSubdistrict() throws Exception {
        subdistrictRepository.update(stubOtherSubdistrict());
    }

    @Test
    public void testFindByDistrictCode() throws Exception {
        List<Subdistrict> subdistricts = subdistrictRepository.findByDistrictCode("1001");
        assertEquals(1, subdistricts.size());
        assertTrue(subdistricts.contains(stubSubdistrict()));

        List<Subdistrict> emptySubdistrict = subdistrictRepository.findByDistrictCode("1000");
        assertTrue(emptySubdistrict.isEmpty());
    }
}
