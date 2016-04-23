package org.tanrabad.survey.presenter.maps;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import org.tanrabad.survey.entity.field.Location;


public class LocationUtils {
    public static String convertLocationToJson(Location location) {
        Gson gson = new Gson();
        return gson.toJson(location);
    }

    public static Location convertJsonToLocation(String locationJson) {
        Gson gson = new Gson();
        return gson.fromJson(locationJson, Location.class);
    }

    public static LatLng convertLocationToLatLng(Location location) {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    public static LatLng convertLocationToLatLng(android.location.Location location) {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }
}
