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

package th.or.nectec.tanrabad.survey.repository;

import java.util.List;

import th.or.nectec.tanrabad.domain.place.PlaceTypeRepository;
import th.or.nectec.tanrabad.entity.lookup.PlaceType;
import th.or.nectec.tanrabad.survey.TanrabadApp;
import th.or.nectec.tanrabad.survey.repository.persistence.DbPlaceTypeRepository;

public class BrokerPlaceTypeRepository implements PlaceTypeRepository {

    private static BrokerPlaceTypeRepository instance;
    private PlaceTypeRepository persistence;
    private PlaceTypeRepository cache;

    public BrokerPlaceTypeRepository(PlaceTypeRepository persistence, PlaceTypeRepository cache) {
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
    public void updateOrInsert(List<PlaceType> placeTypes) {
        persistence.updateOrInsert(placeTypes);
        cache.updateOrInsert(placeTypes);
    }
}
