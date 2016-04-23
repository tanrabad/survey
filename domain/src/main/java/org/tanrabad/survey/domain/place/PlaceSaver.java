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

package org.tanrabad.survey.domain.place;

import th.or.nectec.tanrabad.entity.Place;

public class PlaceSaver {
    private final PlaceSavePresenter placeSavePresenter;
    private final PlaceRepository placeRepository;
    private final PlaceValidator placeValidator;

    public PlaceSaver(PlaceRepository placeRepository,
                      PlaceValidator placeValidator,
                      PlaceSavePresenter placeSavePresenter) {
        this.placeRepository = placeRepository;
        this.placeSavePresenter = placeSavePresenter;
        this.placeValidator = placeValidator;
        placeValidator.setPlaceRepository(placeRepository);
    }

    public void save(Place place) {
        if (placeValidator.validate(place) && placeRepository.save(place)) {
            placeSavePresenter.displaySaveSuccess();
        } else {
            placeSavePresenter.displaySaveFail();
        }
    }

    public void update(Place place) {
        if (placeValidator.validate(place) && placeRepository.update(place)) {
            placeSavePresenter.displayUpdateSuccess();
        } else {
            placeSavePresenter.displayUpdateFail();
        }
    }
}
