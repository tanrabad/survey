package th.or.nectec.tanrabad.survey.presenter.maps;

import com.google.gson.Gson;

import th.or.nectec.tanrabad.entity.Location;

public class LocationUtils {
    public static String convertLocationToJson(Location location) {
        Gson gson = new Gson();
        return location == null ? null : gson.toJson(location);
    }

    public static Location convertJsonToLocation(String locationJson) {
        Gson gson = new Gson();
        return gson.fromJson(locationJson, Location.class);
    }
}
