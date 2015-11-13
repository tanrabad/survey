package th.or.nectec.tanrabad.domain;

import th.or.nectec.tanrabad.entity.Place;

import java.util.List;
import java.util.UUID;

public interface PlaceRepository {

    List<Place> findPlaces();

    void findPlaceByPlaceName(String placeName);

    Place findPlaceByPlaceUUID(UUID placeUUID);
}
