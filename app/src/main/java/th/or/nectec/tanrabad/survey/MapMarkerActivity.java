package th.or.nectec.tanrabad.survey;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.model.LatLng;

import th.or.nectec.tanrabad.survey.maps.MapMarkerFragment;
import th.or.nectec.tanrabad.survey.utils.alert.Alert;

public class MapMarkerActivity extends TanrabadActivity implements View.OnClickListener {

    public static final String MAP_LOCATION = "map_location";
    MapMarkerFragment mapMarkerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_marker);
        assignViews();
        setupMap();
    }

    private void setupMap() {
        mapMarkerFragment = MapMarkerFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.map_container, mapMarkerFragment, MapMarkerFragment.FRAGMENT_TAG).commit();
    }

    private void assignViews() {
        Button removeLocation = (Button) findViewById(R.id.remove_location);
        removeLocation.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_save_map_marker_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_marker_menu:
                LatLng markedLocation = mapMarkerFragment.getMarkedLocation();
                if (markedLocation != null) {
                    sendMarkedLocationResult();
                } else {
                    Alert.highLevel().show(R.string.please_define_location);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendMarkedLocationResult() {
        Intent data = new Intent();
        data.putExtra(MAP_LOCATION, mapMarkerFragment.getMarkedLocation());
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.remove_location:
                mapMarkerFragment.removeMarkedLocation();
                break;
        }
    }
}
