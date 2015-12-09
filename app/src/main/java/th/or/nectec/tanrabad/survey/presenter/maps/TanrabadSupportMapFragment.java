/*
 * Copyright (c) 2015 NECTEC
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

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.utils.alert.Alert;
import th.or.nectec.tanrabad.survey.utils.prompt.AlertDialogPromptMessage;
import th.or.nectec.tanrabad.survey.utils.prompt.PromptMessage;

public class TanrabadSupportMapFragment extends com.google.android.gms.maps.SupportMapFragment implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnCameraChangeListener {

    public static final String ARGS_LOCKED_MAP = "args_locked_map";
    public static final String ARGS_ZOOMABLE = "args_zoomable";
    public static final String ARGS_MYLOCATION_ENABLE = "args_mylocation_enable";
    public static final String ARGS_MOVE_TO_MY_LOCATION = "args_move_to_my_location";

    private static final long UPDATE_INTERVAL_MS = 500;
    private static final long FASTEST_INTERVAL_MS = 100;

    protected GoogleMap googleMap;
    private OnPositionChangeListener mLocationListener;
    private OnCurrentCameraChangeListener mCameraChangeListener;
    private Boolean isLocked = false;
    private Boolean isZoomable = false;
    private Boolean isMoveToMyLocation = false;
    private Boolean isGPSDialogShowed = false;
    private Boolean isLocationEnabled = true;
    private Location lastLocation;
    private GoogleApiClient locationApiClient;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupMapOption();
        setupMap();
        setupLocationAPI();
    }

    private void setupMapOption() {
        Bundle args = getArguments();
        if (args != null) {
            isLocked = args.getBoolean(ARGS_LOCKED_MAP, false);
            isZoomable = args.getBoolean(ARGS_ZOOMABLE, true);
            isLocationEnabled = args.getBoolean(ARGS_MYLOCATION_ENABLE, true);
            isMoveToMyLocation = args.getBoolean(ARGS_MOVE_TO_MY_LOCATION, isLocationEnabled);
        }
    }

    private void setupMap() {
        googleMap = getMap();
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        googleMap.setOnCameraChangeListener(this);
        setLockedMap(isLocked);
        googleMap.getUiSettings().setZoomControlsEnabled(isZoomable);
    }

    private void setupLocationAPI() {
        locationApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    public void setLockedMap(boolean isLocked) {
        googleMap.getUiSettings().setScrollGesturesEnabled(!isLocked);
        googleMap.getUiSettings().setMyLocationButtonEnabled(!isLocked);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (locationApiClient != null) {
            locationApiClient.connect();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (locationApiClient != null) {
            locationApiClient.disconnect();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult arg0) {
        Alert.highLevel().show("ไม่สามารถเชื่อมต่อ Google Play Services ได้");
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        setLocationEnabled(isLocationEnabled);

        if (isMoveToMyLocation) {
            moveToLastLocationOrThailand();
        }

        if (!isGpsEnabled()) {
            showGpsSettingsDialog();
        }
    }

    public void setLocationEnabled(boolean isLocationEnabled) {
        googleMap.setMyLocationEnabled(isLocationEnabled);
        if (isLocationEnabled)
            setupLocationUpdateService();
    }

    private void moveToLastLocationOrThailand() {
        lastLocation = getLastLocation();
        if (lastLocation != null) {
            onMoveToLocation(lastLocation);
        } else {
            ThailandLocation.move(getActivity(), googleMap);
        }
    }

    private boolean isGpsEnabled() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Activity.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * Show gps setting dialog when gps disable
     */
    private void showGpsSettingsDialog() {
        if (!isGPSDialogShowed) {
            PromptMessage promptMessage = new AlertDialogPromptMessage(getActivity());
            promptMessage.setOnConfirm(getString(R.string.enable_gps), new PromptMessage.OnConfirmListener() {
                @Override
                public void onConfirm() {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            promptMessage.setOnCancel(getResources().getString(R.string.cancel), null);
            promptMessage.show(getString(R.string.gps_dialog_tilte), getString(R.string.gps_dialog_message));
            isGPSDialogShowed = true;
        }
    }

    private void setupLocationUpdateService() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationRequest.setInterval(UPDATE_INTERVAL_MS);
        locationRequest.setFastestInterval(FASTEST_INTERVAL_MS);

        LocationServices.FusedLocationApi.requestLocationUpdates(
                locationApiClient, locationRequest, this);
    }

    public Location getLastLocation() {
        return LocationServices.FusedLocationApi.getLastLocation(locationApiClient);
    }

    public void onMoveToLocation(Location location) {
        LatLng cur = new LatLng(location.getLatitude(), location.getLongitude());
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cur, 15));
    }

    @Override
    public void onConnectionSuspended(int cause) {
    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
        LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
        if (mLocationListener != null) {
            mLocationListener.onPositionChange(position);
        }
    }

    @Override
    public void onCameraChange(CameraPosition position) {
        if (mCameraChangeListener != null) {
            mCameraChangeListener.setOnCameraChangeListener(position);
        }
    }

    public void moveToLocation(LatLng position) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
    }

    public void setOnPositionChangeListener(OnPositionChangeListener listener) {
        mLocationListener = listener;
    }

    public void setOnCameraChangeListener(OnCurrentCameraChangeListener listener) {
        mCameraChangeListener = listener;
    }

    public interface OnPositionChangeListener {
        void onPositionChange(LatLng position);
    }

    public interface OnCurrentCameraChangeListener {
        void setOnCameraChangeListener(CameraPosition position);
    }
}