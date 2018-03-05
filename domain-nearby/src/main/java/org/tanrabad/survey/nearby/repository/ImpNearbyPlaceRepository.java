package org.tanrabad.survey.nearby.repository;

import org.tanrabad.survey.domain.place.PlaceRepository;
import org.tanrabad.survey.entity.Place;
import org.tanrabad.survey.entity.field.Location;
import org.tanrabad.survey.nearby.PlaceUtils;
import org.tanrabad.survey.nearby.matching.WeightPlaceWithoutLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ImpNearbyPlaceRepository implements NearbyPlaceRepository {

    public static final int DISTANCE_IN_KM = 10;

    private LocationBoundCalculator locationBoundCalculator;
    private int distanceKm;
    private PlaceRepository repository;

    public ImpNearbyPlaceRepository(PlaceRepository repository) {
        this(repository, DISTANCE_IN_KM);
    }

    public ImpNearbyPlaceRepository(PlaceRepository repository, int distanceKm) {
        this(repository, distanceKm, new ImpLocationBoundCalculator());
    }

    public ImpNearbyPlaceRepository(PlaceRepository repository, int distanceKm, LocationBoundCalculator locationBoundCalculator) {
        this.repository = repository;
        this.locationBoundCalculator = locationBoundCalculator;
        this.distanceKm = distanceKm;
    }

    @Override public List<Place> findByLocation(final Location location) {
        List<Place> places = repository.find();
        if (places == null || places.isEmpty()) return null;

        List<Place> place = getPlaceInsideLocationBoundary(places, locationBoundCalculator.get(location, distanceKm));
        Collections.sort(place, new PlaceDistanceComparator(location));
        return place.isEmpty() ? null : place;
    }

    private List<Place> getPlaceInsideLocationBoundary(List<Place> places, org.tanrabad.survey.entity.field.LocationBound locationBound) {
        List<Place> placeInsideLocation = new ArrayList<>();
        Location minimumLocation = locationBound.getMinimumLocation();
        Location maximumLocation = locationBound.getMaximumLocation();
        for (Place eachPlace : places) {
            Location placeLocation = eachPlace.getLocation();
            if (placeLocation == null) continue;

            if (placeLocation.isLocationInsideBoundary(minimumLocation, maximumLocation)) {
                placeInsideLocation.add(eachPlace);
            }
        }
        return placeInsideLocation;
    }

    @Override
    public List<Place> findByPlaces(List<Place> nearbyPlaces) {
        List<Place> places = repository.find();
        List<Place> placesWithoutLocation = PlaceUtils.getPlacesWithoutLocation(places);
        if (placesWithoutLocation == null) return null;

        List<Place> placesWithoutLocationInsideSubdistrict =
            PlaceUtils.findPlacesWithoutLocationInsideSubdistrict(PlaceUtils.groupingSubdistict(nearbyPlaces),
                placesWithoutLocation);
        Map<UUID, Double> weightPlaceWithoutPlantationScore =
            WeightPlaceWithoutLocation.calculate(nearbyPlaces, placesWithoutLocationInsideSubdistrict);
        for (Place place : placesWithoutLocationInsideSubdistrict) {
            place.setWeight(weightPlaceWithoutPlantationScore.get(place.getId()));
        }
        return placesWithoutLocationInsideSubdistrict;

    }
}
