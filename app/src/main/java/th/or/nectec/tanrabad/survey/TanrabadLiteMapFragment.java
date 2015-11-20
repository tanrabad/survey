package th.or.nectec.tanrabad.survey;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class TanrabadLiteMapFragment {

    public static SupportMapFragment setupLiteMapFragment() {
        return setupLiteMapFragmentWithPosition(null);
    }

    public static SupportMapFragment setupLiteMapFragmentWithPosition(final LatLng position) {
        GoogleMapOptions googleMapOptions = new GoogleMapOptions();
        googleMapOptions.liteMode(true);
        googleMapOptions.mapType(GoogleMap.MAP_TYPE_SATELLITE);
        googleMapOptions.mapToolbarEnabled(false);

        SupportMapFragment supportMapFragment = SupportMapFragment.newInstance(googleMapOptions);
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                if (position == null) {
                    LatLngBounds thailand = new LatLngBounds(new LatLng(5, 97), new LatLng(21, 105));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(thailand, 40));
                } else
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 16));
            }
        });
        return supportMapFragment;
    }
}
