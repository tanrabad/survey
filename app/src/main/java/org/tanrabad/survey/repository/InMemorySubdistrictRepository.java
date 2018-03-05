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


import org.tanrabad.survey.domain.address.SubdistrictRepository;
import org.tanrabad.survey.entity.lookup.Subdistrict;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemorySubdistrictRepository implements SubdistrictRepository {

    private static InMemorySubdistrictRepository instance;

    private Map<String, Subdistrict> subdistrictMap;

    private InMemorySubdistrictRepository() {
        subdistrictMap = new ConcurrentHashMap<>();
    }

    public static InMemorySubdistrictRepository getInstance() {
        if (instance == null)
            instance = new InMemorySubdistrictRepository();
        return instance;
    }

    @Override
    public List<Subdistrict> find() {
        List<Subdistrict> subDistricts = new ArrayList<>();
        subDistricts.addAll(subdistrictMap.values());
        return subDistricts;
    }

    @Override
    public List<Subdistrict> findByDistrictCode(String districtCode) {
        ArrayList<Subdistrict> subdistricts = new ArrayList<>();
        for (Subdistrict subdistrict : subdistrictMap.values()) {
            if (subdistrict.getDistrictCode().equals(districtCode))
                subdistricts.add(subdistrict);
        }
        return subdistricts;
    }

    @Override
    public Subdistrict findByCode(String subdistrictCode) {
        return subdistrictMap.get(subdistrictCode);
    }

    public class SubdistrictRepositoryException extends RuntimeException {
    }

    @Override
    public boolean save(Subdistrict subdistrict) {
        subdistrictMap.put(subdistrict.getCode(), subdistrict);
        return true;
    }

    @Override
    public boolean update(Subdistrict subdistrict) {
        subdistrictMap.put(subdistrict.getCode(), subdistrict);
        return true;
    }

    @Override
    public boolean delete(Subdistrict subdistrict) {
        if (!subdistrictMap.containsKey(subdistrict.getCode())) {
            throw new SubdistrictRepositoryException();
        } else {
            subdistrictMap.remove(subdistrict.getCode());
        }
        return true;
    }

    @Override
    public void updateOrInsert(List<Subdistrict> subdistricts) {
        for (Subdistrict subdistrict : subdistricts) {
            try {
                update(subdistrict);
            } catch (SubdistrictRepositoryException pre) {
                save(subdistrict);
            }
        }
    }
}
