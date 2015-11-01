package th.or.nectec.tanrabad.domain;

import th.or.nectec.tanrabad.entity.Place;

import java.util.List;

public interface PlaceRepository {

    List<Place> findPlaces();
}
