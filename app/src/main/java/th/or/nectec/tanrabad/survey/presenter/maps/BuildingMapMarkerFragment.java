package th.or.nectec.tanrabad.survey.presenter.maps;

import android.os.Bundle;
import android.support.v4.graphics.ColorUtils;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;

import java.util.UUID;

import th.or.nectec.tanrabad.entity.Location;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.repository.InMemoryPlaceRepository;
import th.or.nectec.tanrabad.survey.utils.MapUtils;
import th.or.nectec.tanrabad.survey.utils.android.ResourceUtils;

public class BuildingMapMarkerFragment extends MapMarkerFragment implements GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerDragListener, OnMapReadyCallback {

    public static final String FRAGMENT_TAG = "building_map_marker_fragment";
    public static final int DISTANCE_LIMIT_IN_METER = 4000;
    private Place place;
    private Marker placeMarker;

    public static BuildingMapMarkerFragment newInstance(String placeUUID) {
        BuildingMapMarkerFragment mapMarkerFragment = new BuildingMapMarkerFragment();
        mapMarkerFragment.setMoveToMyLocation(true);
        mapMarkerFragment.loadPlaceData(placeUUID);
        return mapMarkerFragment;
    }

    private void loadPlaceData(String placeUUID) {
        place = InMemoryPlaceRepository.getInstance().findPlaceByPlaceUUID(UUID.fromString(placeUUID));
    }

    public static BuildingMapMarkerFragment newInstanceWithLocation(String placeUUID, Location buildingLocation) {
        BuildingMapMarkerFragment mapMarkerFragment = new BuildingMapMarkerFragment();
        mapMarkerFragment.setMoveToMyLocation(false);
        mapMarkerFragment.setMarkedLocation(buildingLocation);
        mapMarkerFragment.loadPlaceData(placeUUID);
        return mapMarkerFragment;
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        super.onConnected(connectionHint);
        Location placeLocation = place.getLocation();
        if (placeLocation != null) {
            addPlaceMarker();
            addPlaceCircle();
            if (getMarkedLocation() == null) {
                moveToLocation(LocationUtils.convertLocationToLatLng(placeLocation));
            }
        }
    }

    private void addPlaceMarker() {
        LatLng placePosition = LocationUtils.convertLocationToLatLng(place.getLocation());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.icon(MapUtils.getIconBitmapDescriptor(getActivity(), R.color.water_blue));
        markerOptions.position(placePosition);
        markerOptions.title(place.getName());
        placeMarker = googleMap.addMarker(markerOptions);
    }

    private void addPlaceCircle() {
        LatLng placePosition = LocationUtils.convertLocationToLatLng(place.getLocation());
        int placeColor = ResourceUtils.from(getActivity()).getColor(R.color.water_blue);
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(placePosition);
        circleOptions.radius(DISTANCE_LIMIT_IN_METER);
        circleOptions.fillColor(ColorUtils.setAlphaComponent(placeColor, 40));
        circleOptions.strokeColor(placeColor);
        circleOptions.strokeWidth(1);
        getMap().addCircle(circleOptions);
    }

    public boolean isDistanceBetweenPlaceAndBuildingExceed() {
        return placeMarker != null && getDistanceBetweenPlaceAndBuilding() > DISTANCE_LIMIT_IN_METER;
    }

    public double getDistanceBetweenPlaceAndBuilding() {
        return SphericalUtil.computeDistanceBetween(placeMarker.getPosition(), marker.getPosition());
    }
}