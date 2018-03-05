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

import org.joda.time.LocalTime;
import org.tanrabad.survey.TanrabadApp;
import org.tanrabad.survey.domain.place.PlaceRepository;
import org.tanrabad.survey.entity.Place;
import org.tanrabad.survey.entity.User;
import org.tanrabad.survey.repository.persistence.DbPlaceRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class BrokerPlaceRepository implements PlaceRepository {

    private static BrokerPlaceRepository instance;
    private PlaceRepository cache;
    private PlaceRepository persistence;

    private static final int RECENT_CACHE_TIME = 1;
    private List<Place> recentPlace = new ArrayList<>();
    private LocalTime recentLastUpdate = LocalTime.now().minusHours(1);


    private BrokerPlaceRepository(PlaceRepository cache, PlaceRepository persistence) {
        this.cache = cache;
        this.persistence = persistence;
    }

    public static BrokerPlaceRepository getInstance() {
        if (instance == null)
            instance = new BrokerPlaceRepository(InMemoryPlaceRepository.getInstance(),
                    new DbPlaceRepository(TanrabadApp.getInstance()));
        return instance;
    }

    @Override
    public List<Place> find() {
        return persistence.find();
    }

    @Override
    public Place findByUuid(UUID placeUuid) {
        Place place = cache.findByUuid(placeUuid);
        if (place == null) {
            place = persistence.findByUuid(placeUuid);
            if (place != null)
                cache.save(place);
        }
        return place;
    }

    @Override
    public List<Place> findByPlaceType(int placeType) {
        return persistence.findByPlaceType(placeType);
    }

    @Override
    public List<Place> findByName(String placeName) {
        return persistence.findByName(placeName);
    }

    @Override
    public List<Place> findRecent(User user) {
        List<Place> recent = new ArrayList<>();
        if (recentPlace.isEmpty() || recentLastUpdate.plusMinutes(RECENT_CACHE_TIME).isBefore(LocalTime.now())) {
            recent = persistence.findRecent(user);
            if (recent != null && !recent.isEmpty()) {
                recentPlace.addAll(recent);
            }
        }
        return recent;
    }

    @Override
    public boolean save(Place place) {
        boolean success = persistence.save(place);
        if (success) {
            cache.save(place);
        }
        return success;
    }

    @Override
    public boolean update(Place place) {
        boolean success = persistence.update(place);
        if (success) {
            cache.update(place);
        }
        return success;
    }

    @Override
    public boolean delete(Place place) {
        boolean success = persistence.delete(place);
        if (success) {
            cache.delete(place);
        }
        return success;
    }

    @Override
    public void updateOrInsert(List<Place> update) {
        persistence.updateOrInsert(update);
        cache.updateOrInsert(update);
    }
}
