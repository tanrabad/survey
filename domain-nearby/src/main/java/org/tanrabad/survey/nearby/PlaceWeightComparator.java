package org.tanrabad.survey.nearby;

import java.util.Comparator;
import org.tanrabad.survey.entity.Place;


public class PlaceWeightComparator implements Comparator<Place> {
    @Override public int compare(Place place, Place anotherPlace) {

        double placeWeight = place.getWeight();
        double anotherPlaceWeight = anotherPlace.getWeight();
        if (placeWeight > anotherPlaceWeight) {
            return -1;
        }

        if (placeWeight < anotherPlaceWeight) {
            return 1;
        }

        return 0;
    }
}
