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
import th.or.nectec.tanrabad.entity.Location;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.repository.InMemoryPlaceRepository;
import th.or.nectec.tanrabad.survey.utils.MapUtils;
import th.or.nectec.tanrabad.survey.utils.android.ResourceUtils;

import java.util.UUID;

public class BuildingMapMarkerFragment extends MapMarkerFragment implements GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerDragListener, OnMapReadyCallback {

    public static final String FRAGMENT_TAG = "building_map_marker_fragment";
    public static final String ARGS_PLACE_UUID = "args_place_location";
    public static final int DISTANCE_LIMIT_IN_METER = 4000;
    private Place place;
    private Marker placeMarker;

    public static BuildingMapMarkerFragment newInstance(String placeUUID) {
        BuildingMapMarkerFragment mapMarkerFragment = new BuildingMapMarkerFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARGS_MOVE_TO_MY_LOCATION, true);
        args.putString(ARGS_PLACE_UUID, placeUUID);
        mapMarkerFragment.setArguments(args);
        return mapMarkerFragment;
    }

    public static BuildingMapMarkerFragment newInstanceWithLocation(String placeUUID, LatLng buildingLocation) {
        BuildingMapMarkerFragment mapMarkerFragment = new BuildingMapMarkerFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARGS_MOVE_TO_MY_LOCATION, false);
        args.putString(ARGS_PLACE_UUID, placeUUID);
        args.putParcelable(ARGS_LOCATION, buildingLocation);
        mapMarkerFragment.setArguments(args);
        return mapMarkerFragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadPlaceData();
    }

    private void loadPlaceData() {
        String placeUUID = getArguments().getString(ARGS_PLACE_UUID);
        place = InMemoryPlaceRepository.getInstance().findPlaceByPlaceUUID(UUID.fromString(placeUUID));
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        super.onConnected(connectionHint);
        Location placeLocation = place.getLocation();
        if (placeLocation != null) {
            addPlaceLocation(placeLocation);
        }
    }

    private void addPlaceLocation(Location placeLocation) {
        addPlaceMarker(place);
        addPlaceCircle(place);
        moveToLocation(new LatLng(placeLocation.getLatitude(), placeLocation.getLongitude()));
    }

    private void addPlaceMarker(Place place) {
        LatLng placePosition = new LatLng(place.getLocation().getLatitude(), place.getLocation().getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.icon(MapUtils.getIconBitmapDescriptor(getActivity(), R.color.water_blue));
        markerOptions.position(placePosition);
        markerOptions.title(place.getName());
        placeMarker = googleMap.addMarker(markerOptions);
    }


    private void addPlaceCircle(Place place) {
        LatLng placePosition = new LatLng(place.getLocation().getLatitude(), place.getLocation().getLongitude());
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