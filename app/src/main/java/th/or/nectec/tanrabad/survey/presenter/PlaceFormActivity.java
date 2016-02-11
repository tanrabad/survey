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
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.google.android.gms.maps.SupportMapFragment;
import org.joda.time.DateTime;
import th.or.nectec.tanrabad.domain.place.*;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.entity.field.Location;
import th.or.nectec.tanrabad.entity.lookup.PlaceSubType;
import th.or.nectec.tanrabad.entity.lookup.PlaceType;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.job.AbsJobRunner;
import th.or.nectec.tanrabad.survey.job.Job;
import th.or.nectec.tanrabad.survey.job.PostDataJob;
import th.or.nectec.tanrabad.survey.job.PutDataJob;
import th.or.nectec.tanrabad.survey.presenter.maps.LiteMapFragment;
import th.or.nectec.tanrabad.survey.presenter.maps.LocationUtils;
import th.or.nectec.tanrabad.survey.repository.BrokerPlaceRepository;
import th.or.nectec.tanrabad.survey.repository.adapter.ThaiWidgetProvinceRepository;
import th.or.nectec.tanrabad.survey.repository.persistence.DbPlaceRepository;
import th.or.nectec.tanrabad.survey.service.PlaceRestService;
import th.or.nectec.tanrabad.survey.utils.alert.Alert;
import th.or.nectec.tanrabad.survey.utils.android.InternetConnection;
import th.or.nectec.tanrabad.survey.utils.android.ResourceUtils;
import th.or.nectec.tanrabad.survey.utils.android.SoftKeyboard;
import th.or.nectec.tanrabad.survey.utils.android.TwiceBackPressed;
import th.or.nectec.tanrabad.survey.validator.SavePlaceValidator;
import th.or.nectec.tanrabad.survey.validator.UpdatePlaceValidator;
import th.or.nectec.tanrabad.survey.validator.ValidatorException;
import th.or.nectec.thai.widget.address.AddressPicker;
import th.or.nectec.thai.widget.address.AddressPickerDialog;

import java.util.UUID;

