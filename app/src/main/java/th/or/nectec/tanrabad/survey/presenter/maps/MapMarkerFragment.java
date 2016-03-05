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

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import th.or.nectec.tanrabad.survey.utils.MapUtils;
import th.or.nectec.tanrabad.survey.utils.map.MarkerUtil;

public class MapMarkerFragment extends BaseMapFragment implements MapMarkerInterface, GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMarkerDragListener, OnMapReadyCallback {

    public static final String FRAGMENT_TAG = "map_marker_fragment";

    Marker marker;
    private th.or.nectec.tanrabad.entity.field.Location markedLocation;
    private GoogleApiClient.ConnectionCallbacks locationServiceCallback = new GoogleApiClient.ConnectionCallbacks() {
        @Override
        public void onConnected(@Nullable Bundle bundle) {
            if (markedLocation != null) {
                marker = googleMap.addMarker(MarkerUtil.buildDragableMarkerOption(markedLocation));
                googleMap.moveCamera(MapUtils.locationZoom(markedLocation, 15));
            } else {
                Location lastLocation = playLocationService.getLastKnowLocation();
                if (lastLocation != null && marker == null) {
                    LatLng latLng = LocationUtils.convertLocationToLatLng(lastLocation);
                    marker = googleMap.addMarker(MarkerUtil.buildDragableMarkerOption(latLng));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                }
            }
        }

        @Override
        public void onConnectionSuspended(int i) {

        }
    };

    public static MapMarkerFragment newInstance() {
        MapMarkerFragment mapMarkerFragment = new MapMarkerFragment();
        mapMarkerFragment.setMoveToMyLocation(true);
        return mapMarkerFragment;
    }

    public static MapMarkerFragment newInstanceWithLocation(th.or.nectec.tanrabad.entity.field.Location location) {
        MapMarkerFragment mapMarkerFragment = new MapMarkerFragment();
        mapMarkerFragment.setMoveToMyLocation(false);
        mapMarkerFragment.setMarkedLocation(location);
        return mapMarkerFragment;
    }

    @Override
    public void onStop() {
        super.onStop();
        playLocationService.removeConnectionCallbacks(locationServiceCallback);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        super.onMapReady(googleMap);
        googleMap.setOnMapLongClickListener(this);
        googleMap.setOnMarkerDragListener(this);
        playLocationService.addConnectionCallbacks(locationServiceCallback);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        removeMarkedLocation();
        marker = googleMap.addMarker(MarkerUtil.buildDragableMarkerOption(latLng));
        new MarkerDropInAnimator(this, marker).start();
    }

    public void removeMarkedLocation() {
        if (marker == null)
            return;
        marker.remove();
        marker = null;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        googleMap.getUiSettings().setScrollGesturesEnabled(false);
    }

    @Override
    public void onMarkerDrag(Marker marker) {
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        googleMap.getUiSettings().setScrollGesturesEnabled(true);
    }

    public th.or.nectec.tanrabad.entity.field.Location getMarkedLocation() {
        return marker == null ? null : new th.or.nectec.tanrabad.entity.field.Location(marker.getPosition().latitude,
                marker.getPosition().longitude);
    }

    @Override
    public void setMarkedLocation(th.or.nectec.tanrabad.entity.field.Location location) {
        markedLocation = location;
    }
}