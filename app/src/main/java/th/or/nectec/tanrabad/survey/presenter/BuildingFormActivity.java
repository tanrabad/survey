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
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.UUID;

import th.or.nectec.tanrabad.domain.building.BuildingController;
import th.or.nectec.tanrabad.domain.building.BuildingPresenter;
import th.or.nectec.tanrabad.domain.building.BuildingSavePresenter;
import th.or.nectec.tanrabad.domain.building.BuildingSaver;
import th.or.nectec.tanrabad.domain.place.PlaceController;
import th.or.nectec.tanrabad.domain.place.PlacePresenter;
import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.entity.Location;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.presenter.maps.LiteMapFragment;
import th.or.nectec.tanrabad.survey.repository.InMemoryBuildingRepository;
import th.or.nectec.tanrabad.survey.repository.InMemoryPlaceRepository;
import th.or.nectec.tanrabad.survey.utils.alert.Alert;
import th.or.nectec.tanrabad.survey.utils.android.SoftKeyboard;
import th.or.nectec.tanrabad.survey.utils.android.TwiceBackPressed;
import th.or.nectec.tanrabad.survey.validator.SaveBuildingValidator;
import th.or.nectec.tanrabad.survey.validator.ValidatorException;

public class BuildingFormActivity extends TanrabadActivity implements PlacePresenter, BuildingPresenter, BuildingSavePresenter, View.OnClickListener {

    public static final String PLACE_UUID_ARG = "place_uuid_arg";
    public static final String BUILDING_UUID_ARG = "building_uuid_arg";

    public static final int ADD_BUILDING_REQ_CODE = 40000;
    public static final int MARK_LOCATION_REQUEST_CODE = 50000;


    private TextView placeName;
    private Toolbar toolbar;
    private TextView buildingNameTitle;
    private EditText buildingNameView;
    private FrameLayout addLocationBackground;
    private LatLng buildingLocation;
    private PlaceController placeController = new PlaceController(InMemoryPlaceRepository.getInstance(), this);
    private BuildingController buildingController = new BuildingController(InMemoryBuildingRepository.getInstance(), this);
    private BuildingSaver buildingSaver = new BuildingSaver(InMemoryBuildingRepository.getInstance(), new SaveBuildingValidator(), this);

    private Place place;
    private Building building;
    private Button editLocationButton;
    private TwiceBackPressed twiceBackPressed;

    public static void startEdit(Activity activity, String placeUUID, String buildingUUID) {
        Intent intent = new Intent(activity, BuildingFormActivity.class);
        intent.putExtra(PLACE_UUID_ARG, placeUUID);
        intent.putExtra(BuildingFormActivity.BUILDING_UUID_ARG, buildingUUID);
        activity.startActivityForResult(intent, ADD_BUILDING_REQ_CODE);
    }

    public static void startAdd(Activity activity, String placeUUID) {
        Intent intent = new Intent(activity, BuildingFormActivity.class);
        intent.putExtra(PLACE_UUID_ARG, placeUUID);
        activity.startActivityForResult(intent, ADD_BUILDING_REQ_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_form);
        assignViews();

        setupTwiceBackPressed();
        setSupportActionBar(toolbar);
        placeController.showPlace(UUID.fromString(getPlaceUUID()));
        loadBuildingData();
    }

    private void setupTwiceBackPressed() {
        twiceBackPressed = new TwiceBackPressed(this);
    }

    private void assignViews() {
        placeName = (TextView) findViewById(R.id.place_name);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        buildingNameTitle = (TextView) findViewById(R.id.building_name_label);
        buildingNameView = (EditText) findViewById(R.id.building_name);
        addLocationBackground = (FrameLayout) findViewById(R.id.add_location_background);
        editLocationButton = (Button) findViewById(R.id.edit_location);
        editLocationButton.setVisibility(View.GONE);
        Button addMarkerButton = (Button) findViewById(R.id.add_marker);
        addMarkerButton.setOnClickListener(this);
    }

    private String getPlaceUUID() {
        return getIntent().getStringExtra(PLACE_UUID_ARG);
    }

