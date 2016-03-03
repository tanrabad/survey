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

import org.joda.time.DateTime;

import java.util.UUID;

import th.or.nectec.tanrabad.domain.building.BuildingController;
import th.or.nectec.tanrabad.domain.building.BuildingPresenter;
import th.or.nectec.tanrabad.domain.building.BuildingSavePresenter;
import th.or.nectec.tanrabad.domain.building.BuildingSaver;
import th.or.nectec.tanrabad.domain.place.PlaceController;
import th.or.nectec.tanrabad.domain.place.PlacePresenter;
import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.entity.field.Location;
import th.or.nectec.tanrabad.entity.lookup.PlaceType;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.TanrabadApp;
import th.or.nectec.tanrabad.survey.presenter.maps.LiteMapFragment;
import th.or.nectec.tanrabad.survey.presenter.maps.LocationUtils;
import th.or.nectec.tanrabad.survey.repository.BrokerBuildingRepository;
import th.or.nectec.tanrabad.survey.repository.BrokerPlaceRepository;
import th.or.nectec.tanrabad.survey.utils.alert.Alert;
import th.or.nectec.tanrabad.survey.utils.android.SoftKeyboard;
import th.or.nectec.tanrabad.survey.utils.android.TwiceBackPressed;
import th.or.nectec.tanrabad.survey.validator.SaveBuildingValidator;
import th.or.nectec.tanrabad.survey.validator.UpdateBuildingValidator;
import th.or.nectec.tanrabad.survey.validator.ValidatorException;

public class BuildingFormActivity extends TanrabadActivity implements PlacePresenter, BuildingPresenter,
        BuildingSavePresenter, View.OnClickListener {

    public static final String PLACE_UUID_ARG = "place_uuid_arg";
    public static final String BUILDING_UUID_ARG = "building_uuid_arg";

    public static final int ADD_BUILDING_REQ_CODE = 40000;


    private TextView placeName;
    private Toolbar toolbar;
    private TextView buildingNameTitle;
    private EditText buildingNameView;
    private FrameLayout addLocationBackground;
    private PlaceController placeController = new PlaceController(BrokerPlaceRepository.getInstance(), this);
    private BuildingController buildingController = new BuildingController(
            BrokerBuildingRepository.getInstance(), this);

    private Place place;
    private Building building;
    private Button editLocationButton;
    private TwiceBackPressed twiceBackPressed;

    public static void startEdit(Activity activity, String placeUuid, String buildingUuid) {
        Intent intent = new Intent(activity, BuildingFormActivity.class);
        intent.putExtra(PLACE_UUID_ARG, placeUuid);
        intent.putExtra(BuildingFormActivity.BUILDING_UUID_ARG, buildingUuid);
        activity.startActivityForResult(intent, ADD_BUILDING_REQ_CODE);
    }

    public static void startAdd(Activity activity, String placeUuid) {
        Intent intent = new Intent(activity, BuildingFormActivity.class);
        intent.putExtra(PLACE_UUID_ARG, placeUuid);
        activity.startActivityForResult(intent, ADD_BUILDING_REQ_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_form);
        assignViews();
        setSupportActionBar(toolbar);
        setupHomeButton();
        setupTwiceBackPressed();
        placeController.showPlace(UUID.fromString(getPlaceUuid()));
        loadBuildingData();
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

    private void setupTwiceBackPressed() {
        twiceBackPressed = new TwiceBackPressed(this);
    }

    private String getPlaceUuid() {
        return getIntent().getStringExtra(PLACE_UUID_ARG);
    }

    private void loadBuildingData() {
        if (TextUtils.isEmpty(getBuildingUuid())) {
            setupPreviewMap();
            building = Building.withName(null);
        } else {
            buildingController.showBuilding(UUID.fromString(getBuildingUuid()));
        }
    }

    private String getBuildingUuid() {
        return getIntent().getStringExtra(BUILDING_UUID_ARG);
    }

    private void setupPreviewMap() {
        SupportMapFragment supportMapFragment = LiteMapFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.map_container, supportMapFragment).commit();
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
        building.setName(buildingNameView.getText().toString().trim());
        building.setPlace(place);
        building.setUpdateTimestamp(DateTime.now().toString());
        building.setUpdateBy(AccountUtils.getUser().getUsername());
        try {
            if (TextUtils.isEmpty(getBuildingUuid())) {
                BuildingSaver buildingSaver = new BuildingSaver(
                        BrokerBuildingRepository.getInstance(), new SaveBuildingValidator(), this);
                buildingSaver.save(building);
            } else {
                BuildingSaver buildingSaver = new BuildingSaver(
                        BrokerBuildingRepository.getInstance(), new UpdateBuildingValidator(), this);
                buildingSaver.update(building);
            }
        } catch (ValidatorException exception) {
            Alert.highLevel().show(exception.getMessageId());
        }
    }

    @Override
    public void displayPlace(Place place) {
        this.place = place;
        placeName.setText(place.getName());
        if (place.getType() == PlaceType.VILLAGE_COMMUNITY) {
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
            setupPreviewMapWithPosition(building.getLocation());
        }
    }

    @Override
    public void alertBuildingNotFound() {
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(R.string.add_building);
        Alert.mediumLevel().show(R.string.building_not_found);
    }

    private void setupPreviewMapWithPosition(Location location) {
        addLocationBackground.setVisibility(View.GONE);
        editLocationButton.setVisibility(View.VISIBLE);
        editLocationButton.setOnClickListener(this);
        SupportMapFragment supportMapFragment = LiteMapFragment.newInstance(location);
        getSupportFragmentManager().beginTransaction().replace(R.id.map_container, supportMapFragment).commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_marker:
                BuildingMapMarkerActivity.startAdd(BuildingFormActivity.this, getPlaceUuid());
                break;
            case R.id.edit_location:
                BuildingMapMarkerActivity.startEdit(BuildingFormActivity.this, getPlaceUuid(), building.getLocation());
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case BuildingMapMarkerActivity.MARK_LOCATION_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Location buildingLocation = LocationUtils.convertJsonToLocation(
                            data.getStringExtra(BuildingMapMarkerActivity.BUILDING_LOCATION));
                    building.setLocation(buildingLocation);
                    setupPreviewMapWithPosition(buildingLocation);
                }
        }
    }

    @Override
    public void onBackPressed() {
        if (twiceBackPressed.onTwiceBackPressed()) {
            finish();
            if (TextUtils.isEmpty(getBuildingUuid()))
                BuildingListActivity.open(this, getPlaceUuid());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_activity_building_form, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void displaySaveSuccess() {
        setResult(RESULT_OK);
        finish();
        TanrabadApp.action().addBuilding(building);
        SurveyActivity.open(BuildingFormActivity.this, building);
    }

    @Override
    public void displaySaveFail() {
        Alert.lowLevel().show(R.string.save_fail);
    }

    @Override
    public void displayUpdateFail() {

    }

    @Override
    public void displayUpdateSuccess() {
        setResult(RESULT_OK);
        finish();
        TanrabadApp.action().updateBuilding(building);
    }

    public void onRootViewClick(View view) {
        SoftKeyboard.hideOn(this);
    }
}
