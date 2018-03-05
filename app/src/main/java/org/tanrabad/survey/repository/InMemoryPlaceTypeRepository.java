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

import org.tanrabad.survey.domain.place.PlaceTypeRepository;
import org.tanrabad.survey.domain.place.PlaceTypeRepositoryException;
import org.tanrabad.survey.entity.lookup.PlaceType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

final class InMemoryPlaceTypeRepository implements PlaceTypeRepository {

    private static InMemoryPlaceTypeRepository instance;
    private Map<Integer, PlaceType> placeTypes;

    private InMemoryPlaceTypeRepository() {
        placeTypes = new ConcurrentHashMap<>();
    }

    public static InMemoryPlaceTypeRepository getInstance() {
        if (instance == null)
            instance = new InMemoryPlaceTypeRepository();
        return instance;
    }

    @Override
    public List<PlaceType> find() {
        return new ArrayList<>(placeTypes.values());
    }

    @Override
    public PlaceType findById(int placeTypeId) {
        return placeTypes.get(placeTypeId);
    }

    @Override
    public void updateOrInsert(List<PlaceType> updateList) {
        for (PlaceType placeType : updateList) {
            try {
                update(placeType);
            } catch (PlaceTypeRepositoryException pre) {
                save(placeType);
            }
        }
    }

    public boolean save(PlaceType placeType) {
        placeTypes.put(placeType.getId(), placeType);
        return true;
    }

    public boolean update(PlaceType placeType) {
        placeTypes.put(placeType.getId(), placeType);
        return true;
    }

    @Override
    public boolean delete(PlaceType placeType) {
        if (!placeTypes.containsKey(placeType.getId())) {
            throw new PlaceTypeRepositoryException();
        }
        placeTypes.remove(placeType.getId());
        return true;
    }
}
