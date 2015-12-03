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

package th.or.nectec.tanrabad.survey.presenter;

import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.survey.R;

import java.util.HashMap;

public class PlaceIconMapping {
    private static final HashMap<Integer, Integer> PLACE_ICON_MAP = new HashMap<>();

    static {
        PLACE_ICON_MAP.put(Place.TYPE_VILLAGE_COMMUNITY, R.mipmap.ic_place_village);
        PLACE_ICON_MAP.put(Place.SUBTYPE_TEMPLE, R.mipmap.ic_place_temple);
        PLACE_ICON_MAP.put(Place.SUBTYPE_CHURCH, R.mipmap.ic_place_church);
        PLACE_ICON_MAP.put(Place.SUBTYPE_MOSQUE, R.mipmap.ic_place_mosque);
        PLACE_ICON_MAP.put(Place.TYPE_SCHOOL, R.mipmap.ic_place_school);
        PLACE_ICON_MAP.put(Place.TYPE_HOSPITAL, R.mipmap.ic_place_hospital);
        PLACE_ICON_MAP.put(Place.TYPE_FACTORY, R.mipmap.ic_place_factory);
    }

    public static int getPlaceIcon(Place place) {
        if (place.getType() == Place.TYPE_WORSHIP) {
            return PLACE_ICON_MAP.get(place.getSubType());
        } else {
            return PLACE_ICON_MAP.get(place.getType());
        }
    }
}