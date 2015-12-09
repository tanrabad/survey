package th.or.nectec.tanrabad.survey.presenter.maps;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

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
    private static LatLngBounds THAILAND = new LatLngBounds(new LatLng(5, 97),
            new LatLng(21, 105));
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

    private void setupMap() {
        googleMap = getMap();
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        googleMap.setOnCameraChangeListener(this);
        setLockedMap(isLocked);
        googleMap.getUiSettings().setZoomControlsEnabled(isZoomable);
    }

    public void setLockedMap(boolean isLocked) {
        googleMap.getUiSettings().setScrollGesturesEnabled(!isLocked);
        googleMap.getUiSettings().setMyLocationButtonEnabled(!isLocked);
    }

    private void setupLocationAPI() {
        locationApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
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

    private void moveToLastLocationOrThailand() {
        lastLocation = getLastLocation();
        if (lastLocation != null) {
            onMoveToLocation(lastLocation);
        } else {
            moveCameraToThailand(getActivity(), googleMap);
        }
    }

    public Location getLastLocation() {
        return LocationServices.FusedLocationApi.getLastLocation(locationApiClient);
    }

    public static void moveCameraToThailand(Activity activity, GoogleMap map) {
        if (map == null)
            return;

        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay()
                .getMetrics(metrics);
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;
        map.moveCamera(CameraUpdateFactory.newLatLngBounds(THAILAND, width,
                height, 32));
    }

    public void onMoveToLocation(Location location) {
        LatLng cur = new LatLng(location.getLatitude(), location.getLongitude());
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cur, 15));
    }

    private boolean isGpsEnabled() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Activity.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public void setLocationEnabled(boolean isLocationEnabled) {
        googleMap.setMyLocationEnabled(isLocationEnabled);
        if (isLocationEnabled)
            setupLocationUpdateService();
    }

    private void setupLocationUpdateService() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationRequest.setInterval(UPDATE_INTERVAL_MS);
        locationRequest.setFastestInterval(FASTEST_INTERVAL_MS);

        LocationServices.FusedLocationApi.requestLocationUpdates(
                locationApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int cause) {
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