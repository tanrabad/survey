/*
 * Copyright (c) 2015 NECTEC
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

import android.support.annotation.NonNull;

import th.or.nectec.tanrabad.domain.PlaceRepository;
import th.or.nectec.tanrabad.entity.Place;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StubPlaceRepository implements PlaceRepository {

    public Place getPalazzettoVillage() {
        return palazzettoVillage;
    }

    public Place getBangkokHospital() {
        return bangkokHospital;
    }

    public Place getWatpaphukon() {
        return watpaphukon;
    }

    private final Place palazzettoVillage;
    private final Place bangkokHospital;
    private final Place watpaphukon;

    public StubPlaceRepository() {
        palazzettoVillage = new Place(generateUUID("1abc"), "บ้านพาลาซเซตโต้");
        palazzettoVillage.setType(Place.TYPE_VILLAGE_COMMUNITY);
        bangkokHospital = new Place(generateUUID("2bcd"), "โรงพยาบาลกรุงเทพ");
        bangkokHospital.setType(Place.TYPE_HOSPITAL);
        watpaphukon = new Place(generateUUID("3def"), "วัดป่าภูก้อน");
        watpaphukon.setType(Place.TYPE_WORSHIP);
    }

    @NonNull
    private UUID generateUUID(String input) {
        return UUID.nameUUIDFromBytes(input.getBytes());
    }

    @Override
    public List<Place> findPlaces() {
        List<Place> places = new ArrayList<>();
        places.add(palazzettoVillage);
        places.add(bangkokHospital);
        places.add(watpaphukon);
        return places;
    }
}
