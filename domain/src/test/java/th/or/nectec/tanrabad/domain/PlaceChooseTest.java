package th.or.nectec.tanrabad.domain;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import th.or.nectec.tanrabad.entity.Place;

public class PlaceChooseTest {

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();
    private PlaceRepository placeRepository;
    private PlaceListPresenter placeListPresenter;

    @Before
    public void setUp() {
        placeRepository = context.mock(PlaceRepository.class);
        placeListPresenter = context.mock(PlaceListPresenter.class);
    }

    @Test
    public void getPlaceList() {
        final List<Place> places = new ArrayList<>();
        places.add(Place.withName("Vaillage A"));
        places.add(Place.withName("Vaillage C"));

        context.checking(new Expectations() {
            {
                oneOf(placeRepository).findPlaces();
                will(returnValue(places));
                oneOf(placeListPresenter).displayPlaceList(places);
            }
        });
        PlaceChooser chooser = new PlaceChooser(placeRepository, placeListPresenter);
        chooser.getPlaceList();
    }

    @Test
    public void getPlaceListWithFilterBuildingType() throws Exception {
        Place villageA = Place.withName("Village A");
        villageA.setType(Place.TYPE_VILLAGE_COMMUNITY);

        Place villageB = Place.withName("Village B");
        villageB.setType(Place.TYPE_VILLAGE_COMMUNITY);

        final List<Place> filterPlace = new ArrayList<>();
        filterPlace.add(villageA);
        filterPlace.add(villageB);

        context.checking(new Expectations() {
            {
                oneOf(placeRepository).findPlacesWithPlaceFilter(Place.TYPE_VILLAGE_COMMUNITY);
                will(returnValue(filterPlace));
                oneOf(placeListPresenter).displayPlaceList(filterPlace);
            }
        });

        PlaceChooser chooser = new PlaceChooser(placeRepository, placeListPresenter);
        chooser.getPlaceListWithPlaceFilter(Place.TYPE_VILLAGE_COMMUNITY);
    }

    @Test
    public void testPlaceListNotFound() throws Exception {
        context.checking(new Expectations() {
            {
                oneOf(placeRepository).findPlaces();
                will(returnValue(null));
                oneOf(placeListPresenter).displayPlaceNotFound();
            }
        });
        PlaceChooser chooser = new PlaceChooser(placeRepository, placeListPresenter);
        chooser.getPlaceList();
    }
}
