package th.or.nectec.tanrabad.survey.service.json;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import org.joda.time.DateTimeZone;
import th.or.nectec.tanrabad.domain.UserRepository;
import th.or.nectec.tanrabad.domain.place.PlaceRepository;
import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.entity.Location;

import java.util.UUID;

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
    public GeoJsonPoint location;

    @JsonField(name = "update_by")
    public String updateBy;

    @JsonField(name = "update_timestamp")
    public String updateTime;

    public static JsonBuilding parse(Building building) {
        JsonBuilding jsonBuilding = new JsonBuilding();
        jsonBuilding.buildingID = building.getId();
        jsonBuilding.placeID = building.getPlace().getId();
        jsonBuilding.buildingName = building.getName();
        jsonBuilding.placeTypeID = building.getPlace().getType();
        jsonBuilding.location = GeoJsonPoint.parse(building.getLocation());
        jsonBuilding.updateBy = building.getUpdateBy().getUsername();
        jsonBuilding.updateTime = building.getUpdateTimestamp().withZone(DateTimeZone.UTC).toString();
        return jsonBuilding;
    }

    public Building getEntity(PlaceRepository placeRepository, UserRepository userRepository) {
        Building building = new Building(buildingID, buildingName);
        building.setPlace(placeRepository.findPlaceByUUID(placeID));
        Location location = this.location==null ? null : this.location.getEntity();
        building.setLocation(location);
        building.setUpdateBy(userRepository.findUserByName(updateBy));
        building.setUpdateTimestamp(updateTime);
        return building;
    }
}
