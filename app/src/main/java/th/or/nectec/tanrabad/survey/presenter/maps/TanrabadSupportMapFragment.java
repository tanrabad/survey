package th.or.nectec.tanrabad.survey.presenter.maps;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.ColorRes;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.utils.alert.Alert;

public class TanrabadSupportMapFragment extends com.google.android.gms.maps.SupportMapFragment implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnCameraChangeListener {

    public static final String ARGS_LOCKED_MAP = "args_locked_map";
    public static final String ARGS_ZOOMABLE = "args_zoomable";
    public static final String ARGS_MYLOCATION_ENABLE = "args_mylocation_enable";
    public static final String ARGS_MOVE_TO_MY_LOCATION = "args_move_to_my_location";
    private static final String ARGS_LITE_MODE = "args_lite_mode";
    // Update frequency in milliseconds
    private static final long UPDATE_INTERVAL = 500;
    // A fast frequency ceiling in milliseconds
    private static final long FASTEST_INTERVAL = 100;
    public static LatLngBounds THAILAND = new LatLngBounds(new LatLng(5, 97),
            new LatLng(21, 105));
    OnPositionChangeListener mLocationListener;
    OnCurrentCameraChangeListener mCameraChangeListener;
    private GoogleMap googleMap;
    private Boolean isLocked = false;
    private Boolean isZoomable = false;
    private Boolean isMoveToMyLocation = false;
    private Boolean isGPSDialogShowed = false;
    private Boolean isLocationEnabled = true;
    private Location mCurrentLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    public TanrabadSupportMapFragment() {
        super();
    }

    public static TanrabadSupportMapFragment newInstance() {
        return newInstance(false, true, true, false);
    }

    public static TanrabadSupportMapFragment newInstance(boolean isLocked, boolean isZoomable, boolean isLocationEnabled, boolean isMovetoMyLocation) {
        TanrabadSupportMapFragment mapFrag = new TanrabadSupportMapFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARGS_LOCKED_MAP, isLocked);
        args.putBoolean(ARGS_MYLOCATION_ENABLE, isLocationEnabled);
        args.putBoolean(ARGS_MOVE_TO_MY_LOCATION, isMovetoMyLocation);
        args.putBoolean(ARGS_ZOOMABLE, isZoomable);
        mapFrag.setArguments(args);
        return mapFrag;

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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            isLocked = args.getBoolean(ARGS_LOCKED_MAP, false);
            isZoomable = args.getBoolean(ARGS_ZOOMABLE, true);
            isLocationEnabled = args.getBoolean(ARGS_MYLOCATION_ENABLE, true);
            isMoveToMyLocation = args.getBoolean(ARGS_MOVE_TO_MY_LOCATION, isLocationEnabled);
        }

        googleMap = getMap();
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        googleMap.setOnCameraChangeListener(this);

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        setLockedMap(isLocked);
        googleMap.getUiSettings().setZoomControlsEnabled(isZoomable);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
    }

    public void setMoveToMyLocation(boolean isMoveToMyLocation) {
        this.isMoveToMyLocation = isMoveToMyLocation;
    }

    public void addMarker(LatLng position) {
        addMarker(position, getColor(R.color.shock_pink));
    }

    public void addMarker(LatLng position, int color) {
        float hsv[] = new float[3];
        Color.colorToHSV(color, hsv);

        MarkerOptions marker = new MarkerOptions();
        marker.icon(BitmapDescriptorFactory.defaultMarker(hsv[0]));
        marker.position(position);
        googleMap.addMarker(marker);
    }

    public void clearMap() {
        googleMap.clear();
    }

    public void setLockedMap(boolean isLocked) {
        if (isLocked) {
            googleMap.getUiSettings().setScrollGesturesEnabled(false);
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        } else {
            googleMap.getUiSettings().setScrollGesturesEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        }
    }

    public void setLocationEnabled(boolean isLocationEnabled) {
        googleMap.setMyLocationEnabled(isLocationEnabled);

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    /**
     * @return current location from GooglePlay Service Location Client, May
     * null if Can't connect the service
     * @since 1.0
     */
    public Location getCurrentLocation() {
        return mCurrentLocation;
    }

    public void setMoveToMyLocationEnabled(boolean enabled) {
        isMoveToMyLocation = enabled;
    }

    @Override
    public void onConnectionSuspended(int cause) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult arg0) {
        Alert.highLevel().show("ไม่สามารถเชื่อมต่อ Google Play Services ได้");
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        setLocationEnabled(isLocationEnabled);

        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        if (mCurrentLocation != null && isMoveToMyLocation) {
            onMoveToLocation(mCurrentLocation);
        } else {
            if (isMoveToMyLocation) {
                if (!isGPSDialogShowed) {
                    showGpsSettingsDialog();
                    isGPSDialogShowed = true;
                }
                moveCameraToThailand(getActivity(), googleMap);
            }
        }
    }

    /**
     * Show gps setting dialog when gps disable
     */
    private void showGpsSettingsDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("GPS ยังไม่ได้เปิดการใช้งาน");
        alertDialog.setMessage("คุณต้องการเปิดการตั้งค่าการใช้งาน GPS หรือไม่");
        alertDialog.setPositiveButton("เปิดการตั้งค่า GPS",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                });
        alertDialog.setNegativeButton("ยกเลิก", null);
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
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

    public void onMoveToLocation(Location location) {
        LatLng cur = new LatLng(location.getLatitude(), location.getLongitude());
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cur, 15));
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

    @TargetApi(Build.VERSION_CODES.M)
    private int getColor(@ColorRes int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            return getResources().getColor(color, getContext().getTheme());
        else
            //noinspection deprecation
            return getResources().getColor(color);
    }

    public interface OnPositionChangeListener {
        void onPositionChange(LatLng position);
    }

    public interface OnCurrentCameraChangeListener {
        void setOnCameraChangeListener(CameraPosition position);
    }
}
