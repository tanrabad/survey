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

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import th.or.nectec.tanrabad.survey.utils.GpsUtils;
import th.or.nectec.tanrabad.survey.utils.LocationPermissionPrompt;
import th.or.nectec.tanrabad.survey.utils.PlayLocationService;

@SuppressLint("ValidFragment")
class BaseMapFragment extends com.google.android.gms.maps.SupportMapFragment
        implements MapFragmentInterface, OnMapReadyCallback {

    protected GoogleMap googleMap;
    protected PlayLocationService playLocationService = PlayLocationService.getInstance();
    private Boolean isLocked = false;
    private Boolean isZoomable = false;
    private Boolean isMoveToMyLocation = false;
    private Boolean isGpsDialogShowed = false;
    private Boolean isMyLocationEnabled = true;
    private boolean isMyLocationButtonEnabled = true;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getMapAsync(this);
    }


    @Override
    public void onStart() {
        super.onStart();
        playLocationService.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        playLocationService.disconnect();
    }

    public void moveToLocation(LatLng position) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        setupMap();
        ThailandLocation.move(getActivity(), googleMap);

        if (!GpsUtils.isGpsEnabled(getContext()) && !isGpsDialogShowed) {
            GpsUtils.showGpsSettingsDialog(getContext());
            isGpsDialogShowed = true;
        }

        setMyLocationEnabled(isMyLocationEnabled);
        if (isMoveToMyLocation) {
            moveToLastLocation();
        }
    }

    private void setupMap() {
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        UiSettings googleMapUiSettings = googleMap.getUiSettings();
        googleMapUiSettings.setScrollGesturesEnabled(!isLocked);
        googleMapUiSettings.setMyLocationButtonEnabled(isMyLocationButtonEnabled);
        googleMapUiSettings.setZoomControlsEnabled(isZoomable);
    }

    public void setMyLocationEnabled(boolean isLocationEnabled) {
        try {
            googleMap.setMyLocationEnabled(isLocationEnabled);
        } catch (SecurityException securityException) {
            LocationPermissionPrompt.show(getActivity());
        }
    }

    private void moveToLastLocation() {
        Location myLocation = playLocationService.getLastKnowLocation();
        if (myLocation != null)
            moveToLocation(myLocation);
    }

    public void moveToLocation(Location location) {
        LatLng cur = new LatLng(location.getLatitude(), location.getLongitude());
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cur, 15));
    }

    @Override
    public void setMapCanScrolled(boolean isCanScroll) {
        isLocked = !isCanScroll;
    }

    @Override
    public void setMoveToMyLocation(boolean isMoved) {
        isMoveToMyLocation = isMoved;
    }

    @Override
    public void setMapZoomable(boolean isZoomable) {
        this.isZoomable = isZoomable;
    }

    @Override
    public void setShowMyLocation(boolean isShowMyLocation) {
        this.isMyLocationEnabled = isShowMyLocation;
    }

    @Override
    public void setMoveToMyLocationButtonEnabled(boolean isMyLocationButtonEnabled) {
        this.isMyLocationButtonEnabled = isMyLocationButtonEnabled;
    }
}