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
import android.support.annotation.ColorRes;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.utils.MapUtils;

public class MapMarkerFragment extends BaseMapFragment implements MapMarkerInterface, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerDragListener, OnMapReadyCallback {

    public static final String FRAGMENT_TAG = "map_marker_fragment";

    Marker marker;
    private th.or.nectec.tanrabad.entity.Location markedLocation;

    public static MapMarkerFragment newInstance() {
        MapMarkerFragment mapMarkerFragment = new MapMarkerFragment();
        mapMarkerFragment.setMoveToMyLocation(true);
        return mapMarkerFragment;
    }

    public static MapMarkerFragment newInstanceWithLocation(th.or.nectec.tanrabad.entity.Location buildingLocation) {
        MapMarkerFragment mapMarkerFragment = new MapMarkerFragment();
        mapMarkerFragment.setMoveToMyLocation(false);
        mapMarkerFragment.setMarkedLocation(buildingLocation);
        return mapMarkerFragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getMap().setOnMapLongClickListener(this);
        getMap().setOnMarkerDragListener(this);
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        super.onConnected(connectionHint);
        LatLng targetLocation;
        if (markedLocation != null) {
            targetLocation = LocationUtils.convertLocationToLatLng(markedLocation);
            marker = addDraggableMarker(targetLocation);
            moveToLocation(targetLocation);
        } else {
            Location lastLocation = getLastLocation();
            if (lastLocation != null && marker == null) {
                targetLocation = LocationUtils.convertLocationToLatLng(lastLocation);
                marker = addDraggableMarker(targetLocation);
                moveToLocation(targetLocation);
            }
        }
    }

    public Marker addDraggableMarker(LatLng position) {
        return addMarker(position, R.color.shock_pink, true);
    }

    protected Marker addMarker(LatLng position, @ColorRes int color, boolean draggable) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.draggable(draggable);
        markerOptions.icon(MapUtils.getIconBitmapDescriptor(getActivity(), color));
        markerOptions.position(position);
        Marker pinnedMarker = googleMap.addMarker(markerOptions);
        new MarkerDropInAnimator(this, pinnedMarker).start();
        return pinnedMarker;
    }


    @Override
    public void onMapLongClick(LatLng latLng) {
        removeMarkedLocation();
        marker = addDraggableMarker(latLng);
    }

    public void removeMarkedLocation() {
        if (marker == null)
            return;
        marker.remove();
        marker = null;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        getMap().getUiSettings().setScrollGesturesEnabled(false);
    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        getMap().getUiSettings().setScrollGesturesEnabled(true);
    }

    public th.or.nectec.tanrabad.entity.Location getMarkedLocation() {
        return marker == null ? null : new th.or.nectec.tanrabad.entity.Location(marker.getPosition().latitude, marker.getPosition().longitude);
    }

    @Override
    public void setMarkedLocation(th.or.nectec.tanrabad.entity.Location location) {
        markedLocation = location;
    }
}