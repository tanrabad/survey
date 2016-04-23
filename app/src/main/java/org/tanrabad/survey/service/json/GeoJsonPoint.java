package org.tanrabad.survey.service.json;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import th.or.nectec.tanrabad.entity.field.Location;

import java.util.ArrayList;
import java.util.List;

@JsonObject
public class GeoJsonPoint {

    @JsonField
    public String type;

    @JsonField
    public List<Double> coordinates;

    public static GeoJsonPoint parse(Location location) {
        GeoJsonPoint geoJsonPoint = new GeoJsonPoint();
        geoJsonPoint.type = "Point";
        geoJsonPoint.coordinates = new ArrayList<>();
        geoJsonPoint.coordinates.add(location.getLongitude());
        geoJsonPoint.coordinates.add(location.getLatitude());
        return geoJsonPoint;
    }

    public Location getEntity() {
        return new Location(coordinates.get(1), coordinates.get(0));
    }

    public Double getLatitude() {
        return coordinates.get(1);
    }

    public Double getLongitude() {
        return coordinates.get(0);
    }
}
