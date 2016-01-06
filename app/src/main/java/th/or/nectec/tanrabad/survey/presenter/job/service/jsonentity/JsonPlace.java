package th.or.nectec.tanrabad.survey.presenter.job.service.jsonentity;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import th.or.nectec.tanrabad.domain.UserRepository;
import th.or.nectec.tanrabad.entity.Location;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.entity.utils.Address;

import java.util.UUID;

@JsonObject
public class JsonPlace {

    @JsonField(name = "place_id", typeConverter = UUIDConverter.class)
    public UUID placeID;

    @JsonField(name = "place_type_id")
    public int placeTypeID;

    @JsonField(name = "place_subtype_id")
    public int placeSubtypeID;

    @JsonField(name = "place_name")
    public String placeName;

    @JsonField(name = "tambon_code")
    public String tambonCode;

    @JsonField(name = "tambon_name")
    public String tambonName;

    @JsonField(name = "amphur_code")
    public String amphurCode;

    @JsonField(name = "amphur_name")
    public String amphurName;

    @JsonField(name = "province_code")
    public String provinceCode;

    @JsonField(name = "province_name")
    public String provinceName;

    @JsonField
    public GeoJsonPoint location;

    @JsonField(name = "update_by")
    public String updateBy;


    public static JsonPlace parse(Place place) {
        JsonPlace jsonPlace = new JsonPlace();
        jsonPlace.placeID = place.getId();
        jsonPlace.placeName = place.getName();
        jsonPlace.placeTypeID = place.getType();
        jsonPlace.placeSubtypeID = place.getSubType();
        jsonPlace.location = GeoJsonPoint.parse(place.getLocation());
        jsonPlace.tambonCode = place.getAddress().getAddressCode();
        jsonPlace.updateBy = place.getUpdateBy().getUsername();
        return jsonPlace;
    }

    public Place getEntity(UserRepository userRepository) {
        Place place = new Place(placeID, placeName);
        place.setType(placeTypeID);
        place.setSubType(placeSubtypeID);
        place.setAddress(getAddress());
        Location location = this.location == null ? null : this.location.getEntity();
        place.setLocation(location);
        place.setUpdateBy(userRepository.findUserByName(updateBy));
        return place;
    }

    private Address getAddress() {
        Address address = new Address();
        address.setAddressCode(tambonCode);
        address.setProvince(provinceName);
        address.setDistrict(amphurName);
        address.setSubdistrict(tambonName);
        return address;
    }
}
