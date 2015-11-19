package th.or.nectec.tanrabad.domain;

import th.or.nectec.tanrabad.entity.Place;

public interface PlacePresenter {
    void displayPlace(Place place);

    void alertPlaceNotFound();

}
