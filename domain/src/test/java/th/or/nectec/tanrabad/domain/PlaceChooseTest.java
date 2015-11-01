package th.or.nectec.tanrabad.domain;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import th.or.nectec.tanrabad.entity.Place;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class PlaceChooseTest {

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    @Test
    public void getPlaceList() {
        final PlaceRepository placeRepository = context.mock(PlaceRepository.class);

        final List<Place> places = new ArrayList<>();
        places.add(Place.withName("Vaillage A"));
        places.add(Place.withName("Vaillage C"));

        context.checking(new Expectations() {
            {
                oneOf(placeRepository).findPlaces();
                will(returnValue(places));
            }
        });
        PlaceChooser chooser = new PlaceChooser(placeRepository);
        assertEquals(places, chooser.getPlaceList());

    }

}
