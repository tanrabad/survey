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

import android.graphics.Color;
import android.support.annotation.NonNull;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.utils.android.ResourceUtils;

public class LiteMapFragment {

    public static SupportMapFragment setupLiteMapFragment() {
        return setupLiteMapFragmentWithPosition(null);
    }

    public static SupportMapFragment setupLiteMapFragmentWithPosition(final LatLng position) {
        GoogleMapOptions googleMapOptions = new GoogleMapOptions();
        googleMapOptions.tiltGesturesEnabled(false);
        googleMapOptions.scrollGesturesEnabled(false);
        googleMapOptions.zoomGesturesEnabled(false);
        googleMapOptions.rotateGesturesEnabled(false);
        googleMapOptions.mapType(GoogleMap.MAP_TYPE_SATELLITE);
        googleMapOptions.mapToolbarEnabled(false);

        final SupportMapFragment supportMapFragment = SupportMapFragment.newInstance(googleMapOptions);
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                if (position == null) {
                    ThailandLocation.move(supportMapFragment.getActivity(), googleMap);
                } else {
                    moveMapByLocation(googleMap, supportMapFragment, position);
                }
            }
        });
        return supportMapFragment;
    }

    private static void moveMapByLocation(GoogleMap googleMap, SupportMapFragment supportMapFragment, LatLng position) {
        MarkerOptions marker = buildMarker(googleMap, supportMapFragment, position);
        googleMap.addMarker(marker);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 16));
    }

    @NonNull
    private static MarkerOptions buildMarker(GoogleMap googleMap, SupportMapFragment supportMapFragment, LatLng position) {
        int color = ResourceUtils.from(supportMapFragment.getActivity()).getColor(R.color.shock_pink);
        float hsv[] = new float[3];
        Color.colorToHSV(color, hsv);
        MarkerOptions marker = new MarkerOptions();
        marker.icon(BitmapDescriptorFactory.defaultMarker(hsv[0]));
        marker.position(position);
        googleMap.addMarker(marker);
        return marker;
    }


}
