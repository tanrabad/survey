package th.or.nectec.tanrabad.domain;

import java.util.List;

import th.or.nectec.tanrabad.entity.Locationing;

public interface NearByPlacePresenter {
    void displayPlaceNotFound();

    void displayNearByPlaces(List<Locationing> placeFilter);
}
