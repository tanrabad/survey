package org.tanrabad.survey.nearby;

import java.util.List;
import org.tanrabad.survey.entity.Place;
import org.tanrabad.survey.entity.field.Location;
import org.tanrabad.survey.nearby.repository.NearbyPlaceRepository;

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
        List<Place> nearbyPlaceWithoutLo = repository.findByPlaces(nearbyPlace);

        if (nearbyPlaceWithoutLo == null) {
            presenter.displayNearbyPlaces(nearbyPlace);
        } else {
            List<Place> nearByPlaces = sorter.mergeAndSort(nearbyPlace, nearbyPlaceWithoutLo);
            presenter.displayNearbyPlaces(nearByPlaces);
        }
    }
}