    private void loadBuildingData() {
        if (TextUtils.isEmpty(getBuildingUUID())) {
            setupPreviewMap();
            building = Building.withName(null);
        } else {
            buildingController.showBuilding(UUID.fromString(getBuildingUUID()));
        }
    }

    private String getBuildingUUID() {
        return getIntent().getStringExtra(BUILDING_UUID_ARG);
    }

    private void setupPreviewMap() {
        SupportMapFragment supportMapFragment = LiteMapFragment.setupLiteMapFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.map_container, supportMapFragment).commit();
    }

    @Override
    public void displayPlace(Place place) {
        this.place = place;
        placeName.setText(place.getName());
        if (place.getType() == Place.TYPE_VILLAGE_COMMUNITY) {
            buildingNameTitle.setText(R.string.house_no);
        } else {
            buildingNameTitle.setText(R.string.building_name);
            buildingNameView.setHint(R.string.touch_to_type_building_name);
        }
    }

    @Override
    public void alertPlaceNotFound() {
        Alert.highLevel().show(R.string.place_not_found);
    }

    @Override
    public void displayBuilding(Building building) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(R.string.building_edit);
        this.building = building;
        buildingNameView.setText(this.building.getName());

        if (this.building.getLocation() == null) {
            setupPreviewMap();
        } else {
            double latitiude = this.building.getLocation().getLatitude();
            double longitude = this.building.getLocation().getLongitude();
            setupPreviewMapWithPosition(new LatLng(latitiude, longitude));
        }
    }

    @Override
    public void alertBuildingNotFound() {
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(R.string.add_building);
        Alert.mediumLevel().show(R.string.building_not_found);
    }

    private void setupPreviewMapWithPosition(LatLng latLng) {
        addLocationBackground.setVisibility(View.GONE);
        editLocationButton.setVisibility(View.VISIBLE);
        editLocationButton.setOnClickListener(this);
        buildingLocation = latLng;
        SupportMapFragment supportMapFragment = LiteMapFragment.setupLiteMapFragmentWithPosition(latLng);
        getSupportFragmentManager().beginTransaction().replace(R.id.map_container, supportMapFragment).commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_marker:
                openMapMarkerActivity();
                break;
            case R.id.edit_location:
                openEditMapMarkerActivity(buildingLocation);
                break;
        }

    }

    private void openMapMarkerActivity() {
        Intent intent = new Intent(BuildingFormActivity.this, MapMarkerActivity.class);
        startActivityForResult(intent, MARK_LOCATION_REQUEST_CODE);
    }

    private void openEditMapMarkerActivity(LatLng location) {
        Intent intent = new Intent(BuildingFormActivity.this, MapMarkerActivity.class);
        intent.putExtra(MapMarkerActivity.MAP_LOCATION, location);
        startActivityForResult(intent, MARK_LOCATION_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case MARK_LOCATION_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    setupPreviewMapWithPosition(data.<LatLng>getParcelableExtra(MapMarkerActivity.MAP_LOCATION));
                }
        }
    }

    @Override
    public void onBackPressed() {
        if (twiceBackPressed.onTwiceBackPressed()) {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_activity_building_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                saveBuildingData();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveBuildingData() {
        building.setName(buildingNameView.getText().toString());
        building.setPlace(place);
        Location location = buildingLocation == null
                ? null : new Location(buildingLocation.latitude, buildingLocation.longitude);
        building.setLocation(location);
        try {
            buildingSaver.save(building);
        } catch (ValidatorException e) {
            Alert.highLevel().show(e.getMessageID());
        }
    }

    @Override
    public void displaySaveSuccess() {
        setResult(RESULT_OK);
        finish();
        openSurveyActivity(building);
    }

    private void openSurveyActivity(Building building) {
        Intent intent = new Intent(BuildingFormActivity.this, SurveyActivity.class);
        intent.putExtra(SurveyActivity.BUILDING_UUID_ARG, building.getId().toString());
        intent.putExtra(SurveyActivity.USERNAME_ARG, "sara");
        startActivity(intent);
    }

    @Override
    public void displaySaveFail() {
        Alert.lowLevel().show(R.string.save_fail);
    }

    public void onRootViewClick(View view) {
        SoftKeyboard.hideOn(this);
    }
}
