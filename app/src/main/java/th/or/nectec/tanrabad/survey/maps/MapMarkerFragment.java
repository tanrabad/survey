package th.or.nectec.tanrabad.survey.maps;

import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

public class MapMarkerFragment extends SupportMapFragment implements GoogleMap.OnMapLongClickListener, OnMapReadyCallback {

    public static final String FRAGMENT_TAG = "map_marker_fragment";
    public static final String ARGS_LOCATION = "args_location";
    LatLng location;

    public static MapMarkerFragment newInstance() {
        MapMarkerFragment mapMarkerFragment = new MapMarkerFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARGS_MOVE_TO_MY_LOCATION, true);
        mapMarkerFragment.setArguments(args);
        return mapMarkerFragment;
    }

    public static MapMarkerFragment newInstanceWithLocation(LatLng location) {
        MapMarkerFragment mapMarkerFragment = new MapMarkerFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARGS_MOVE_TO_MY_LOCATION, false);
        args.putParcelable(ARGS_LOCATION, location);
        mapMarkerFragment.setArguments(args);
        return mapMarkerFragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle args = getArguments();
        location = args.getParcelable(ARGS_LOCATION);
        getMapAsync(this);
        getMap().setOnMapLongClickListener(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (location != null) {
            addMarker(location);
            moveToLocation(location);
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        removeMarkedLocation();
        location = latLng;
        addMarker(location);
    }

    public LatLng getMarkedLocation() {
        return location;
    }

    public void removeMarkedLocation() {
        location = null;
        clearMap();
    }
}
