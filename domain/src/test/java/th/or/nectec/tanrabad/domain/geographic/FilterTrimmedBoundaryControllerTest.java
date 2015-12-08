package th.or.nectec.tanrabad.domain.geographic;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import th.or.nectec.tanrabad.domain.place.PlaceRepository;
import th.or.nectec.tanrabad.entity.Location;
import th.or.nectec.tanrabad.entity.LocationEntity;
import th.or.nectec.tanrabad.entity.Place;

public class FilterTrimmedBoundaryControllerTest {

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();
    double currentLatitude = 40.6892;
    double currentLongitude = -74.0444;
    final Location currentLocation = new Location(currentLatitude, currentLongitude);
    double distanceInKm = 100;

    private PlaceRepository placeRepository;
    private NearbyPlacePresenter nearbyPlacePresenter;
    private FilterBoundaryCalculator filterBoundaryCalculate;
    private CoordinateLocationCalculator coordinateLocationCalculate;

    @Before
    public void setUp() {
        placeRepository = context.mock(PlaceRepository.class);
        nearbyPlacePresenter = context.mock(NearbyPlacePresenter.class);
        filterBoundaryCalculate = context.mock(FilterBoundaryCalculator.class);
        coordinateLocationCalculate = context.mock(CoordinateLocationCalculator.class);

        ArrayList<Place> places = new ArrayList<>();
        Place place1 = Place.withName("a");
        place1.setLocation(new Location(39.00000, -73.00000));
        places.add(place1);

        Place place2 = Place.withName("b");
        place2.setLocation(new Location(39.05000, -73.04000));
        places.add(place2);

        Place place3 = Place.withName("c");
        place3.setLocation(new Location(40.95000, -74.95000));
        places.add(place3);

        Place place4 = Place.withName("d");
        place4.setLocation(new Location(39.80000, -73.20000));
        places.add(place4);

        Place place5 = Place.withName("e");
        place5.setLocation(new Location(39.85000, -73.25000));
        places.add(place5);

        Place place6 = Place.withName("f");
        place5.setLocation(new Location(40.90000, -74.10000));
        places.add(place6);

        Place place7 = Place.withName("g");
        place5.setLocation(new Location(40.85000, -74.05000));
        places.add(place7);

        Place place8 = Place.withName("h");
        place5.setLocation(new Location(40.09000, -73.30000));
        places.add(place8);

        Place place9 = Place.withName("i");
        place5.setLocation(new Location(40.10000, -73.35000));
        places.add(place9);

        Place place10 = Place.withName("j");
        place5.setLocation(new Location(40.00000, -74.35000));
        places.add(place10);

        //BigSquare 7 location include small square4 =2, small square3 =1
    }

