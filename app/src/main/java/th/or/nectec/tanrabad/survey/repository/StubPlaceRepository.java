package th.or.nectec.tanrabad.survey.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import th.or.nectec.tanrabad.domain.PlaceRepository;
import th.or.nectec.tanrabad.entity.Place;

public class StubPlaceRepository implements PlaceRepository {
    @Override
    public List<Place> findPlaces() {
        List<Place> places = new ArrayList<>();
        places.add(new Place(UUID.nameUUIDFromBytes("1abc".getBytes()), "บางไผ่"));
        places.add(new Place(UUID.nameUUIDFromBytes("2bcd".getBytes()), "บางโพธิ์"));
        places.add(new Place(UUID.nameUUIDFromBytes("3def".getBytes()), "บางไทร"));
        return places;
    }
}
