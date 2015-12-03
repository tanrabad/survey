package th.or.nectec.tanrabad.survey.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.presenter.maps.LiteMapFragment;
import th.or.nectec.tanrabad.survey.utils.android.SoftKeyboard;

public class PlaceAddActivity extends TanrabadActivity implements View.OnClickListener {

    public static final String PLACE_UUID_ARG = "place_uuid_arg";
    public static final String BUILDING_UUID_ARG = "building_uuid_arg";
    public static final int MARK_LOCATION_REQUEST_CODE = 50000;
    private EditText placeName;
    private EditText addressSelect;
    private AppCompatSpinner placeTypeSelector;
    private RelativeLayout placeSubtypeLayout;
    private TextView placeSubtypeLabel;
    private AppCompatSpinner placeSubtypeSelector;
    private Button editLocationButton;
    private FrameLayout mapContainer;
    private FrameLayout addLocationBackground;
    private Button addMarkerButton;
    private Toolbar toolbar;
    private LatLng placeLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_add);
        setupViews();
        setupPreviewMap();
        setupPlaceTypeSelector();
    }

    private void setupViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        placeName = (EditText) findViewById(R.id.place_name);
        addressSelect = (EditText) findViewById(R.id.address_select);
        placeTypeSelector = (AppCompatSpinner) findViewById(R.id.place_type_selector);
        placeSubtypeLayout = (RelativeLayout) findViewById(R.id.place_subtype_layout);
        placeSubtypeLabel = (TextView) findViewById(R.id.place_subtype_label);
        placeSubtypeSelector = (AppCompatSpinner) findViewById(R.id.place_subtype_selector);
        editLocationButton = (Button) findViewById(R.id.edit_location);
        mapContainer = (FrameLayout) findViewById(R.id.map_container);
        addLocationBackground = (FrameLayout) findViewById(R.id.add_location_background);
        addMarkerButton = (Button) findViewById(R.id.add_marker);

        setSupportActionBar(toolbar);
        addMarkerButton.setOnClickListener(this);
        editLocationButton.setOnClickListener(this);
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
        }
        return super.onOptionsItemSelected(item);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case MARK_LOCATION_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    setupPreviewMapWithPosition(data.<LatLng>getParcelableExtra(MapMarkerActivity.MAP_LOCATION));
                }
        }
    }

    private void setupPreviewMap() {
        SupportMapFragment supportMapFragment = LiteMapFragment.setupLiteMapFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.map_container, supportMapFragment).commit();
    }

    private void setupPreviewMapWithPosition(LatLng latLng) {
        addLocationBackground.setVisibility(View.GONE);
        editLocationButton.setVisibility(View.VISIBLE);
        editLocationButton.setOnClickListener(this);
        placeLocation = latLng;
        SupportMapFragment supportMapFragment = LiteMapFragment.setupLiteMapFragmentWithPosition(latLng);
        getSupportFragmentManager().beginTransaction().replace(R.id.map_container, supportMapFragment).commit();
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
}