    @Test
    public void testGetTrimmedPlaceListLocation() throws Exception {

        final ArrayList<LocationEntity> locationEntities = new ArrayList<>();

        Place place6 = Place.withName("f");
        place6.setLocation(new Location(40.90000, -74.10000));
        locationEntities.add(place6);

        Place place7 = Place.withName("g");
        place7.setLocation(new Location(40.85000, -74.05000));
        locationEntities.add(place7);

        Place place8 = Place.withName("h");
        place8.setLocation(new Location(40.09000, -73.30000));
        locationEntities.add(place8);

        Place place9 = Place.withName("i");
        place9.setLocation(new Location(40.10000, -73.35000));
        locationEntities.add(place9);

        double minimumLatitude = 39.78477101124205;
        double minimumLongitude = -74.93966316260665;
        final Location outsideMinimumLocation = new Location(minimumLatitude, minimumLongitude);

        double newMinimumLatitude = 40.05029095061978;
        double newMinimumLongitude = -74.21719530257553;
        final Location insideMinimumLocation = new Location(newMinimumLatitude, newMinimumLongitude);

        double maximumLatitude = 41.59362898875795;
        double maximumLongitude = -73.14913683739334;
        final Location outsideMaximumLocation = new Location(maximumLatitude, maximumLongitude);

        double newMaximumLatitude = 41.3220397344946955;
        double newMaximumLongitude = -73.87166829177177;
        final Location insideMaximumLocation = new Location(newMaximumLatitude, newMaximumLongitude);

        context.checking(new Expectations() {
            {
                oneOf(filterBoundaryCalculate).getMinLocation(with(currentLocation), with(distanceInKm));
                will(returnValue(outsideMinimumLocation));
                oneOf(coordinateLocationCalculate).getNewMinLocation(with(currentLocation), with(distanceInKm));
                will(returnValue(insideMinimumLocation));
                oneOf(filterBoundaryCalculate).getMaxLocation(with(currentLocation), with(distanceInKm));
                will(returnValue(outsideMaximumLocation));
                oneOf(coordinateLocationCalculate).getNewMaxLocation(with(currentLocation), with(distanceInKm));
                will(returnValue(insideMaximumLocation));
                oneOf(placeRepository).findTrimmedInBoundaryLocation(insideMinimumLocation, outsideMinimumLocation, insideMaximumLocation, outsideMaximumLocation);
                will(returnValue(locationEntities));
                oneOf(nearbyPlacePresenter).displayNearByPlaces(locationEntities);
            }
        });
        FilterTrimmedBoundaryController filterTrimmedBoundaryController = new FilterTrimmedBoundaryController(filterBoundaryCalculate, coordinateLocationCalculate,
                placeRepository,
                nearbyPlacePresenter);
        filterTrimmedBoundaryController.findNearByFilterTrimmedBoundary(currentLocation, distanceInKm);
    }

    @Test
    public void testBigSquareLocationListNotFound() throws Exception {

        double minimumLatitude = 39.78477101124205;
        double minimumLongitude = -74.93966316260665;
        final Location outsideMinimumLocation = new Location(minimumLatitude, minimumLongitude);

        double newMinimumLatitude = 40.05029095061978;
        double newMinimumLongitude = -74.21719530257553;
        final Location insideMinimumLocation = new Location(newMinimumLatitude, newMinimumLongitude);

        double maximumLatitude = 41.59362898875795;
        double maximumLongitude = -73.14913683739334;
        final Location outsideMaximumLocation = new Location(maximumLatitude, maximumLongitude);

        double newMaximumLatitude = 41.3220397344946955;
        double newMaximumLongitude = -73.87166829177177;
        final Location insideMaximumLocation = new Location(newMaximumLatitude, newMaximumLongitude);

        context.checking(new Expectations() {
            {
                oneOf(filterBoundaryCalculate).getMinLocation(with(currentLocation), with(distanceInKm));
                will(returnValue(outsideMinimumLocation));
                oneOf(coordinateLocationCalculate).getNewMinLocation(with(currentLocation), with(distanceInKm));
                will(returnValue(insideMinimumLocation));
                oneOf(filterBoundaryCalculate).getMaxLocation(with(currentLocation), with(distanceInKm));
                will(returnValue(outsideMaximumLocation));
                oneOf(coordinateLocationCalculate).getNewMaxLocation(with(currentLocation), with(distanceInKm));
                will(returnValue(insideMaximumLocation));
                oneOf(placeRepository).findTrimmedInBoundaryLocation(insideMinimumLocation, outsideMinimumLocation, insideMaximumLocation, outsideMaximumLocation);
                will(returnValue(null));
                oneOf(nearbyPlacePresenter).displayPlaceNotFound();
            }
        });
        FilterTrimmedBoundaryController filterTrimmedBoundaryController = new FilterTrimmedBoundaryController(filterBoundaryCalculate, coordinateLocationCalculate,
                placeRepository,
                nearbyPlacePresenter);
        filterTrimmedBoundaryController.findNearByFilterTrimmedBoundary(currentLocation, distanceInKm);
    }
}
