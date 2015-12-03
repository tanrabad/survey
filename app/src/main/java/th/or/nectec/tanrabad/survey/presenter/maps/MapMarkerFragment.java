package th.or.nectec.tanrabad.survey.presenter.maps;

import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class MapMarkerFragment extends TanrabadSupportMapFragment implements GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerDragListener, OnMapReadyCallback {

    public static final String FRAGMENT_TAG = "map_marker_fragment";
    public static final String ARGS_LOCATION = "args_location";
    LatLng location;
    Marker marker;

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
        getMap().setOnMarkerDragListener(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (location != null) {
            addDraggableMarker(location);
            moveToLocation(location);
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        super.onConnected(connectionHint);
        if (location != null)
            return;

        if (getCurrentLocation() != null) {
            location = new LatLng(getCurrentLocation().getLatitude(), getCurrentLocation().getLongitude());
            addMarker(location);
            moveToLocation(location);
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        removeMarkedLocation();
        location = latLng;
        addDraggableMarker(location);
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        getMap().getUiSettings().setScrollGesturesEnabled(false);
    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        location = marker.getPosition();
        getMap().getUiSettings().setScrollGesturesEnabled(true);
    }

    public LatLng getMarkedLocation() {
        return location;
    }

    public void removeMarkedLocation() {
        location = null;
        clearMap();
    }
}
