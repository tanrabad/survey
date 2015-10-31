package th.or.nectec.tanlabad.domain;

import th.or.nectec.tanrabad.entity.Place;

import java.util.List;

public class PlaceChooser {

    private PlaceRepository placeRepository;

    public PlaceChooser(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public List<Place> getPlaceList() {
        return this.placeRepository.findPlaces();
    }
}
