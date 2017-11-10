package org.tanrabad.survey.nearby.repository;

import java.util.Comparator;

import org.tanrabad.survey.nearby.distance.DistanceCalculator;
import org.tanrabad.survey.nearby.distance.EllipsoidDistance;
import org.tanrabad.survey.entity.Place;
import org.tanrabad.survey.entity.field.Location;

class PlaceDistanceComparator implements Comparator<Place> {

    private final Location myLocation;
    private final DistanceCalculator calculator;

    public PlaceDistanceComparator(Location myLocation) {
        this(myLocation, new EllipsoidDistance());
    }

    public PlaceDistanceComparator(Location myLocation, DistanceCalculator calculator ) {
        this.calculator = calculator;
        this.myLocation = myLocation;
    }

    @Override public int compare(Place place, Place anotherPlace) {
        double thisPlaceDistance = calculator.calculate(place.getLocation(), myLocation);
        double thatPlaceDistance = calculator.calculate(anotherPlace.getLocation(), myLocation);
        if (thisPlaceDistance > thatPlaceDistance) {
            return 1;
        } else if (thisPlaceDistance < thatPlaceDistance) {
            return -1;
        } else {
            return 0;
        }
    }
}
