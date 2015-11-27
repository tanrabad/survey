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

package th.or.nectec.tanrabad.domain.geographic;

import th.or.nectec.tanrabad.domain.place.PlaceRepository;
import th.or.nectec.tanrabad.entity.Location;
import th.or.nectec.tanrabad.entity.LocationEntity;

import java.util.List;

public class FindNearbyPlaceController {
    private FilterBoundaryCalculator filterBoundaryCalculator;
    private PlaceRepository placeRepository;
    private DistanceSorter distanceSorter;
    private NearbyPlacePresenter nearbyPlacePresenter;

    public FindNearbyPlaceController(FilterBoundaryCalculator filterBoundaryCalculator,
                                     PlaceRepository placeRepository,
                                     DistanceSorter distanceSorter,
                                     NearbyPlacePresenter nearbyPlacePresenter) {
        this.filterBoundaryCalculator = filterBoundaryCalculator;
        this.placeRepository = placeRepository;
        this.distanceSorter = distanceSorter;
        this.nearbyPlacePresenter = nearbyPlacePresenter;
    }

    public void findNearbyPlace(Location currentLocation, int distanceInKm) {
        Location minimumLocation = filterBoundaryCalculator.getMinLocation(currentLocation, distanceInKm);
        Location maximumLocation = filterBoundaryCalculator.getMaxLocation(currentLocation, distanceInKm);

        List<LocationEntity> placeFiltered = placeRepository.findInBoundaryLocation(minimumLocation, maximumLocation);

        if (placeFiltered == null) {
            nearbyPlacePresenter.displayPlaceNotFound();
        } else {
            distanceSorter.sort(placeFiltered);
            nearbyPlacePresenter.displayNearByPlaces(placeFiltered);
        }
    }

}
