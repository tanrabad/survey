package th.or.nectec.tanrabad.survey.maps;

import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

public class MapMarkerFragment extends SupportMapFragment implements GoogleMap.OnMapLongClickListener {

    LatLng position;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getMap().setOnMapLongClickListener(this);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        position = latLng;
        addMarker(latLng);
    }

    public LatLng getPosition() {
        return position;
    }
}
