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

import th.or.nectec.tanrabad.entity.Location;
import th.or.nectec.tanrabad.entity.LocationEntity;
import th.or.nectec.tanrabad.entity.Place;

import java.util.List;
import java.util.UUID;

public interface PlaceRepository {

    List<Place> findPlaces();

    void findPlaceByPlaceName(String placeName);

    Place findPlaceByPlaceUUID(UUID placeUUID);

    List<Place> findPlacesWithPlaceFilter(int typeVillageCommunity);

    boolean save(Place with);

    List<LocationEntity> findInBoundaryLocation(Location minimumLocation, Location maximumLocation);

    List<LocationEntity> findTrimmedInBoundaryLocation(Location insideMinimumLocation, Location outsideMinimumLocation, Location insideMaximumLocation, Location outsideMaximumLocation);


}
