package th.or.nectec.tanrabad.survey.presenter.maps;

import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.utils.android.ResourceUtils;

public class BuildingMapMarkerFragment extends MapMarkerFragment implements GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerDragListener, OnMapReadyCallback {

    public static final String FRAGMENT_TAG = "building_map_marker_fragment";
    public static final String ARGS_PLACE_LOCATION = "args_place_location";
    Marker placeMarker;

    public static BuildingMapMarkerFragment newInstance(LatLng placeLocation) {
        BuildingMapMarkerFragment mapMarkerFragment = new BuildingMapMarkerFragment();
        Bundle args = new Bundle();

        if (placeLocation != null) {
            args.putBoolean(ARGS_MOVE_TO_MY_LOCATION, false);
            args.putParcelable(ARGS_PLACE_LOCATION, placeLocation);
        } else {
            args.putBoolean(ARGS_MOVE_TO_MY_LOCATION, true);
        }

        mapMarkerFragment.setArguments(args);
        return mapMarkerFragment;
    }

    public static BuildingMapMarkerFragment newInstanceWithLocation(LatLng placeLocation, LatLng location) {
        BuildingMapMarkerFragment mapMarkerFragment = new BuildingMapMarkerFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARGS_MOVE_TO_MY_LOCATION, false);
        args.putParcelable(ARGS_PLACE_LOCATION, placeLocation);
        args.putParcelable(ARGS_LOCATION, location);
        mapMarkerFragment.setArguments(args);
        return mapMarkerFragment;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        super.onMapReady(googleMap);
        LatLng placePosition = getArguments().getParcelable(ARGS_PLACE_LOCATION);
        if (placePosition != null) {
            addPlaceMarker(placePosition);
        }
    }

    private void addPlaceMarker(LatLng position) {
        placeMarker = addMarker(position, ResourceUtils.from(getActivity()).getColor(R.color.water_blue), false);
    }

}