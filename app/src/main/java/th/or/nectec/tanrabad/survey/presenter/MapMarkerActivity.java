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

package th.or.nectec.tanrabad.survey.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.model.LatLng;

import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.presenter.maps.MapMarkerFragment;
import th.or.nectec.tanrabad.survey.utils.alert.Alert;
import th.or.nectec.tanrabad.survey.utils.android.TwiceBackPressed;

public class MapMarkerActivity extends TanrabadActivity implements View.OnClickListener {

    public static final String MAP_LOCATION = "map_location";
    MapMarkerFragment mapMarkerFragment;
    private TwiceBackPressed twiceBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_marker);
        setupTwiceBackPressed();
        assignViews();
        setupMap();
    }

    private void setupTwiceBackPressed() {
        twiceBackPressed = new TwiceBackPressed(this);
    }

    private void assignViews() {
        Button removeLocation = (Button) findViewById(R.id.remove_location);
        removeLocation.setOnClickListener(this);
    }

    private void setupMap() {
        LatLng location = getIntent().getParcelableExtra(MAP_LOCATION);
        if (location == null) {
            mapMarkerFragment = MapMarkerFragment.newInstance();
        } else {
            if (getSupportActionBar() != null)
                getSupportActionBar().setTitle(R.string.edit_location);
            mapMarkerFragment = MapMarkerFragment.newInstanceWithLocation(location);
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.map_container, mapMarkerFragment, MapMarkerFragment.FRAGMENT_TAG).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_activity_map_marker, menu);
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

    @Override
    public void onBackPressed() {
        if (twiceBackPressed.onTwiceBackPressed()) {
            finish();
        }
    }
}
