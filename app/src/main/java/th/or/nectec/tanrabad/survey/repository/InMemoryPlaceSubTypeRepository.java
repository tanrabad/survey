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

import java.util.ArrayList;
import java.util.List;

import th.or.nectec.tanrabad.domain.place.PlaceSubTypeRepository;
import th.or.nectec.tanrabad.domain.place.PlaceSubTypeRepositoryException;
import th.or.nectec.tanrabad.entity.lookup.PlaceSubType;

class InMemoryPlaceSubTypeRepository implements PlaceSubTypeRepository {

    private static InMemoryPlaceSubTypeRepository instance;
    private ArrayList<PlaceSubType> placeSubTypes;

    private InMemoryPlaceSubTypeRepository() {
        placeSubTypes = new ArrayList<>();
    }

    public static InMemoryPlaceSubTypeRepository getInstance() {
        if (instance == null)
            instance = new InMemoryPlaceSubTypeRepository();
        return instance;
    }

    @Override
    public List<PlaceSubType> find() {
        return placeSubTypes;
    }

    @Override
    public PlaceSubType findById(int placeSubTypeId) {
        for (PlaceSubType placeSubType : placeSubTypes) {
            if (placeSubType.getId() == placeSubTypeId)
                return placeSubType;
        }
        return null;
    }

    @Override
    public List<PlaceSubType> findByPlaceTypeId(int placeTypeId) {
        ArrayList<PlaceSubType> queryPlaceSubType = new ArrayList<>();
        for (PlaceSubType placeSubType : placeSubTypes) {
            if (placeSubType.getPlaceTypeId() == placeTypeId)
                queryPlaceSubType.add(placeSubType);
        }
        return queryPlaceSubType;
    }

    @Override
    public int getDefaultPlaceSubTypeId(int placeId) {
        for (PlaceSubType placeSubType : placeSubTypes) {
            if (placeSubType.getPlaceTypeId() == placeId)
                return placeSubType.getId();
        }
        return -1;
    }

    @Override
    public void updateOrInsert(List<PlaceSubType> updateList) {
        for (PlaceSubType placeSubType : updateList) {
            try {
                update(placeSubType);
            } catch (PlaceSubTypeRepositoryException pre) {
                save(placeSubType);
            }
        }
    }

    public boolean save(PlaceSubType placeSubType) {
        if (placeSubTypes.contains(placeSubType)) {
            throw new PlaceSubTypeRepositoryException();
        } else {
            placeSubTypes.add(placeSubType);
        }
        return true;
    }

    public boolean update(PlaceSubType placeSubType) {
        if (!placeSubTypes.contains(placeSubType)) {
            throw new PlaceSubTypeRepositoryException();
        } else {
            placeSubTypes.set(placeSubTypes.indexOf(placeSubType), placeSubType);
        }
        return true;
    }

    @Override
    public boolean delete(PlaceSubType placeSubType) {
        if (!placeSubTypes.contains(placeSubType)) {
            throw new PlaceSubTypeRepositoryException();
        }
        placeSubTypes.remove(placeSubType);
        return true;
    }
}
