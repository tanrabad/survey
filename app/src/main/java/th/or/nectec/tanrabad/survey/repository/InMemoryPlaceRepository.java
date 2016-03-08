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

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import th.or.nectec.tanrabad.domain.place.PlaceRepository;
import th.or.nectec.tanrabad.domain.place.PlaceRepositoryException;
import th.or.nectec.tanrabad.entity.Place;

public class InMemoryPlaceRepository implements PlaceRepository {

    public static InMemoryPlaceRepository instance;

    Map<UUID, Place> placesMap = new HashMap<>();

    protected static InMemoryPlaceRepository getInstance() {
        if (instance == null) {
            instance = new InMemoryPlaceRepository();
        }
        return instance;
    }

    @Override
    public List<Place> find() {
        return new ArrayList<>(placesMap.values());
    }

    @Override
    public Place findByUuid(UUID placeUuid) {
        for (Place eachPlace : placesMap.values()) {
            if (eachPlace.getId().equals(placeUuid)) {
                return eachPlace;
            }
        }
        return null;
    }

    @Override
    public List<Place> findByPlaceType(int placeType) {
        ArrayList<Place> filterPlaces = new ArrayList<>();
        for (Place eachPlace : placesMap.values()) {
            if (eachPlace.getType() == placeType)
                filterPlaces.add(eachPlace);
        }
        return filterPlaces.isEmpty() ? null : filterPlaces;
    }

    @Override
    public List<Place> findByName(String placeName) {
        if (TextUtils.isEmpty(placeName))
            return null;

        ArrayList<Place> filterPlaces = new ArrayList<>();
        for (Place eachPlace : placesMap.values()) {
            if (eachPlace.getName().contains(placeName))
                filterPlaces.add(eachPlace);
        }
        return filterPlaces.isEmpty() ? null : filterPlaces;
    }

    @Override
    public boolean save(Place place) {
        if (placesMap.containsKey(place.getId())) {
            throw new PlaceRepositoryException();
        }
        placesMap.put(place.getId(), place);
        return true;
    }

    @Override
    public boolean update(Place place) {
        if (!placesMap.containsKey(place.getId())) {
            throw new PlaceRepositoryException();
        }
        placesMap.put(place.getId(), place);
        return true;
    }

    @Override
    public void updateOrInsert(List<Place> update) {
        for (Place place : update) {
            try {
                update(place);
            } catch (PlaceRepositoryException pre) {
                save(place);
            }
        }
    }


}
