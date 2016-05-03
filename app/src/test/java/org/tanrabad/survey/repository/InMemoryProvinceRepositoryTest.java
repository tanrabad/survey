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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.tanrabad.survey.entity.lookup.Province;

import static org.junit.Assert.*;

public class InMemoryProvinceRepositoryTest {

    private InMemoryProvinceRepository provinceRepository = InMemoryProvinceRepository.getInstance();

    @Before
    public void setUp() throws Exception {
        provinceRepository.save(stubProvince());
    }

    protected Province stubProvince() {
        Province province = new Province();
        province.setCode("10");
        province.setName("กรุงเทพมหานคร");
        return province;
    }

    @After
    public void tearDown() {
        provinceRepository.delete(stubProvince());
    }

    @Test(expected = InMemoryProvinceRepository.ProvinceRepositoryException.class)
    public void testSaveExistProvinceMustThrowException() throws Exception {
        provinceRepository.save(stubProvince());
    }

    @Test
    public void testEveryCallGetInstanceMustGotSameInstance() throws Exception {
        assertEquals(provinceRepository, InMemoryProvinceRepository.getInstance());
    }

    @Test
    public void testFindByCode() throws Exception {
        assertEquals(stubProvince(), provinceRepository.findByCode(stubProvince().getCode()));
        assertEquals(null, provinceRepository.findByCode("21"));
    }

    @Test
    public void testUpdate() throws Exception {
        Province provinceNewName = stubProvince();
        provinceNewName.setName("ปทุมธานี");
        provinceRepository.update(provinceNewName);
        assertEquals(provinceNewName, provinceRepository.findByCode(stubProvince().getCode()));
    }

    @Test
    public void testDelete() throws Exception {
        provinceRepository.save(stubOtherProvince());
        assertTrue(provinceRepository.delete(stubOtherProvince()));
    }

    protected Province stubOtherProvince() {
        Province province = new Province();
        province.setCode("40");
        return province;
    }

    @Test(expected = InMemoryProvinceRepository.ProvinceRepositoryException.class)
    public void testDeleteNotExistProvince() throws Exception {
        assertFalse(provinceRepository.delete(stubOtherProvince()));
    }

    @Test(expected = InMemoryProvinceRepository.ProvinceRepositoryException.class)
    public void testUpdateNotExistProvince() throws Exception {
        provinceRepository.update(stubOtherProvince());
    }
}
