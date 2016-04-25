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

package org.tanrabad.survey.presenter.maps;

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
import org.tanrabad.survey.repository.BrokerBuildingRepository;
import org.tanrabad.survey.repository.BrokerPlaceRepository;
import org.tanrabad.survey.utils.android.ResourceUtils;
import org.tanrabad.survey.utils.map.MarkerUtil;
import org.tanrabad.survey.entity.Building;
import org.tanrabad.survey.entity.Place;
import org.tanrabad.survey.entity.field.Location;
import org.tanrabad.survey.entity.lookup.PlaceType;
import org.tanrabad.survey.R;

import java.util.List;
import java.util.UUID;

public class BuildingMapMarkerFragment extends MapMarkerFragment implements GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMarkerDragListener, OnMapReadyCallback {

    public static final String FRAGMENT_TAG = "building_map_marker_fragment";
    private static final int DISTANCE_LIMIT_IN_METER = 4000;
    private static String buildingUuid;
    private Place place;
    private Marker placeMarker;
    private GoogleApiClient.ConnectionCallbacks locationServiceCallback = new GoogleApiClient.ConnectionCallbacks() {
        @Override
        public void onConnected(@Nullable Bundle bundle) {
            Location placeLocation = place.getLocation();
            if (placeLocation != null) {
                addPlaceMarker();
                addPlaceCircle();
                if (getMarkedLocation() == null) {
                    moveToLocation(LocationUtils.convertLocationToLatLng(placeLocation));
                }
            }
            queryAndAddAnotherBuildingMarker();
        }

        @Override
        public void onConnectionSuspended(int i) {

        }
    };

    public static BuildingMapMarkerFragment newInstance(String placeUuid, String buildingUuid) {
        BuildingMapMarkerFragment mapMarkerFragment = new BuildingMapMarkerFragment();
        BuildingMapMarkerFragment.buildingUuid = buildingUuid;
        mapMarkerFragment.setMoveToMyLocation(true);
        mapMarkerFragment.loadPlaceData(placeUuid);
        return mapMarkerFragment;
    }

    private void loadPlaceData(String placeUuid) {
        place = BrokerPlaceRepository.getInstance().findByUuid(UUID.fromString(placeUuid));
    }

    public static BuildingMapMarkerFragment newInstanceWithLocation(
            String placeUuid, String buildingUuid, Location buildingLocation) {
        BuildingMapMarkerFragment.buildingUuid = buildingUuid;
        BuildingMapMarkerFragment mapMarkerFragment = new BuildingMapMarkerFragment();
        mapMarkerFragment.setMoveToMyLocation(false);
        mapMarkerFragment.setMarkedLocation(buildingLocation);
        mapMarkerFragment.loadPlaceData(placeUuid);
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
        MarkerOptions markerOptions = MarkerUtil.buildMarkerOption(placePosition, R.color.dark_blue, false);
        markerOptions.title(place.getName());
        placeMarker = googleMap.addMarker(markerOptions);
    }

    private void queryAndAddAnotherBuildingMarker() {
        List<Building> buildingsInPlaceList = BrokerBuildingRepository.getInstance().findByPlaceUuid(place.getId());
        if (buildingsInPlaceList == null)
            return;
        for (Building eachBuilding : buildingsInPlaceList) {
            if (!eachBuilding.getId().toString().equals(buildingUuid))
                addAnotherBuildingMarker(eachBuilding);
        }
    }

    private Marker addAnotherBuildingMarker(Building building) {
        LatLng buildingPosition = LocationUtils.convertLocationToLatLng(building.getLocation());
        MarkerOptions markerOptions = MarkerUtil.buildMarkerOption(buildingPosition, R.color.amber_500, false);
        markerOptions.title(getBuildingPrefix() + building.getName());
        return googleMap.addMarker(markerOptions);
    }

    private String getBuildingPrefix() {
        return place.getType() == PlaceType.VILLAGE_COMMUNITY ? "บ้านเลขที่ " : "อาคาร ";
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
