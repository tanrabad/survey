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
import org.tanrabad.survey.repository.persistence.DbSubdistrictRepository;

import java.util.List;

public final class BrokerSubdistrictRepository implements SubdistrictRepository {

    private static BrokerSubdistrictRepository instance;
    private SubdistrictRepository cache;
    private SubdistrictRepository persistence;


    private BrokerSubdistrictRepository(SubdistrictRepository cache, SubdistrictRepository persistence) {
        this.cache = cache;
        this.persistence = persistence;
    }

    public static BrokerSubdistrictRepository getInstance() {
        if (instance == null)
            instance = new BrokerSubdistrictRepository(InMemorySubdistrictRepository.getInstance(),
                    DbSubdistrictRepository.getInstance());
        return instance;
    }

    @Override
    public List<Subdistrict> find() {
        List<Subdistrict> subdistricts = cache.find();
        if (subdistricts.isEmpty()) {
            subdistricts = persistence.find();
            for (Subdistrict subDis : subdistricts) {
                cache.save(subDis);
            }
        }
        return subdistricts;
    }

    @Override
    public List<Subdistrict> findByDistrictCode(String districtCode) {
        List<Subdistrict> subdistricts = cache.findByDistrictCode(districtCode);
        if (subdistricts == null) {
            subdistricts = persistence.findByDistrictCode(districtCode);
            for (Subdistrict subdist : subdistricts) {
                cache.save(subdist);
            }
        }
        return subdistricts;
    }

    @Override
    public Subdistrict findByCode(String subdistrictCode) {
        Subdistrict subdistrict = cache.findByCode(subdistrictCode);
        if (subdistrict == null) {
            subdistrict = persistence.findByCode(subdistrictCode);
            if (subdistrict != null)
                cache.save(subdistrict);
        }
        return subdistrict;
    }

    @Override
    public boolean save(Subdistrict subdistrict) {
        boolean success = persistence.save(subdistrict);
        if (success) {
            cache.save(subdistrict);
        }
        return success;
    }

    @Override
    public boolean update(Subdistrict subdistrict) {
        boolean success = persistence.update(subdistrict);
        if (success) {
            cache.update(subdistrict);
        }
        return success;
    }

    @Override
    public boolean delete(Subdistrict subdistrict) {
        boolean success = persistence.delete(subdistrict);
        if (success) {
            cache.delete(subdistrict);
        }
        return success;
    }

    @Override
    public void updateOrInsert(List<Subdistrict> update) {
        persistence.updateOrInsert(update);
        cache.updateOrInsert(update);
    }
}
