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

package th.or.nectec.tanrabad.domain.place;

import th.or.nectec.tanrabad.entity.Place;

class PlaceSaver {
        private final PlaceSavePresenter placeSavePresenter;
        private final PlaceRepository placeRepository;
        private final PlaceValidator placeValidator;

    public PlaceSaver(PlaceSavePresenter placeSavePresenter,
                      PlaceValidator placeValidator,
                      PlaceRepository placeRepository) {

        this.placeRepository = placeRepository;
        this.placeSavePresenter = placeSavePresenter;
        this.placeValidator = placeValidator;
    }

    public void save(Place place) {
        if(place.getType()== Place.TYPE_VILLAGE_COMMUNITY){
            placeSavePresenter.alertCannotSaveVillageType();
            return;
        }

        if(placeValidator.validate(place)){
            if(placeRepository.save(place))
                placeSavePresenter.displaySaveSuccess();
        }else{
            placeSavePresenter.displaySaveFail();
        }
    }
}