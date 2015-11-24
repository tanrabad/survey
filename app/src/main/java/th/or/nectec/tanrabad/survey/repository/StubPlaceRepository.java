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
import th.or.nectec.tanrabad.domain.place.PlaceRepository;
import th.or.nectec.tanrabad.entity.Place;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StubPlaceRepository implements PlaceRepository {

    private final Place palazzettoVillage;
    private final Place bangkokHospital;
    private final Place watpaphukon;
    private final Place saintMarySchool;
    private final Place donboscoSchool;
    private final Place anubarnNursery;
    private final Place thammasatHospital;
    private final Place golfView;
    ArrayList<Place> places = new ArrayList<>();

    public StubPlaceRepository() {
        palazzettoVillage = new Place(generateUUID("1abc"), "หมู่บ้านพาลาซเซตโต้");
        palazzettoVillage.setType(Place.TYPE_VILLAGE_COMMUNITY);
        golfView = new Place(generateUUID("67UIP"), "ชุมชนกอล์ฟวิว");
        golfView.setType(Place.TYPE_VILLAGE_COMMUNITY);
        bangkokHospital = new Place(generateUUID("2bcd"), "โรงพยาบาลกรุงเทพ");
        bangkokHospital.setType(Place.TYPE_HOSPITAL);
        thammasatHospital = new Place(generateUUID("32UAW"), "โรงพยาบาลธรรมศาสตร์");
        thammasatHospital.setType(Place.TYPE_HOSPITAL);
        watpaphukon = new Place(generateUUID("3def"), "วัดป่าภูก้อน");
        watpaphukon.setType(Place.TYPE_WORSHIP);
        saintMarySchool = new Place(generateUUID("042ST"), "โรงเรียนเซนต์เมรี่");
        saintMarySchool.setType(Place.TYPE_SCHOOL);
        donboscoSchool = new Place(generateUUID("12AJK"), "โรงเรียนดอนบอสโก");
        donboscoSchool.setType(Place.TYPE_SCHOOL);
        anubarnNursery = new Place(generateUUID("45JKO"), "โรงเรียนอนุบาล");
        anubarnNursery.setType(Place.TYPE_SCHOOL);

        places.add(palazzettoVillage);
        places.add(golfView);
        places.add(bangkokHospital);
        places.add(watpaphukon);
        places.add(saintMarySchool);
        places.add(donboscoSchool);
        places.add(anubarnNursery);
    }

    @NonNull
    private UUID generateUUID(String input) {
        return UUID.nameUUIDFromBytes(input.getBytes());
    }

    public Place getPalazzettoVillage() {
        return palazzettoVillage;
    }

    public Place getBangkokHospital() {
        return bangkokHospital;
    }

    public Place getWatpaphukon() {
        return watpaphukon;
    }

    @Override
    public List<Place> findPlaces() {
        return places;
    }

    @Override
    public void findPlaceByPlaceName(String placeName) {

    }

    @Override
    public Place findPlaceByPlaceUUID(UUID placeUUID) {
        for (Place eachPlace : places) {
            if (eachPlace.getId().equals(placeUUID)) {
                return eachPlace;
            }
        }
        return null;
    }

    @Override
    public List<Place> findPlacesWithPlaceFilter(int placeType) {
        ArrayList<Place> filterPlaces = new ArrayList<>();
        for (Place eachPlace : places) {
            if (eachPlace.getType() == placeType)
                filterPlaces.add(eachPlace);
        }
        return filterPlaces.isEmpty() ? null : filterPlaces;
    }

    @Override
    public boolean save(Place with) {
        return false;
    }
}
