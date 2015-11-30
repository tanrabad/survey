package th.or.nectec.tanrabad.domain.geographic;

import java.util.List;

import th.or.nectec.tanrabad.domain.place.PlaceRepository;
import th.or.nectec.tanrabad.entity.Location;
import th.or.nectec.tanrabad.entity.LocationEntity;

public class FilterTrimmedBoundaryController {
    private FilterBoundaryCalculator filterBoundaryCalculator;
    private PlaceRepository placeRepository;
    private NearbyPlacePresenter nearbyPlacePresenter;
    private CoordinateLocationCalculator coordinateLocationCalculate;

    public FilterTrimmedBoundaryController(FilterBoundaryCalculator filterBoundaryCalculator,
                                           CoordinateLocationCalculator coordinateLocationCalculate,
                                           PlaceRepository placeRepository,
                                           NearbyPlacePresenter nearbyPlacePresenter) {
        this.filterBoundaryCalculator = filterBoundaryCalculator;
        this.coordinateLocationCalculate = coordinateLocationCalculate;
        this.placeRepository = placeRepository;
        this.nearbyPlacePresenter = nearbyPlacePresenter;
    }

    public void findNearByFilterTrimmedBoundary(Location currentLocation, double distanceInKm) {
        Location outsideMinimumLocation = filterBoundaryCalculator.getMinLocation(currentLocation, distanceInKm);
        Location outsideMaximumLocation = filterBoundaryCalculator.getMaxLocation(currentLocation, distanceInKm);

        Location insideMinimumLocation = coordinateLocationCalculate.getNewMinLocation(currentLocation, distanceInKm);
        Location insideMaximumLocation = coordinateLocationCalculate.getNewMaxLocation(currentLocation, distanceInKm);

        List<LocationEntity> placeFiltered = placeRepository.findTrimmedInBoundaryLocation(insideMinimumLocation, outsideMinimumLocation, insideMaximumLocation, outsideMaximumLocation);

        if (placeFiltered == null) {
            nearbyPlacePresenter.displayPlaceNotFound();
        } else {
            nearbyPlacePresenter.displayNearByPlaces(placeFiltered);
        }
    }
}
