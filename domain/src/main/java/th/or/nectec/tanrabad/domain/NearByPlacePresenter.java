package th.or.nectec.tanrabad.domain;

import java.util.List;

import th.or.nectec.tanrabad.entity.LocationEntity;

public interface NearByPlacePresenter {
    void displayPlaceNotFound();

    void displayNearByPlaces(List<LocationEntity> placeFilter);
}
