package org.tanrabad.survey.nearby.repository;

import java.util.List;
import org.tanrabad.survey.entity.Place;
import org.tanrabad.survey.entity.field.Location;

public interface NearbyPlaceRepository {
    List<Place> findByLocation(Location location);

    List<Place> findByPlaces(List<Place> places);
}
