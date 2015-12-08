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
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.UUID;

import th.or.nectec.tanrabad.domain.place.PlaceController;
import th.or.nectec.tanrabad.domain.place.PlacePresenter;
import th.or.nectec.tanrabad.domain.place.PlaceRepository;
import th.or.nectec.tanrabad.domain.place.PlaceSavePresenter;
import th.or.nectec.tanrabad.domain.place.PlaceSaver;
import th.or.nectec.tanrabad.entity.Location;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.presenter.maps.LiteMapFragment;
import th.or.nectec.tanrabad.survey.repository.InMemoryPlaceRepository;
import th.or.nectec.tanrabad.survey.utils.alert.Alert;
import th.or.nectec.tanrabad.survey.utils.android.SoftKeyboard;
import th.or.nectec.tanrabad.survey.utils.android.TwiceBackPressed;
import th.or.nectec.tanrabad.survey.validator.SavePlaceValidator;
import th.or.nectec.tanrabad.survey.validator.ValidatorException;

public class PlaceAddActivity extends TanrabadActivity implements View.OnClickListener, PlaceSavePresenter, PlacePresenter {

    public static final String PLACE_TYPE_ID_ARG = "place_category_id_arg";
    public static final String PLACE_UUID_ARG = "place_uuid_arg";

    public static final int ADD_PLACE_REQ_CODE = 30000;
    public static final int MARK_LOCATION_REQUEST_CODE = 50000;
    Place place;
    PlaceRepository placeRepository = InMemoryPlaceRepository.getInstance();
    PlaceSaver placeSaver = new PlaceSaver(placeRepository, new SavePlaceValidator(), this);
    private EditText placeNameView;
    private EditText addressSelect;
    private AppCompatSpinner placeTypeSelector;
    private View placeSubtypeLayout;
    private TextView placeSubtypeLabel;
    private AppCompatSpinner placeSubtypeSelector;
    private Button editLocationButton;
    private FrameLayout addLocationBackground;
    private Button addMarkerButton;
    private Toolbar toolbar;
    private LatLng placeLocation;
    private TwiceBackPressed twiceBackPressed;

    public static void openAddPlaceActivity(Activity activity, int placeTypeID) {
        Intent intent = new Intent(activity, PlaceAddActivity.class);
        intent.putExtra(PlaceAddActivity.PLACE_TYPE_ID_ARG, placeTypeID);
        activity.startActivityForResult(intent, ADD_PLACE_REQ_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_add);
        setupTwiceBackPressed();
        setupViews();
        setupPreviewMap();
        setupPlaceTypeSelector();
        loadPlaceData();
    }

    private void setupTwiceBackPressed() {
        twiceBackPressed = new TwiceBackPressed(this);
    }

    private void setupViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        placeNameView = (EditText) findViewById(R.id.place_name);
        addressSelect = (EditText) findViewById(R.id.address_select);
        placeTypeSelector = (AppCompatSpinner) findViewById(R.id.place_type_selector);
        placeSubtypeLayout = findViewById(R.id.place_subtype_layout);
        placeSubtypeLabel = (TextView) findViewById(R.id.place_subtype_label);
        placeSubtypeSelector = (AppCompatSpinner) findViewById(R.id.place_subtype_selector);
        addLocationBackground = (FrameLayout) findViewById(R.id.add_location_background);
        addMarkerButton = (Button) findViewById(R.id.add_marker);

        editLocationButton = (Button) findViewById(R.id.edit_location);
        editLocationButton.setVisibility(View.GONE);

