package th.or.nectec.tanrabad.domain;

import java.util.List;

import th.or.nectec.tanrabad.entity.Place;

public interface PlaceListPresenter {
    void displayPlaceList(List<Place> places);

    void displayPlaceNotFound();
}
