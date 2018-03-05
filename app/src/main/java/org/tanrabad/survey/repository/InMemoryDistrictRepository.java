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


import org.tanrabad.survey.domain.address.DistrictRepository;
import org.tanrabad.survey.entity.lookup.District;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryDistrictRepository implements DistrictRepository {

    private static InMemoryDistrictRepository instance;

    private Map<String, District> districtMap;

    private InMemoryDistrictRepository() {
        districtMap = new ConcurrentHashMap<>();
    }

    public static InMemoryDistrictRepository getInstance() {
        if (instance == null)
            instance = new InMemoryDistrictRepository();
        return instance;
    }

    @Override
    public List<District> find() {
        ArrayList<District> districts = new ArrayList<>();
        districts.addAll(districtMap.values());
        return districts;
    }

    @Override
    public List<District> findByProvinceCode(String provinceCode) {
        ArrayList<District> districts = new ArrayList<>();
        for (District district : districtMap.values()) {
            if (district.getProvinceCode().equals(provinceCode))
                districts.add(district);
        }
        return districts;
    }

    @Override
    public District findByCode(String districtCode) {
        return districtMap.get(districtCode);
    }

    public class DistrictRepositoryException extends RuntimeException {
    }

    @Override
    public boolean save(District district) {
        districtMap.put(district.getCode(), district);
        return true;
    }

    @Override
    public boolean update(District district) {
        districtMap.put(district.getCode(), district);
        return true;
    }

    @Override
    public boolean delete(District district) {
        if (!districtMap.containsKey(district.getCode())) {
            throw new DistrictRepositoryException();
        } else {
            districtMap.remove(district.getCode());
        }
        return true;
    }

    @Override
    public void updateOrInsert(List<District> districts) {
        for (District district : districts) {
            try {
                update(district);
            } catch (DistrictRepositoryException pre) {
                save(district);
            }
        }
    }
}
