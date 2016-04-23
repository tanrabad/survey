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

import th.or.nectec.tanrabad.domain.place.PlaceTypeRepository;
import th.or.nectec.tanrabad.domain.place.PlaceTypeRepositoryException;
import th.or.nectec.tanrabad.entity.lookup.PlaceType;

import java.util.ArrayList;
import java.util.List;

final class InMemoryPlaceTypeRepository implements PlaceTypeRepository {

    private static InMemoryPlaceTypeRepository instance;
    private List<PlaceType> placeTypes;

    private InMemoryPlaceTypeRepository() {
        placeTypes = new ArrayList<>();
    }

    public static InMemoryPlaceTypeRepository getInstance() {
        if (instance == null)
            instance = new InMemoryPlaceTypeRepository();
        return instance;
    }

    @Override
    public List<PlaceType> find() {
        return placeTypes;
    }

    @Override
    public PlaceType findById(int placeTypeId) {
        for (PlaceType placeType : placeTypes) {
            if (placeType.getId() == placeTypeId)
                return placeType;
        }
        return null;
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
        if (placeTypes.contains(placeType)) {
            throw new PlaceTypeRepositoryException();
        } else {
            placeTypes.add(placeType);
        }
        return true;
    }

    public boolean update(PlaceType placeType) {
        if (!placeTypes.contains(placeType)) {
            throw new PlaceTypeRepositoryException();
        } else {
            placeTypes.set(placeTypes.indexOf(placeType), placeType);
        }
        return true;
    }

    @Override
    public boolean delete(PlaceType placeType) {
        if (!placeTypes.contains(placeType)) {
            throw new PlaceTypeRepositoryException();
        }
        placeTypes.remove(placeType.getId());
        return true;
    }
}
