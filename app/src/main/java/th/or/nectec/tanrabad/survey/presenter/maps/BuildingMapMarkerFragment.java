/*
 * Copyright (c) 2016 NECTEC
 *   National Electronics and Computer Technology Center, Thailand
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package th.or.nectec.tanrabad.survey.presenter.maps;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.ColorUtils;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;
import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.entity.field.Location;
import th.or.nectec.tanrabad.entity.lookup.PlaceType;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.repository.BrokerBuildingRepository;
import th.or.nectec.tanrabad.survey.repository.BrokerPlaceRepository;
import th.or.nectec.tanrabad.survey.utils.MapUtils;
import th.or.nectec.tanrabad.survey.utils.android.ResourceUtils;

import java.util.List;
import java.util.UUID;

public class BuildingMapMarkerFragment extends MapMarkerFragment implements GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMarkerDragListener, OnMapReadyCallback {

    public static final String FRAGMENT_TAG = "building_map_marker_fragment";
    public static final int DISTANCE_LIMIT_IN_METER = 4000;
    private static Location buildingLocation;
    private Place place;
    private Marker placeMarker;
    private GoogleApiClient.ConnectionCallbacks locationServiceCallback = new GoogleApiClient.ConnectionCallbacks() {
        @Override
        public void onConnected(@Nullable Bundle bundle) {
            Location placeLocation = place.getLocation();
            if (placeLocation != null) {
                addPlaceMarker();
                addPlaceCircle();
                queryAndAddAnotherBuildingMarker();
                if (getMarkedLocation() == null) {
                    moveToLocation(LocationUtils.convertLocationToLatLng(placeLocation));
                }
            }
        }

        @Override
        public void onConnectionSuspended(int i) {

        }
    };

    public static BuildingMapMarkerFragment newInstance(String placeUUID) {
        BuildingMapMarkerFragment mapMarkerFragment = new BuildingMapMarkerFragment();
        mapMarkerFragment.setMoveToMyLocation(true);
        mapMarkerFragment.loadPlaceData(placeUUID);
        return mapMarkerFragment;
    }

    private void loadPlaceData(String placeUUID) {
        place = BrokerPlaceRepository.getInstance().findByUUID(UUID.fromString(placeUUID));
    }

    public static BuildingMapMarkerFragment newInstanceWithLocation(String placeUUID, Location buildingLocation) {
        BuildingMapMarkerFragment.buildingLocation = buildingLocation;
        BuildingMapMarkerFragment mapMarkerFragment = new BuildingMapMarkerFragment();
        mapMarkerFragment.setMoveToMyLocation(false);
        mapMarkerFragment.setMarkedLocation(buildingLocation);
        mapMarkerFragment.loadPlaceData(placeUUID);
        return mapMarkerFragment;
    }

    public void onStart() {
        super.onStart();
        playLocationService.addConnectionCallbacks(locationServiceCallback);
    }

    @Override
    public void onStop() {
        super.onStop();
        playLocationService.removeConnectionCallbacks(locationServiceCallback);
    }

    private void addPlaceMarker() {
        LatLng placePosition = LocationUtils.convertLocationToLatLng(place.getLocation());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.icon(MapUtils.getIconBitmapDescriptor(getActivity(), R.color.water_blue));
        markerOptions.position(placePosition);
        markerOptions.title(place.getName());
        placeMarker = googleMap.addMarker(markerOptions);
    }

    private void queryAndAddAnotherBuildingMarker() {
        List<Building> buildingsInPlaceList = BrokerBuildingRepository.getInstance().findByPlaceUUID(place.getId());
        if (buildingsInPlaceList == null)
            return;
        for (Building eachBuilding : buildingsInPlaceList) {
            if (!eachBuilding.getLocation().equals(buildingLocation))
                addAnotherBuildingMarker(eachBuilding);
        }
    }

    private void addAnotherBuildingMarker(Building building) {
        LatLng buildingPosition = LocationUtils.convertLocationToLatLng(building.getLocation());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.icon(MapUtils.getIconBitmapDescriptor(getActivity(), R.color.amber_500));
        markerOptions.position(buildingPosition);
        markerOptions.title(getBuildingPrefix() + building.getName());
        placeMarker = googleMap.addMarker(markerOptions);
    }

    private String getBuildingPrefix() {
        return place.getType() == PlaceType.VILLAGE_COMMUNITY ? "บ้านเลขที่" : "อาคาร";
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
        googleMap.addCircle(circleOptions);
    }

    public boolean isDistanceBetweenPlaceAndBuildingExceed() {
        return placeMarker != null && getDistanceBetweenPlaceAndBuilding() > DISTANCE_LIMIT_IN_METER;
    }

    public double getDistanceBetweenPlaceAndBuilding() {
        return SphericalUtil.computeDistanceBetween(placeMarker.getPosition(), marker.getPosition());
    }
}