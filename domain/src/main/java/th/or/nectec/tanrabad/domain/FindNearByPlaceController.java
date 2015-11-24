package th.or.nectec.tanrabad.domain;

import java.util.List;

import th.or.nectec.tanrabad.entity.Location;
import th.or.nectec.tanrabad.entity.Place;

class FindNearByPlaceController {
    private FilterBoundaryCalculator filterBoundaryCalculator;
    private PlaceRepository placeRepository;
    private PlaceListPresenter placeListPresenter;

    public FindNearByPlaceController(FilterBoundaryCalculator filterBoundaryCalculator, PlaceRepository placeRepository, PlaceListPresenter placeListPresenter) {
        this.filterBoundaryCalculator = filterBoundaryCalculator;
        this.placeRepository = placeRepository;
        this.placeListPresenter = placeListPresenter;
    }

    public void findNearByPlace(Location currentLocation, int distanceInKm) {
        Location minimumLocation = filterBoundaryCalculator.getMinLocation(currentLocation, distanceInKm);
        Location maximumLocation = filterBoundaryCalculator.getMaxLocation(currentLocation, distanceInKm);

        List<Place> placeFilter = placeRepository.findInBoundaryLocation(minimumLocation, maximumLocation);
        if (placeFilter == null) {
            placeListPresenter.displayPlaceNotFound();
        } else {
            placeListPresenter.displayPlaceList(placeFilter);
        }
    }

}