public class PlaceFormActivity extends TanrabadActivity implements View.OnClickListener,
        PlaceSavePresenter, PlacePresenter {

    public static final String PLACE_TYPE_ID_ARG = "place_category_id_arg";
    public static final String PLACE_UUID_ARG = "place_uuid_arg";

    public static final int ADD_PLACE_REQ_CODE = 30000;
    Place place;
    PlaceRepository placeRepository = BrokerPlaceRepository.getInstance();

    private EditText placeNameView;
    private AddressPicker addressSelect;
    private AppCompatSpinner placeTypeSelector;
    private View placeSubtypeLayout;
    private TextView placeSubtypeLabel;
    private AppCompatSpinner placeSubtypeSelector;
    private Button editLocationButton;
    private FrameLayout addLocationBackground;
    private Button addMarkerButton;
    private TwiceBackPressed twiceBackPressed;


    public static void startAdd(Activity activity, int placeTypeID) {
        Intent intent = new Intent(activity, PlaceFormActivity.class);
        intent.putExtra(PlaceFormActivity.PLACE_TYPE_ID_ARG, placeTypeID);
        activity.startActivityForResult(intent, ADD_PLACE_REQ_CODE);
    }

    public static void startEdit(Activity activity, Place place) {
        Intent intent = new Intent(activity, PlaceFormActivity.class);
        intent.putExtra(PlaceFormActivity.PLACE_UUID_ARG, place.getId().toString());
        intent.putExtra(PlaceFormActivity.PLACE_TYPE_ID_ARG, place.getType());
        activity.startActivityForResult(intent, ADD_PLACE_REQ_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_form);
        setupViews();
        setupHomeButton();
        setupTwiceBackPressed();
        setupPreviewMap();
        setupPlaceTypeSelector();
        loadPlaceData();
    }

    private void setupTwiceBackPressed() {
        twiceBackPressed = new TwiceBackPressed(this);
    }

    private void setupViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        placeNameView = (EditText) findViewById(R.id.place_name);
        addressSelect = (AddressPicker) findViewById(R.id.address_select);
        AddressPickerDialog popup = new AddressPickerDialog(this)
                .setProvinceRepository(new ThaiWidgetProvinceRepository());
        addressSelect.setPopup(popup);
        placeTypeSelector = (AppCompatSpinner) findViewById(R.id.place_type_selector);
        placeSubtypeLayout = findViewById(R.id.place_subtype_layout);
        placeSubtypeLabel = (TextView) findViewById(R.id.place_subtype_label);
        placeSubtypeSelector = (AppCompatSpinner) findViewById(R.id.place_subtype_selector);
        addLocationBackground = (FrameLayout) findViewById(R.id.add_location_background);
        addLocationBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SoftKeyboard.hideOn(PlaceFormActivity.this);
            }
        });
        addMarkerButton = (Button) findViewById(R.id.add_marker);

        editLocationButton = (Button) findViewById(R.id.edit_location);
        editLocationButton.setVisibility(View.GONE);

        setSupportActionBar(toolbar);
        addMarkerButton.setOnClickListener(this);
        editLocationButton.setOnClickListener(this);
    }

    private void setupPreviewMap() {
        SupportMapFragment supportMapFragment = LiteMapFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.map_container, supportMapFragment).commit();
    }

    private void setupPlaceTypeSelector() {
        final PlaceTypeForAddAdapter placeAdapter = new PlaceTypeForAddAdapter(
                this, !AccountUtils.canAddOrEditVillage());
        placeTypeSelector.setAdapter(placeAdapter);
        placeTypeSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setupPlaceSubtypeSpinner(placeAdapter.getItem(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        placeTypeSelector.setSelection(placeAdapter.getPlaceTypePosition(getPlaceTypeID()));
    }

    private void setupPlaceSubtypeSpinner(PlaceType selectedPlaceType) {
        placeSubtypeLabel.setText(String.format(getString(R.string.place_subtype_label), selectedPlaceType.getName()));
        PlaceSubTypeAdapter placeSubTypeAdapter = new PlaceSubTypeAdapter(
                PlaceFormActivity.this, selectedPlaceType.getId());
        placeSubtypeSelector.setAdapter(placeSubTypeAdapter);
        if (placeSubTypeAdapter.getCount() > 0) {
            placeSubtypeLayout.setVisibility(View.VISIBLE);
            placeSubtypeSelector.setSelection(placeSubTypeAdapter.getPosition(place.getSubType()));
        } else {
            placeSubtypeLayout.setVisibility(View.GONE);
        }

    }

    private int getPlaceTypeID() {
        return getIntent().getIntExtra(PLACE_TYPE_ID_ARG, Place.TYPE_WORSHIP);
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
        getMenuInflater().inflate(R.menu.action_activity_place_form, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                if (TextUtils.isEmpty(getPlaceUUID())) {
                    doSaveData();
                } else {
                    doUpdateData();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void doSaveData() {
        try {
            getPlaceFieldData();
            PlaceSaver placeSaver = new PlaceSaver(placeRepository, new SavePlaceValidator(), this);
            placeSaver.save(place);
        } catch (ValidatorException e) {
            Alert.highLevel().show(e.getMessageID());
        }
    }

    private void getPlaceFieldData() {
        place.setName(placeNameView.getText().toString().trim());
        int placeTypeID = ((PlaceType) placeTypeSelector.getSelectedItem()).getId();
        place.setType(placeTypeID);
        PlaceSubType placeSubType = ((PlaceSubType) placeSubtypeSelector.getSelectedItem());
        place.setSubType(placeSubType.getId());
        place.setSubdistrictCode(addressSelect.getAddress() == null ? null : addressSelect.getAddress().getCode());
        place.setUpdateTimestamp(DateTime.now().toString());
        place.setUpdateBy(AccountUtils.getUser().getUsername());
    }

    public void doUpdateData() {
        getPlaceFieldData();
        try {
            PlaceSaver placeSaver = new PlaceSaver(placeRepository, new UpdatePlaceValidator(), this);
            placeSaver.update(place);
        } catch (ValidatorException e) {
            Alert.highLevel().show(e.getMessageID());
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case MapMarkerActivity.MARK_LOCATION_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Location placeLocation = LocationUtils.convertJsonToLocation(
                            data.getStringExtra(MapMarkerActivity.MAP_LOCATION));
                    place.setLocation(placeLocation);
                    setupPreviewMapWithPosition(placeLocation);
                }
        }
    }

    private void setupPreviewMapWithPosition(Location location) {
        addLocationBackground.setBackgroundColor(ResourceUtils.from(this).getColor(R.color.transparent));
        addMarkerButton.setVisibility(View.GONE);
        editLocationButton.setVisibility(View.VISIBLE);
        editLocationButton.setOnClickListener(this);
        SupportMapFragment supportMapFragment = LiteMapFragment.newInstance(location);
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
    public void displaySaveSuccess() {
        if (InternetConnection.isAvailable(this))
            doPostData();
        setResult(RESULT_OK);
        finish();
        SurveyBuildingHistoryActivity.open(PlaceFormActivity.this, place);
    }

    private void doPostData() {
        PlacePostJobRunner placePostJobRunner = new PlacePostJobRunner();
        placePostJobRunner.addJob(new PostDataJob<>(new DbPlaceRepository(this), new PlaceRestService()));
        placePostJobRunner.start();
    }

    @Override
    public void displaySaveFail() {

    }

    @Override
    public void alertCannotSaveVillageType() {

    }

    @Override
    public void displayUpdateSuccess() {
        if (InternetConnection.isAvailable(this))
            doPutData();
        setResult(RESULT_OK);
        finish();
    }

    private void doPutData() {
        PlacePostJobRunner placePostJobRunner = new PlacePostJobRunner();
        placePostJobRunner.addJob(new PutDataJob<>(new DbPlaceRepository(this), new PlaceRestService()));
        placePostJobRunner.start();
    }

    @Override
    public void displayUpdateFail() {

    }

    @Override
    public void displayPlace(Place place) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(R.string.edit_place);

        this.place = place;
        placeNameView.setText(place.getName());

        if (!TextUtils.isEmpty(place.getSubdistrictCode())) {
            addressSelect.setAddressCode(place.getSubdistrictCode());
        }

        if (place.getLocation() != null)
            setupPreviewMapWithPosition(place.getLocation());
    }


    @Override
    public void alertPlaceNotFound() {
        this.place = Place.withName(null);
    }

    public class PlacePostJobRunner extends AbsJobRunner {

        @Override
        protected void onJobError(Job errorJob, Exception exception) {
            super.onJobError(errorJob, exception);
            Log.e(errorJob.toString(), exception.getMessage());
        }

        @Override
        protected void onJobStart(Job startingJob) {
        }

        @Override
        protected void onRunFinish() {
            if (errorJobs() == 0) {
                Alert.mediumLevel().show(R.string.upload_data_success);
            } else {
                Alert.mediumLevel().show(R.string.upload_data_failure);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_marker:
                MapMarkerActivity.startAdd(PlaceFormActivity.this);
                break;
            case R.id.edit_location:
                MapMarkerActivity.startEdit(PlaceFormActivity.this, place.getLocation());
                break;
        }
    }
}
