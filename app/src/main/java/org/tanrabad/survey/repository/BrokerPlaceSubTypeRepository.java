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

import org.tanrabad.survey.TanrabadApp;
import org.tanrabad.survey.repository.persistence.DbPlaceSubTypeRepository;
import org.tanrabad.survey.domain.place.PlaceSubTypeRepository;
import th.or.nectec.tanrabad.entity.lookup.PlaceSubType;

import java.util.List;

public class BrokerPlaceSubTypeRepository implements PlaceSubTypeRepository {

    private static BrokerPlaceSubTypeRepository instance;

    private PlaceSubTypeRepository persistance;
    private PlaceSubTypeRepository cache;

    public BrokerPlaceSubTypeRepository(PlaceSubTypeRepository persistance, PlaceSubTypeRepository cache) {
        this.persistance = persistance;
        this.cache = cache;
        cache.updateOrInsert(persistance.find());
    }

    public static PlaceSubTypeRepository getInstance() {
        if (instance == null)
            instance = new BrokerPlaceSubTypeRepository(new DbPlaceSubTypeRepository(TanrabadApp.getInstance()),
                    InMemoryPlaceSubTypeRepository.getInstance());
        return instance;
    }

    @Override
    public List<PlaceSubType> find() {
        List<PlaceSubType> placeSubTypes = cache.find();
        if (placeSubTypes == null || placeSubTypes.isEmpty()) {
            placeSubTypes = persistance.find();
            cache.updateOrInsert(placeSubTypes);
        }
        return null;
    }

    @Override
    public PlaceSubType findById(int subTypeId) {
        PlaceSubType subType = cache.findById(subTypeId);
        if (subType == null)
            subType = persistance.findById(subTypeId);
        return subType;
    }

    @Override
    public List<PlaceSubType> findByPlaceTypeId(int placeTypeId) {
        List<PlaceSubType> subTypes = cache.findByPlaceTypeId(placeTypeId);
        if (subTypes == null || subTypes.isEmpty()) {
            subTypes = persistance.findByPlaceTypeId(placeTypeId);
            cache.updateOrInsert(subTypes);
        }
        return subTypes;
    }

    @Override
    public int getDefaultPlaceSubTypeId(int placeId) {
        return persistance.getDefaultPlaceSubTypeId(placeId);
    }

    @Override
    public boolean save(PlaceSubType placeSubType) {
        boolean success = persistance.save(placeSubType);
        if (success)
            cache.save(placeSubType);
        return success;
    }

    @Override
    public boolean update(PlaceSubType placeSubType) {
        boolean success = persistance.update(placeSubType);
        if (success)
            cache.update(placeSubType);
        return success;
    }

    @Override
    public boolean delete(PlaceSubType placeSubType) {
        boolean success = persistance.delete(placeSubType);
        if (success)
            cache.delete(placeSubType);
        return success;
    }

    @Override
    public void updateOrInsert(List<PlaceSubType> placeSubTypes) {
        persistance.updateOrInsert(placeSubTypes);
        cache.updateOrInsert(placeSubTypes);
    }
}
