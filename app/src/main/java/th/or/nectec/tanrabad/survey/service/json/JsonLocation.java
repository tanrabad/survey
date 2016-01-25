package th.or.nectec.tanrabad.survey.service.json;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import th.or.nectec.tanrabad.entity.field.Location;

@JsonObject
public class JsonLocation {

    @JsonField
    public Double latitude;

    @JsonField
    public Double longitude;

    public static JsonLocation parse(Location location) {
        JsonLocation jsonLocation = new JsonLocation();
        jsonLocation.latitude = location.getLatitude();
        jsonLocation.longitude = location.getLongitude();
        return jsonLocation;
    }

    public Location getEntity(){
        return new Location(latitude,longitude);
    }

    @Override
    public String toString() {
        return "{\"latitude\":" + latitude + ",\"longitude\":" + longitude + '}';
    }
}
