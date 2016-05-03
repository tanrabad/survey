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


import org.tanrabad.survey.domain.address.ProvinceRepository;
import org.tanrabad.survey.entity.lookup.Province;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryProvinceRepository implements ProvinceRepository {

    private static InMemoryProvinceRepository instance;

    private Map<String, Province> provinceMap;

    private InMemoryProvinceRepository() {
        provinceMap = new ConcurrentHashMap<>();
    }

    public static InMemoryProvinceRepository getInstance() {
        if (instance == null)
            instance = new InMemoryProvinceRepository();
        return instance;
    }

    @Override
    public List<Province> find() {
        return new ArrayList<>(provinceMap.values());
    }

    @Override
    public Province findByCode(String provinceCode) {
        return provinceMap.get(provinceCode);
    }

    public class ProvinceRepositoryException extends RuntimeException {
    }

    @Override
    public boolean save(Province province) {
        if (provinceMap.containsKey(province.getCode())) {
            throw new ProvinceRepositoryException();
        } else {
            provinceMap.put(province.getCode(), province);
        }
        return true;
    }

    @Override
    public boolean update(Province province) {
        if (!provinceMap.containsKey(province.getCode())) {
            throw new ProvinceRepositoryException();
        } else {
            provinceMap.put(province.getCode(), province);
        }
        return true;
    }

    @Override
    public boolean delete(Province province) {
        if (!provinceMap.containsKey(province.getCode())) {
            throw new ProvinceRepositoryException();
        } else {
            provinceMap.remove(province.getCode());
        }
        return true;
    }

    @Override
    public void updateOrInsert(List<Province> provinces) {
        for (Province province : provinces) {
            try {
                update(province);
            } catch (ProvinceRepositoryException pre) {
                save(province);
            }
        }
    }
}
