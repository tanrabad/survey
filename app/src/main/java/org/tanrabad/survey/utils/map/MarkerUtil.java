package org.tanrabad.survey.utils.map;

import android.support.annotation.ColorRes;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import org.tanrabad.survey.TanrabadApp;
import org.tanrabad.survey.presenter.maps.LocationUtils;
import org.tanrabad.survey.utils.MapUtils;
import org.tanrabad.survey.entity.field.Location;
import org.tanrabad.survey.R;

public class MarkerUtil {
    public static MarkerOptions buildMarkerOption(Location location) {
        LatLng position = LocationUtils.convertLocationToLatLng(location);
        return buildMarkerOption(position, R.color.pink, false);
    }

    public static MarkerOptions buildMarkerOption(LatLng location, @ColorRes int color, boolean draggable) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.draggable(draggable);
        markerOptions.icon(MapUtils.getIconBitmapDescriptor(TanrabadApp.getInstance(), color));
        markerOptions.position(location);
        return markerOptions;
    }

    public static MarkerOptions buildDragableMarkerOption(Location location) {
        LatLng position = LocationUtils.convertLocationToLatLng(location);
        return buildMarkerOption(position, R.color.pink, true);
    }

    public static MarkerOptions buildMarkerOption(LatLng location) {
        return buildMarkerOption(location, R.color.pink, false);
    }

    public static MarkerOptions buildDragableMarkerOption(LatLng location) {
        return buildMarkerOption(location, R.color.pink, true);
    }
}
