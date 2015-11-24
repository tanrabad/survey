package th.or.nectec.tanrabad.domain;

import java.util.List;
import java.util.UUID;

import th.or.nectec.tanrabad.entity.Location;
import th.or.nectec.tanrabad.entity.Locationing;
import th.or.nectec.tanrabad.entity.Place;

public interface PlaceRepository {

    List<Place> findPlaces();

    void findPlaceByPlaceName(String placeName);

    Place findPlaceByPlaceUUID(UUID placeUUID);

    List<Place> findPlacesWithPlaceFilter(int typeVillageCommunity);

    boolean save(Place with);

    List<Locationing> findInBoundaryLocation(Location minimumLocation, Location maximumLocation);
}
