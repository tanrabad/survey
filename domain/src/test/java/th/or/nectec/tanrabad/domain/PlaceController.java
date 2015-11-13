package th.or.nectec.tanrabad.domain;

import java.util.UUID;

import th.or.nectec.tanrabad.entity.Place;

public class PlaceController {

    private PlaceRepository placeRepository;
    private  PlacePresenter placePresenter;

    public PlaceController(PlaceRepository placeRepository, PlacePresenter placePresenter) {
        this.placeRepository = placeRepository;
        this.placePresenter = placePresenter;
    }

    public void showPlace(UUID placeUUID) {
        Place placeByUUID = placeRepository.findPlaceByPlaceUUID(placeUUID);
        if (placeByUUID == null) {
            placePresenter.displayNotFoundPlace();
        } else {
            placePresenter.displayPlace(placeByUUID);
        }
    }
}
