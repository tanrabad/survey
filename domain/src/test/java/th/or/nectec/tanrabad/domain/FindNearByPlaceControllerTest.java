package th.or.nectec.tanrabad.domain;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

import th.or.nectec.tanrabad.entity.Location;
import th.or.nectec.tanrabad.entity.Place;

public class FindNearByPlaceControllerTest {

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    private PlaceRepository placeRepository;
    private PlaceListPresenter placeListPresenter;
    private FilterBoundaryCalculator filterBoundaryCalculate;

    double currentLatitude = 40.6892;
    double currentLongitude = -74.0444;
    final Location currentLocation = new Location(currentLatitude, currentLongitude);

    int distanceInKm = 100;

    @Before
    public void setUp() {
        placeRepository = context.mock(PlaceRepository.class);
        placeListPresenter = context.mock(PlaceListPresenter.class);
        filterBoundaryCalculate = context.mock(FilterBoundaryCalculator.class);

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
        final ArrayList<Place> placesFilter = new ArrayList<>();

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
                oneOf(placeListPresenter).displayPlaceList(placesFilter);
            }
        });
        FindNearByPlaceController locationBoundaryController = new FindNearByPlaceController(filterBoundaryCalculate, placeRepository, placeListPresenter);
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
                oneOf(placeListPresenter).displayPlaceNotFound();
            }
        });
        FindNearByPlaceController locationBoundaryController = new FindNearByPlaceController(filterBoundaryCalculate, placeRepository, placeListPresenter);
        locationBoundaryController.findNearByPlace(currentLocation, distanceInKm);
    }

}
