package th.or.nectec.tanrabad.domain;

import th.or.nectec.tanrabad.entity.Place;

class PlaceSaver {
        private final PlaceSavePresenter placeSavePresenter;
        private final PlaceRepository placeRepository;
        private final PlaceValidator placeValidator;

    public PlaceSaver(PlaceSavePresenter placeSavePresenter,
                      PlaceValidator placeValidator,
                      PlaceRepository placeRepository) {

        this.placeRepository = placeRepository;
        this.placeSavePresenter = placeSavePresenter;
        this.placeValidator = placeValidator;
    }

    public void save(Place place) {
        if(place.getType()== Place.TYPE_VILLAGE_COMMUNITY){
            placeSavePresenter.alertCannotSaveVillageType();
            return;
        }

        if(placeValidator.validate(place)){
            if(placeRepository.save(place))
                placeSavePresenter.displaySaveSuccess();
        }else{
            placeSavePresenter.displaySaveFail();
        }
    }
}
