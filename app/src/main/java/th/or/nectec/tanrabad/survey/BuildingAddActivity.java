package th.or.nectec.tanrabad.survey;

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

import th.or.nectec.tanrabad.domain.BuildingController;
import th.or.nectec.tanrabad.domain.BuildingPresenter;
import th.or.nectec.tanrabad.domain.BuildingSavePresenter;
import th.or.nectec.tanrabad.domain.BuildingSaver;
import th.or.nectec.tanrabad.domain.PlaceController;
import th.or.nectec.tanrabad.domain.PlacePresenter;
import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.entity.Location;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.survey.maps.LiteMapFragment;
import th.or.nectec.tanrabad.survey.repository.InMemoryBuildingRepository;
import th.or.nectec.tanrabad.survey.repository.StubPlaceRepository;
import th.or.nectec.tanrabad.survey.utils.alert.Alert;
import th.or.nectec.tanrabad.survey.validator.SaveBuildingValidator;

public class BuildingAddActivity extends TanrabadActivity implements PlacePresenter, BuildingPresenter, BuildingSavePresenter, View.OnClickListener {

    public static final String PLACE_UUID_ARG = "place_uuid_arg";
    public static final String BUILDING_UUID_ARG = "building_uuid_arg";
    public static final int MARK_LOCATION_REQUEST_CODE = 50000;


    private TextView placeName;
    private Toolbar toolbar;
    private TextView buildingNameTitle;
    private EditText buildingNameView;
    private FrameLayout addLocationBackground;
    private LatLng buildingLocation;
    private PlaceController placeController = new PlaceController(new StubPlaceRepository(), this);
    private BuildingController buildingController = new BuildingController(InMemoryBuildingRepository.getInstance(), this);
    private BuildingSaver buildingSaver = new BuildingSaver(InMemoryBuildingRepository.getInstance(), new SaveBuildingValidator(), this);

    private Place place;
    private Building building;
    private Button editLocationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_add);
        assignViews();

        setSupportActionBar(toolbar);
        placeController.showPlace(UUID.fromString(getPlaceUUID()));
        loadBuildingData();
    }

    private void loadBuildingData() {
        if (TextUtils.isEmpty(getBuildingUUID())) {
            setupPreviewMap();
            this.building = Building.withName(null);
        } else {
            buildingController.showBuilding(UUID.fromString(getBuildingUUID()));
        }
    }

    private void assignViews() {
        placeName = (TextView) findViewById(R.id.place_name);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        buildingNameTitle = (TextView) findViewById(R.id.building_name_title);
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

    private String getBuildingUUID() {
        return getIntent().getStringExtra(BUILDING_UUID_ARG);
    }

    @Override
    public void displayPlace(Place place) {
        this.place = place;
        placeName.setText(place.getName());
        if (place.getType() == Place.TYPE_VILLAGE_COMMUNITY) {
            buildingNameTitle.setText(R.string.house_no);
        } else {
            buildingNameTitle.setText(R.string.building_name);
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
            getSupportActionBar().setTitle(R.string.building_add);
        Alert.mediumLevel().show(R.string.building_not_found);
    }

    private void setupPreviewMap() {
        SupportMapFragment supportMapFragment = LiteMapFragment.setupLiteMapFragment();
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
        Intent intent = new Intent(BuildingAddActivity.this, MapMarkerActivity.class);
        startActivityForResult(intent, MARK_LOCATION_REQUEST_CODE);
    }

    private void openEditMapMarkerActivity(LatLng location) {
        Intent intent = new Intent(BuildingAddActivity.this, MapMarkerActivity.class);
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

    private void setupPreviewMapWithPosition(LatLng latLng) {
        addLocationBackground.setVisibility(View.GONE);
        editLocationButton.setVisibility(View.VISIBLE);
        editLocationButton.setOnClickListener(this);
        buildingLocation = latLng;
        SupportMapFragment supportMapFragment = LiteMapFragment.setupLiteMapFragmentWithPosition(latLng);
        getSupportFragmentManager().beginTransaction().replace(R.id.map_container, supportMapFragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_save_menu, menu);
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
        buildingSaver.save(building);
    }

    @Override
    public void displaySaveSuccess() {
        Alert.lowLevel().show(R.string.save_success);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void displaySaveFail() {
        Alert.highLevel().show(R.string.save_fail);
    }
}
