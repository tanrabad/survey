package org.tanrabad.survey.nearby;

import java.util.Collections;
import java.util.List;
import org.tanrabad.survey.entity.Place;

public class ImpMergeAndSortNearbyPlaces implements MergeAndSortNearbyPlaces {
    @Override public List<Place> mergeAndSort(List<Place> placesWithLocation, List<Place> placesWithoutLocation) {
        List<Place> mergeAndSortPlaces = placesWithLocation;

        Collections.sort(placesWithoutLocation, new PlaceWeightComparator());
        mergeAndSortPlaces.addAll(placesWithoutLocation);

        return mergeAndSortPlaces;
    }
}
