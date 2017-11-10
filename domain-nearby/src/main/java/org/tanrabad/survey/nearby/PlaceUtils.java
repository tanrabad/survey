package org.tanrabad.survey.nearby;

import java.util.ArrayList;
import java.util.List;
import org.tanrabad.survey.entity.Place;

public class PlaceUtils {
    public static List<Place> getPlacesWithoutLocation(List<Place> places) {
        List<Place> placesWithoutLocation = new ArrayList<>();
        for (Place place : places) {
            if (place.getLocation() == null) {
                placesWithoutLocation.add(place);
            }
        }
        return placesWithoutLocation.isEmpty() ? null : placesWithoutLocation;
    }

    public static List<String> groupingSubdistict(List<Place> places) {
        List<String> groupingAddressString = new ArrayList<>();
        for (Place place : places) {
            String subdistrictCode = place.getSubdistrictCode();
            if (!groupingAddressString.contains(subdistrictCode)) {
                groupingAddressString.add(subdistrictCode);
            }
        }
        return groupingAddressString;
    }

    public static List<Place> findPlacesWithoutLocationInsideSubdistrict(List<String> grouppingSubdistrict,
            List<Place> placesWithoutLocation) {
        List<Place> placeInsideSubdistrict = new ArrayList<>();
        for (Place place : placesWithoutLocation) {
            if (grouppingSubdistrict.contains(place.getSubdistrictCode())) {
                placeInsideSubdistrict.add(place);
            }
        }
        return placeInsideSubdistrict;
    }
}
