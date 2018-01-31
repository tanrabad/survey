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

package org.tanrabad.survey.validator;


import org.tanrabad.survey.R;
import org.tanrabad.survey.domain.place.PlaceRepository;
import org.tanrabad.survey.domain.place.PlaceValidator;
import org.tanrabad.survey.entity.Place;
import org.tanrabad.survey.entity.lookup.PlaceType;

import java.util.List;


public class SavePlaceValidator implements PlaceValidator {
    private PlaceRepository placeRepository;

    @Override
    public boolean validate(Place place) {
        if (place.getName() == null || place.getName().isEmpty()) {
            throw new EmptyNameException(R.string.please_define_place_name);
        }

        if (place.getType() < 0) {
            throw new ValidatorException(R.string.please_define_place_type);
        }

        if (place.getSubType() < 0) {
            throw new ValidatorException(R.string.please_define_place_sub_type);
        }

        if (place.getSubdistrictCode() == null || place.getSubdistrictCode().isEmpty()) {
            throw new NullAddressException(R.string.please_define_place_address);
        }

        List<Place> places = placeRepository.find();
        if (places != null) {
            for (Place eachPlace : places) {
                if (isSamePlaceName(place, eachPlace)
                        && isSamePlaceType(place, eachPlace)
                        && isSamePlaceAddress(place, eachPlace)) {
                    throw new ValidatorException(R.string.cant_save_same_place_name);
                }
            }
        }

        return true;
    }

    private boolean isSamePlaceName(Place place, Place comparePlace) {
        return comparePlace.getName().equals(place.getName());
    }

    private boolean isSamePlaceType(Place place, Place comparePlace) {
        if (place.getType() == PlaceType.WORSHIP) {
            return comparePlace.getSubType() == place.getSubType();
        }
        return comparePlace.getType() == place.getType();
    }

    private boolean isSamePlaceAddress(Place place, Place comparePlace) {
        return comparePlace.getSubdistrictCode().equals(place.getSubdistrictCode());
    }

    @Override
    public void setPlaceRepository(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }
}
