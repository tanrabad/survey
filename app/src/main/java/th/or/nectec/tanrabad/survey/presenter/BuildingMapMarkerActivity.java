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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import th.or.nectec.tanrabad.entity.field.Location;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.presenter.maps.BuildingMapMarkerFragment;
import th.or.nectec.tanrabad.survey.presenter.maps.LocationUtils;
import th.or.nectec.tanrabad.survey.utils.alert.Alert;
import th.or.nectec.tanrabad.survey.utils.android.TwiceBackPressed;
import th.or.nectec.tanrabad.survey.utils.prompt.AlertDialogPromptMessage;
import th.or.nectec.tanrabad.survey.utils.prompt.PromptMessage;

import java.text.DecimalFormat;

public class BuildingMapMarkerActivity extends TanrabadActivity implements View.OnClickListener {

    public static final String PLACE_UUID = "place_uuid";
    public static final String BUILDING_LOCATION = "building_location";
    public static final int MARK_LOCATION_REQUEST_CODE = 50000;
    BuildingMapMarkerFragment buildingMapMarkerFragment;
    DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private TwiceBackPressed twiceBackPressed;

    public static void startAdd(Activity activity, String placeUUID) {
        Intent intent = new Intent(activity, BuildingMapMarkerActivity.class);
        intent.putExtra(BuildingMapMarkerActivity.PLACE_UUID, placeUUID);
        activity.startActivityForResult(intent, MARK_LOCATION_REQUEST_CODE);
    }

    public static void startEdit(Activity activity, String placeUUID, Location buildingLocation) {
        Intent intent = new Intent(activity, BuildingMapMarkerActivity.class);
        intent.putExtra(BuildingMapMarkerActivity.PLACE_UUID, placeUUID);
        intent.putExtra(BuildingMapMarkerActivity.BUILDING_LOCATION, LocationUtils.convertLocationToJson(buildingLocation));
        activity.startActivityForResult(intent, MARK_LOCATION_REQUEST_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_marker);
        setupHomeButton();
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
        Location buildingLocation = LocationUtils.convertJsonToLocation(getIntent().getStringExtra(BUILDING_LOCATION));
        if (buildingLocation == null) {
            buildingMapMarkerFragment = BuildingMapMarkerFragment.newInstance(getPlaceUUID());
        } else {
            if (getSupportActionBar() != null)
                getSupportActionBar().setTitle(R.string.edit_location);
            buildingMapMarkerFragment = BuildingMapMarkerFragment.newInstanceWithLocation(getPlaceUUID(), buildingLocation);
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.map_container, buildingMapMarkerFragment, BuildingMapMarkerFragment.FRAGMENT_TAG).commit();
    }

    public String getPlaceUUID() {
        return getIntent().getStringExtra(PLACE_UUID);
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
                Location markedLocation = buildingMapMarkerFragment.getMarkedLocation();
                if (markedLocation != null) {
                    if (buildingMapMarkerFragment.isDistanceBetweenPlaceAndBuildingExceed()) {
                        showPromptWhenPositionBetweenBuildingAndPlaceIsExceed();
                    } else {
                        sendMarkedLocationResult();
                    }
                } else {
                    Alert.highLevel().show(R.string.please_define_building_location);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showPromptWhenPositionBetweenBuildingAndPlaceIsExceed() {
        PromptMessage promptMessage = new AlertDialogPromptMessage(this);
        promptMessage.setOnConfirm(getString(R.string.confirm), new PromptMessage.OnConfirmListener() {
            @Override
            public void onConfirm() {
                sendMarkedLocationResult();
            }
        });
        promptMessage.setOnCancel(getString(R.string.cancel), null);
        promptMessage.show(getString(R.string.confirm_add_building_location), getDistanceBetweenBuildingAndPlaceMessage());
    }

    private void sendMarkedLocationResult() {
        Intent data = new Intent();
        data.putExtra(BUILDING_LOCATION, LocationUtils.convertLocationToJson(buildingMapMarkerFragment.getMarkedLocation()));
        setResult(RESULT_OK, data);
        finish();
    }

    private String getDistanceBetweenBuildingAndPlaceMessage() {
        double distanceBetweenBuildingAndPlaceInKm = buildingMapMarkerFragment.getDistanceBetweenPlaceAndBuilding() / 1000.f;
        return String.format(getString(R.string.distance_between_place_and_building),
                decimalFormat.format(distanceBetweenBuildingAndPlaceInKm));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.remove_location:
                buildingMapMarkerFragment.removeMarkedLocation();
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
