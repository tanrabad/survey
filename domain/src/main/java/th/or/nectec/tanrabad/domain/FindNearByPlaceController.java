package th.or.nectec.tanrabad.domain;

import java.util.List;

import th.or.nectec.tanrabad.domain.place.PlaceRepository;
import th.or.nectec.tanrabad.entity.Location;
import th.or.nectec.tanrabad.entity.LocationEntity;

class FindNearByPlaceController {
    private FilterBoundaryCalculator filterBoundaryCalculator;
    private PlaceRepository placeRepository;
    private DistanceSorter distanceSorter;
    private NearByPlacePresenter nearByPlacePresenter;

    public FindNearByPlaceController(FilterBoundaryCalculator filterBoundaryCalculator,
                                     PlaceRepository placeRepository,
                                     DistanceSorter distanceSorter,
                                     NearByPlacePresenter nearByPlacePresenter) {
        this.filterBoundaryCalculator = filterBoundaryCalculator;
        this.placeRepository = placeRepository;
        this.distanceSorter = distanceSorter;
        this.nearByPlacePresenter = nearByPlacePresenter;
    }

    public void findNearByPlace(Location currentLocation, int distanceInKm) {
        Location minimumLocation = filterBoundaryCalculator.getMinLocation(currentLocation, distanceInKm);
        Location maximumLocation = filterBoundaryCalculator.getMaxLocation(currentLocation, distanceInKm);

        List<LocationEntity> placeFilter = placeRepository.findInBoundaryLocation(minimumLocation, maximumLocation);

        if (placeFilter == null) {
            nearByPlacePresenter.displayPlaceNotFound();
        } else {
            distanceSorter.sort(placeFilter);
            nearByPlacePresenter.displayNearByPlaces(placeFilter);
        }
    }

}
