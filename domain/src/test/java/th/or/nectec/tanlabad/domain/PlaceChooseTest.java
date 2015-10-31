package th.or.nectec.tanlabad.domain;

import org.junit.Test;
import th.or.nectec.tanrabad.Place;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class PlaceChooseTest {

    @Test
    public void getPlaceList() {
        PlaceChoose chooser = new PlaceChoose(new SinglePlaceRepository());

        List<Place> places = chooser.getPlaceList();

        assertEquals(1, places.size());
    }

    private static class SinglePlaceRepository implements PlaceRepository {

        @Override
        public List<Place> findPlaces() {
            List<Place> places = new ArrayList<>();
            places.add(new Place());
            return places;
        }
    }
}
