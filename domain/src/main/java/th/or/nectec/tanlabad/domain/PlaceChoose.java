package th.or.nectec.tanlabad.domain;

import th.or.nectec.tanrabad.Place;

import java.util.List;

public class PlaceChoose {

    private PlaceRepository placeRepository;

    public PlaceChoose(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public List<Place> getPlaceList() {
        return this.placeRepository.findPlaces();
    }
}