        setSupportActionBar(toolbar);
        addMarkerButton.setOnClickListener(this);
        editLocationButton.setOnClickListener(this);
    }

    private void setupPreviewMap() {
        SupportMapFragment supportMapFragment = LiteMapFragment.setupLiteMapFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.map_container, supportMapFragment).commit();
    }

    private void setupPlaceTypeSelector() {
        final PlaceTypeForAddAdapter placeAdapter = new PlaceTypeForAddAdapter(this);
        placeTypeSelector.setAdapter(placeAdapter);
        placeTypeSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (placeAdapter.getItem(i).id == Place.TYPE_WORSHIP) {
                    placeSubtypeLayout.setVisibility(View.VISIBLE);
                    placeSubtypeLabel.setText(R.string.place_worship_type);
                    placeSubtypeSelector.setAdapter(new PlaceSubTypeOfWorshipAdapter(PlaceAddActivity.this));
                } else {
                    placeSubtypeLayout.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        placeTypeSelector.setSelection(placeAdapter.getPlaceTypePosition(getPlaceTypeID()));
    }

    private int getPlaceTypeID() {
        return getIntent().getIntExtra(PLACE_TYPE_ID_ARG, Place.SUBTYPE_TEMPLE);
    }

    private void loadPlaceData() {
        if (TextUtils.isEmpty(getPlaceUUID())) {
            place = Place.withName(null);
        } else {
            PlaceController placeController = new PlaceController(placeRepository, this);
            placeController.showPlace(UUID.fromString(getPlaceUUID()));
        }
    }

    public String getPlaceUUID() {
        return getIntent().getStringExtra(PLACE_UUID_ARG);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_activity_place_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                doSaveData();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void doSaveData() {
        place.setName(placeNameView.getText().toString());
        int placeTypeID = ((PlaceType) placeTypeSelector.getSelectedItem()).id;
        place.setType(placeTypeID);
        if (placeTypeID == Place.TYPE_WORSHIP) {
            place.setSubType(((PlaceType) placeSubtypeSelector.getSelectedItem()).id);
        }
        Location location = placeLocation == null
                ? null : new Location(placeLocation.latitude, placeLocation.longitude);
        place.setLocation(location);

        try {
            placeSaver.save(place);
        } catch (ValidatorException e) {
            Alert.highLevel().show(e.getMessageID());
        }
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

    private void setupPreviewMapWithPosition(LatLng latLng) {
        addLocationBackground.setVisibility(View.GONE);
        editLocationButton.setVisibility(View.VISIBLE);
        editLocationButton.setOnClickListener(this);
        placeLocation = latLng;
        SupportMapFragment supportMapFragment = LiteMapFragment.setupLiteMapFragmentWithPosition(latLng);
        getSupportFragmentManager().beginTransaction().replace(R.id.map_container, supportMapFragment).commit();
    }

    @Override
    public void onBackPressed() {
        if (twiceBackPressed.onTwiceBackPressed()) {
            finish();
        }
    }

    public void onRootViewClick(View view) {
        SoftKeyboard.hideOn(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_marker:
                openMapMarkerActivity();
                break;
            case R.id.edit_location:
                openEditMapMarkerActivity(placeLocation);
                break;
        }
    }

    private void openMapMarkerActivity() {
        Intent intent = new Intent(PlaceAddActivity.this, MapMarkerActivity.class);
        startActivityForResult(intent, MARK_LOCATION_REQUEST_CODE);
    }

    private void openEditMapMarkerActivity(LatLng location) {
        Intent intent = new Intent(PlaceAddActivity.this, MapMarkerActivity.class);
        intent.putExtra(MapMarkerActivity.MAP_LOCATION, location);
        startActivityForResult(intent, MARK_LOCATION_REQUEST_CODE);
    }

    @Override
    public void displaySaveSuccess() {
        setResult(RESULT_OK);
        finish();
        SurveyBuildingHistoryActivity.openBuildingSurveyHistoryActivity(PlaceAddActivity.this, place, "sara");
    }

    @Override
    public void displaySaveFail() {

    }

    @Override
    public void alertCannotSaveVillageType() {

    }

    @Override
    public void displayPlace(Place place) {
        this.place = place;
    }

    @Override
    public void alertPlaceNotFound() {
        this.place = Place.withName(null);
    }
}
