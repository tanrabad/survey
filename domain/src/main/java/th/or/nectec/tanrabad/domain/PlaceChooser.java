package th.or.nectec.tanrabad.domain;

import java.util.List;

import th.or.nectec.tanrabad.entity.Place;

public class PlaceChooser {

    private PlaceRepository placeRepository;
    private PlaceListPresenter placeListPresenter;

    public PlaceChooser(PlaceRepository placeRepository, PlaceListPresenter placeListPresenter) {
        this.placeRepository = placeRepository;
        this.placeListPresenter = placeListPresenter;
    }

    public void getPlaceList() {
        List<Place> places = this.placeRepository.findPlaces();
        if (places == null) {
            placeListPresenter.displayPlaceNotFound();
        } else {
            placeListPresenter.displayPlaceList(places);
        }
    }

    public void getPlaceListWithPlaceFilter(int typeVillageCommunity) {
        List<Place> places = this.placeRepository.findPlacesWithPlaceFilter(typeVillageCommunity);
        if (places == null) {
            placeListPresenter.displayPlaceNotFound();
        } else {
            placeListPresenter.displayPlaceList(places);
        }
    }
}
