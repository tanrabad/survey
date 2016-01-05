package th.or.nectec.tanrabad.survey.presenter.job.service.jsonentity;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.UUID;

import th.or.nectec.tanrabad.domain.UserRepository;
import th.or.nectec.tanrabad.entity.Location;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.entity.utils.Address;

@JsonObject
public class JsonPlace {

    @JsonField(name = "place_id", typeConverter = UUIDConverter.class)
    public UUID placeID;

    @JsonField(name = "place_type_id")
    public int placeTypeID;

    @JsonField(name = "place_subtype_id")
    public int placeSubtypeID;

    @JsonField(name = "place_namet")
    public String placeName;

    @JsonField(name = "tambon_code")
    public String tambonCode;

    @JsonField
    public JsonLocation location;

    @JsonField(name = "update_by")
    public String updateBy;


    public static JsonPlace parse(Place place) {
        JsonPlace jsonPlace = new JsonPlace();
        jsonPlace.placeID = place.getId();
        jsonPlace.placeName = place.getName();
        jsonPlace.placeTypeID = place.getType();
        jsonPlace.placeSubtypeID = place.getSubType();
        jsonPlace.location = JsonLocation.parse(place.getLocation());
        jsonPlace.tambonCode = place.getAddress().getAddressCode();
        jsonPlace.updateBy = place.getUpdateBy().getUsername();
        return jsonPlace;
    }

    public Place getEntity(UserRepository userRepository) {
        Place place = new Place(placeID, placeName);
        place.setType(placeTypeID);
        place.setSubType(placeSubtypeID);
        Address address = new Address();
        address.setAddressCode(tambonCode);
        place.setAddress(address);
        Location location = new Location(this.location.latitude, this.location.longitude);
        place.setLocation(location);
        place.setUpdateBy(userRepository.findUserByName(updateBy));
        return place;
    }
}
