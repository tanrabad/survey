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
import org.tanrabad.survey.repository.persistence.DbDistrictRepository;

import java.util.List;

public final class BrokerDistrictRepository implements DistrictRepository {

    private static BrokerDistrictRepository instance;
    private DistrictRepository cache;
    private DistrictRepository persistence;


    private BrokerDistrictRepository(DistrictRepository cache, DistrictRepository persistence) {
        this.cache = cache;
        this.persistence = persistence;
    }

    public static BrokerDistrictRepository getInstance() {
        if (instance == null)
            instance = new BrokerDistrictRepository(InMemoryDistrictRepository.getInstance(),
                DbDistrictRepository.getInstance());
        return instance;
    }

    @Override
    public List<District> find() {
        List<District> districts = cache.find();
        if (districts.isEmpty()) {
            districts = persistence.find();
            for (District dis : districts) {
                cache.save(dis);
            }
        }
        return districts;
    }

    @Override
    public List<District> findByProvinceCode(String provinceCode) {
        return persistence.findByProvinceCode(provinceCode);
    }

    @Override
    public District findByCode(String provinceCode) {
        District district = cache.findByCode(provinceCode);
        if (district == null) {
            district = persistence.findByCode(provinceCode);
            if (district != null)
                cache.save(district);
        }
        return district;
    }

    @Override
    public boolean save(District district) {
        boolean success = persistence.save(district);
        if (success) {
            cache.save(district);
        }
        return success;
    }

    @Override
    public boolean update(District district) {
        boolean success = persistence.update(district);
        if (success) {
            cache.update(district);
        }
        return success;
    }

    @Override
    public boolean delete(District district) {
        boolean success = persistence.delete(district);
        if (success) {
            cache.delete(district);
        }
        return success;
    }

    @Override
    public void updateOrInsert(List<District> update) {
        persistence.updateOrInsert(update);
        cache.updateOrInsert(update);
    }
}
