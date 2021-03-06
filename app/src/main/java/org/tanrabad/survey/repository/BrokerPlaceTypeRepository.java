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
import org.tanrabad.survey.domain.place.PlaceTypeRepository;
import org.tanrabad.survey.entity.lookup.PlaceType;
import org.tanrabad.survey.repository.persistence.DbPlaceTypeRepository;

import java.util.List;

public final class BrokerPlaceTypeRepository implements PlaceTypeRepository {

    private static BrokerPlaceTypeRepository instance;
    private PlaceTypeRepository persistence;
    private PlaceTypeRepository cache;

    private BrokerPlaceTypeRepository(PlaceTypeRepository persistence, PlaceTypeRepository cache) {
        this.persistence = persistence;
        this.cache = cache;
        cache.updateOrInsert(persistence.find());
    }

    public static BrokerPlaceTypeRepository getInstance() {
        if (instance == null)
            instance = new BrokerPlaceTypeRepository(new DbPlaceTypeRepository(TanrabadApp.getInstance()),
                    InMemoryPlaceTypeRepository.getInstance());
        return instance;
    }

    @Override
    public List<PlaceType> find() {
        List<PlaceType> placeTypes = cache.find();
        if (placeTypes == null || placeTypes.isEmpty()) {
            placeTypes = persistence.find();
            cache.updateOrInsert(placeTypes);
        }
        return placeTypes;
    }

    @Override
    public PlaceType findById(int placeTypeId) {
        PlaceType placeType = cache.findById(placeTypeId);
        if (placeType == null) {
            placeType = persistence.findById(placeTypeId);
            if (placeType != null)
                cache.save(placeType);
        }
        return placeType;
    }

    @Override
    public boolean save(PlaceType placeType) {
        boolean success = persistence.save(placeType);
        if (success)
            cache.save(placeType);
        return success;
    }

    @Override
    public boolean update(PlaceType placeType) {
        boolean success = persistence.update(placeType);
        if (success)
            cache.update(placeType);
        return success;
    }

    @Override
    public boolean delete(PlaceType placeType) {
        boolean success = persistence.delete(placeType);
        if (success) {
            cache.delete(placeType);
        }
        return success;
    }

    @Override
    public void updateOrInsert(List<PlaceType> placeTypes) {
        persistence.updateOrInsert(placeTypes);
        cache.updateOrInsert(placeTypes);
    }
}
