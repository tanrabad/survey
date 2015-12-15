package th.or.nectec.tanrabad.survey.presenter.maps;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.utils.MapUtils;

public class MapMarkerFragment extends BaseMapFragment implements MapMarkerInterface, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerDragListener, OnMapReadyCallback {

    public static final String FRAGMENT_TAG = "map_marker_fragment";

    Marker marker;
    private th.or.nectec.tanrabad.entity.Location markedLocation;
    private th.or.nectec.tanrabad.entity.Location fixedLocation;

    public static MapMarkerFragment newInstance() {
        MapMarkerFragment mapMarkerFragment = new MapMarkerFragment();
        mapMarkerFragment.setMoveToMyLocation(true);
        return mapMarkerFragment;
    }

    public static MapMarkerFragment newInstanceWithLocation(th.or.nectec.tanrabad.entity.Location buildingLocation) {
        MapMarkerFragment mapMarkerFragment = new MapMarkerFragment();
        mapMarkerFragment.setMoveToMyLocation(false);
        mapMarkerFragment.setMarkedLocation(buildingLocation);
        return mapMarkerFragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getMapAsync(this);
        getMap().setOnMapLongClickListener(this);
        getMap().setOnMarkerDragListener(this);
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        super.onConnected(connectionHint);
        LatLng targetLocation;
        if (markedLocation != null) {
            targetLocation = new LatLng(markedLocation.getLatitude(), markedLocation.getLongitude());
            marker = addDraggableMarker(targetLocation);
            moveToLocation(targetLocation);
        } else {
            Location lastLocation = getLastLocation();
            if (lastLocation != null) {
                targetLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                marker = addDraggableMarker(targetLocation);
                moveToLocation(targetLocation);
            }
        }
    }

    public Marker addDraggableMarker(LatLng position) {
        return addMarker(position, R.color.shock_pink, true);
    }

    protected Marker addMarker(LatLng position, @ColorRes int color, boolean draggable) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.draggable(draggable);
        markerOptions.icon(MapUtils.getIconBitmapDescriptor(getActivity(), color));
        markerOptions.position(position);
        return googleMap.addMarker(markerOptions);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        removeMarkedLocation();
        marker = addDraggableMarker(latLng);
    }

    public void removeMarkedLocation() {
        if (marker == null)
            return;

        marker.remove();
        marker = null;
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
        getMap().getUiSettings().setScrollGesturesEnabled(true);
    }

    @Override
    public void setFixedLocation(th.or.nectec.tanrabad.entity.Location location) {
        fixedLocation = location;
    }

    public th.or.nectec.tanrabad.entity.Location getMarkedLocation() {
        Log.d("marker", marker.getPosition().toString());
        return marker == null ? null : new th.or.nectec.tanrabad.entity.Location(marker.getPosition().latitude, marker.getPosition().longitude);
    }

    @Override
    public void setMarkedLocation(th.or.nectec.tanrabad.entity.Location location) {
        markedLocation = location;
    }
}