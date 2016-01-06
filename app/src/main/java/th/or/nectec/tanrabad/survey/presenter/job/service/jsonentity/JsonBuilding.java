package th.or.nectec.tanrabad.survey.presenter.job.service.jsonentity;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.UUID;

import th.or.nectec.tanrabad.domain.UserRepository;
import th.or.nectec.tanrabad.domain.place.PlaceRepository;
import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.entity.Location;

@JsonObject
public class JsonBuilding {

    @JsonField(name = "building_id", typeConverter = UUIDConverter.class)
    public UUID buildingID;

    @JsonField(name = "place_id", typeConverter = UUIDConverter.class)
    public UUID placeID;

    @JsonField(name = "place_type_id")
    public int placeTypeID;

    @JsonField(name = "name")
    public String buildingName;

    @JsonField
    public JsonLocation location;

    @JsonField(name = "update_by")
    public String updateBy;

    public static JsonBuilding parse(Building building) {
        JsonBuilding jsonBuilding = new JsonBuilding();
        jsonBuilding.buildingID = building.getId();
        jsonBuilding.placeID = building.getPlace().getId();
        jsonBuilding.buildingName = building.getName();
        jsonBuilding.placeTypeID = building.getPlace().getType();
        jsonBuilding.location = JsonLocation.parse(building.getLocation());
        jsonBuilding.updateBy = building.getUpdateBy().getUsername();
        return jsonBuilding;
    }

    public Building getEntity(PlaceRepository placeRepository, UserRepository userRepository) {
        Building building = new Building(buildingID, buildingName);
        building.setPlace(placeRepository.findPlaceByUUID(placeID));
        Location location = this.location==null ? null : this.location.getEntity();
        building.setLocation(location);
        building.setUpdateBy(userRepository.findUserByName(updateBy));
        return building;
    }
}
