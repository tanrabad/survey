package th.or.nectec.tanrabad.survey.maps;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import th.or.nectec.tanrabad.survey.R;

public class LiteMapFragment {

    public static SupportMapFragment setupLiteMapFragment() {
        return setupLiteMapFragmentWithPosition(null);
    }

    public static SupportMapFragment setupLiteMapFragmentWithPosition(final LatLng position) {
        GoogleMapOptions googleMapOptions = new GoogleMapOptions();
        googleMapOptions.liteMode(true);
        googleMapOptions.mapType(GoogleMap.MAP_TYPE_SATELLITE);
        googleMapOptions.mapToolbarEnabled(false);

        final SupportMapFragment supportMapFragment = SupportMapFragment.newInstance(googleMapOptions);
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                if (position == null) {
                    LatLngBounds thailand = new LatLngBounds(new LatLng(5, 97), new LatLng(21, 105));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(thailand, 40));
                } else {
                    MarkerOptions marker = buildMarker(googleMap, supportMapFragment, position);
                    googleMap.addMarker(marker);
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 16));
                }
            }
        });
        return supportMapFragment;
    }

    @NonNull
    private static MarkerOptions buildMarker(GoogleMap googleMap, SupportMapFragment supportMapFragment, LatLng position) {
        int color = getColor(supportMapFragment, R.color.shock_pink);
        float hsv[] = new float[3];
        Color.colorToHSV(color, hsv);
        MarkerOptions marker = new MarkerOptions();
        marker.icon(BitmapDescriptorFactory.defaultMarker(hsv[0]));
        marker.position(position);
        googleMap.addMarker(marker);
        return marker;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static int getColor(SupportMapFragment supportMapFragment, @ColorRes int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            return supportMapFragment.getResources().getColor(color, supportMapFragment.getActivity().getTheme());
        else
            //noinspection deprecation
            return supportMapFragment.getResources().getColor(color);
    }
}
