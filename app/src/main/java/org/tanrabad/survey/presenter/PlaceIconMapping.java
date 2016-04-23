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

package org.tanrabad.survey.presenter;

import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.entity.lookup.PlaceSubType;
import th.or.nectec.tanrabad.entity.lookup.PlaceType;
import org.tanrabad.survey.R;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class PlaceIconMapping {
    private static final Map<Integer, Integer> PLACE_ICON_MAP = new ConcurrentHashMap<>();

    static {
        PLACE_ICON_MAP.put(PlaceType.VILLAGE_COMMUNITY, R.mipmap.ic_place_village);
        PLACE_ICON_MAP.put(PlaceSubType.TEMPLE, R.mipmap.ic_place_temple);
        PLACE_ICON_MAP.put(PlaceSubType.CHURCH, R.mipmap.ic_place_church);
        PLACE_ICON_MAP.put(PlaceSubType.MOSQUE, R.mipmap.ic_place_mosque);
        PLACE_ICON_MAP.put(PlaceType.SCHOOL, R.mipmap.ic_place_school);
        PLACE_ICON_MAP.put(PlaceType.HOSPITAL, R.mipmap.ic_place_hospital);
        PLACE_ICON_MAP.put(PlaceType.FACTORY, R.mipmap.ic_place_factory);
    }

    public static int getPlaceIcon(Place place) {
        if (place.getType() == PlaceType.WORSHIP) {
            if (!PLACE_ICON_MAP.containsKey(place.getSubType()))
                return R.mipmap.ic_place_temple;
            return PLACE_ICON_MAP.get(place.getSubType());
        } else {
            if (!PLACE_ICON_MAP.containsKey(place.getType()))
                return R.mipmap.ic_place;
            return PLACE_ICON_MAP.get(place.getType());
        }
    }
}
