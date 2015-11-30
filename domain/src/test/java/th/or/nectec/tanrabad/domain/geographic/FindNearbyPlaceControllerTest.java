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

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import th.or.nectec.tanrabad.domain.place.PlaceRepository;
import th.or.nectec.tanrabad.entity.Location;
import th.or.nectec.tanrabad.entity.LocationEntity;
import th.or.nectec.tanrabad.entity.Place;

import java.util.ArrayList;

public class FindNearByPlaceControllerTest {

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();
    double currentLatitude = 40.6892;
    double currentLongitude = -74.0444;
    final Location currentLocation = new Location(currentLatitude, currentLongitude);
    double distanceInKm = 100;
    private PlaceRepository placeRepository;
    private NearbyPlacePresenter nearbyPlacePresenter;
    private FilterBoundaryCalculator filterBoundaryCalculate;
    private DistanceSorter distanceSorter;

    @Before
    public void setUp() {
        placeRepository = context.mock(PlaceRepository.class);
        nearbyPlacePresenter = context.mock(NearbyPlacePresenter.class);
        filterBoundaryCalculate = context.mock(FilterBoundaryCalculator.class);
        distanceSorter = context.mock(DistanceSorter.class);

        ArrayList<Place> places = new ArrayList<>();
        Place place1 = Place.withName("a");
        place1.setLocation(new Location(38.123, -80.123));
        places.add(place1);

        Place place2 = Place.withName("b");
        place2.setLocation(new Location(39.800, -74.000));
        places.add(place2);

        Place place3 = Place.withName("c");
        place3.setLocation(new Location(30.123, -72.123));
        places.add(place3);

        Place place4 = Place.withName("d");
        place4.setLocation(new Location(40.000, -73.000));
        places.add(place4);

        Place place5 = Place.withName("e");
        place5.setLocation(new Location(41.500, -74.500));
        places.add(place5);
    }

    @Test
    public void testGetPlaceListWithLocationFilter() throws Exception {
        final ArrayList<LocationEntity> placesFilter = new ArrayList<>();

        Place place2 = Place.withName("b");
        place2.setLocation(new Location(39.800, -74.000));
        placesFilter.add(place2);

        Place place4 = Place.withName("d");
        place4.setLocation(new Location(40.000, -73.000));
        placesFilter.add(place4);

        Place place5 = Place.withName("e");
        place5.setLocation(new Location(41.500, -74.500));
        placesFilter.add(place5);

        double minimumLatitude = 39.7802;
        double minimumLongitude = -74.9453;
        final Location minimumLocation = new Location(minimumLatitude, minimumLongitude);

        double maximumLatitude = 41.5982;
        double maximumLongitude = -73.1434;
        final Location maximumLocation = new Location(maximumLatitude, maximumLongitude);

        context.checking(new Expectations() {
            {
                oneOf(filterBoundaryCalculate).getMinLocation(with(currentLocation), with(distanceInKm));
                will(returnValue(minimumLocation));
                oneOf(filterBoundaryCalculate).getMaxLocation(with(currentLocation), with(distanceInKm));
                will(returnValue(maximumLocation));
                oneOf(placeRepository).findInBoundaryLocation(minimumLocation, maximumLocation);
                will(returnValue(placesFilter));
                oneOf(distanceSorter).sort(placesFilter);
                oneOf(nearbyPlacePresenter).displayNearByPlaces(placesFilter);
            }
        });
        FindNearByPlaceController locationBoundaryController = new FindNearByPlaceController(filterBoundaryCalculate,
                placeRepository,
                distanceSorter,
                nearbyPlacePresenter);
        locationBoundaryController.findNearByPlace(currentLocation, distanceInKm);
    }

    @Test
    public void testLocationListNotFound() throws Exception {

        double minimumLatitude = 39.7802;
        double minimumLongitude = -74.9453;
        final Location minimumLocation = new Location(minimumLatitude, minimumLongitude);

        double maximumLatitude = 41.5982;
        double maximumLongitude = -73.1434;
        final Location maximumLocation = new Location(maximumLatitude, maximumLongitude);

        context.checking(new Expectations() {
            {
                oneOf(filterBoundaryCalculate).getMinLocation(with(currentLocation), with(distanceInKm));
                will(returnValue(minimumLocation));
                oneOf(filterBoundaryCalculate).getMaxLocation(with(currentLocation), with(distanceInKm));
                will(returnValue(maximumLocation));
                oneOf(placeRepository).findInBoundaryLocation(minimumLocation, maximumLocation);
                will(returnValue(null));
                oneOf(nearbyPlacePresenter).displayPlaceNotFound();
            }
        });
        FindNearByPlaceController locationBoundaryController = new FindNearByPlaceController(filterBoundaryCalculate,
                placeRepository,
                distanceSorter,
                nearbyPlacePresenter);
        locationBoundaryController.findNearByPlace(currentLocation, distanceInKm);
    }

}
