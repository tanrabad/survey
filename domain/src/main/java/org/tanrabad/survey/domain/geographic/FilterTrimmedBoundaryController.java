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

package org.tanrabad.survey.domain.geographic;

import java.util.List;

import org.tanrabad.survey.entity.LocationEntity;
import org.tanrabad.survey.entity.field.Location;

class FilterTrimmedBoundaryController {
    private final FilterBoundaryCalculator filterBoundaryCalculator;
    private final LocationRepository placeRepository;
    private final NearbyPlacePresenter nearbyPlacePresenter;
    private final CoordinateLocationCalculator coordinateLocationCalculate;

    public FilterTrimmedBoundaryController(FilterBoundaryCalculator filterBoundaryCalculator,
                                           CoordinateLocationCalculator coordinateLocationCalculate,
                                           LocationRepository locationRepository,
                                           NearbyPlacePresenter nearbyPlacePresenter) {
        this.filterBoundaryCalculator = filterBoundaryCalculator;
        this.coordinateLocationCalculate = coordinateLocationCalculate;
        this.placeRepository = locationRepository;
        this.nearbyPlacePresenter = nearbyPlacePresenter;
    }

    public void findNearByFilterTrimmedBoundary(Location currentLocation, double distanceInKm) {
        Location outsideMinimumLocation = filterBoundaryCalculator.getMinLocation(currentLocation, distanceInKm);
        Location outsideMaximumLocation = filterBoundaryCalculator.getMaxLocation(currentLocation, distanceInKm);

        Location insideMinimumLocation = coordinateLocationCalculate.getNewMinLocation(currentLocation, distanceInKm);
        Location insideMaximumLocation = coordinateLocationCalculate.getNewMaxLocation(currentLocation, distanceInKm);

        List<LocationEntity> placeFiltered = placeRepository.findTrimmedInBoundaryLocation(
                insideMinimumLocation, outsideMinimumLocation, insideMaximumLocation, outsideMaximumLocation);

        if (placeFiltered == null) {
            nearbyPlacePresenter.displayPlaceNotFound();
        } else {
            nearbyPlacePresenter.displayNearByPlaces(placeFiltered);
        }
    }
}