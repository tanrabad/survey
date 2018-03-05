package org.tanrabad.survey.nearby;

import org.tanrabad.survey.entity.Place;
import org.tanrabad.survey.entity.field.Location;
import org.tanrabad.survey.nearby.repository.NearbyPlaceRepository;

import java.util.List;

public class NearbyPlacesFinderController {
    private NearbyPlaceRepository repository;
    private MergeAndSortNearbyPlaces sorter;
    private NearbyPlacePresenter presenter;

    public NearbyPlacesFinderController(NearbyPlaceRepository repository,
                                        MergeAndSortNearbyPlaces sorter,
                                        NearbyPlacePresenter presenter) {
        this.repository = repository;
        this.sorter = sorter;
        this.presenter = presenter;
    }

    public void findNearbyPlaces(Location myLocation) {
        List<Place> nearbyPlace = repository.findByLocation(myLocation);

        if (nearbyPlace == null) {
            presenter.displayPlaceNotFound();
            return;
        }
        presenter.displayNearbyPlaces(nearbyPlace);

        List<Place> nearbyPlaceWithoutLo = repository.findByPlaces(nearbyPlace);
        if (nearbyPlaceWithoutLo != null && !nearbyPlaceWithoutLo.isEmpty()) {
            List<Place> nearByPlaces = sorter.mergeAndSort(nearbyPlace, nearbyPlaceWithoutLo);
            presenter.displayNearbyPlaces(nearByPlaces);
        }
    }
}
