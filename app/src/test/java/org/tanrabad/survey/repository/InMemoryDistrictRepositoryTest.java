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
import org.tanrabad.survey.entity.lookup.District;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InMemoryDistrictRepositoryTest {

    private InMemoryDistrictRepository districtRepository = InMemoryDistrictRepository.getInstance();

    @Before
    public void setUp() throws Exception {
        districtRepository.save(stubDistrict());
    }

    protected District stubDistrict() {
        District district = new District();
        district.setCode("1001");
        district.setProvinceCode("10");
        district.setName("พระนคร");
        return district;
    }

    @After
    public void tearDown() {
        districtRepository.delete(stubDistrict());
    }

    @Test
    public void testEveryCallGetInstanceMustGotSameInstance() throws Exception {
        assertEquals(districtRepository, InMemoryDistrictRepository.getInstance());
    }

    @Test
    public void testFindByDistrictCode() throws Exception {
        assertEquals(stubDistrict(), districtRepository.findByCode(stubDistrict().getCode()));
        assertEquals(null, districtRepository.findByCode("2170"));
    }

    @Test
    public void testUpdate() throws Exception {
        District districtNewName = stubDistrict();
        districtNewName.setName("บางเขน");
        districtRepository.update(districtNewName);
        assertEquals(districtNewName, districtRepository.findByCode(stubDistrict().getCode()));
    }

    @Test
    public void testDelete() throws Exception {
        districtRepository.save(stubOtherDistrict());
        assertTrue(districtRepository.delete(stubOtherDistrict()));
    }

    protected District stubOtherDistrict() {
        District district = new District();
        district.setCode("4010");
        return district;
    }

    @Test(expected = InMemoryDistrictRepository.DistrictRepositoryException.class)
    public void testDeleteNotExistDistrict() throws Exception {
        assertFalse(districtRepository.delete(stubOtherDistrict()));
    }

    @Test
    public void testFindByProvinceCode() throws Exception {
        List<District> districtByProvince = districtRepository.findByProvinceCode("10");
        assertEquals(1, districtByProvince.size());
        assertTrue(districtByProvince.contains(stubDistrict()));

        List<District> emptyDistrict = districtRepository.findByProvinceCode("20");
        assertTrue(emptyDistrict.isEmpty());
    }
}
