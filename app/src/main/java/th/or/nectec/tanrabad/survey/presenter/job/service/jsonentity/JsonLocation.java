package th.or.nectec.tanrabad.survey.presenter.job.service.jsonentity;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import th.or.nectec.tanrabad.entity.Location;

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

    @Override
    public String toString() {
        return "{\"latitude\":" + latitude + ",\"longitude\":" + longitude + '}';
    }
}
