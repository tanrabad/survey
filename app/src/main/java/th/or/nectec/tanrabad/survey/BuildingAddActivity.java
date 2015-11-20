package th.or.nectec.tanrabad.survey;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.maps.SupportMapFragment;

import java.util.UUID;

import th.or.nectec.tanrabad.domain.PlaceController;
import th.or.nectec.tanrabad.domain.PlacePresenter;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.survey.repository.StubPlaceRepository;
import th.or.nectec.tanrabad.survey.utils.alert.Alert;

public class BuildingAddActivity extends TanrabadActivity implements PlacePresenter, View.OnClickListener {

    public static final String PLACE_UUID_ARG = "place_uuid_arg";
    private TextView placeName;
    private Toolbar toolbar;
    private TextView buildingNameTitle;
    private EditText buildingName;
    private FrameLayout addLocationBackground;
    private Button addMarkerButton;

    private PlaceController placeController = new PlaceController(new StubPlaceRepository(), this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_add);
        assignViews();

        setSupportActionBar(toolbar);
        placeController.showPlace(UUID.fromString(getPlaceUUID()));
        setupPreviewMap();
    }

    private void setupPreviewMap() {
        SupportMapFragment supportMapFragment = TanrabadLiteMapFragment.setupLiteMapFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.map_container, supportMapFragment).commit();
    }

    private String getPlaceUUID() {
        return getIntent().getStringExtra(PLACE_UUID_ARG);
    }

    private void assignViews() {
        placeName = (TextView) findViewById(R.id.place_name);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        buildingNameTitle = (TextView) findViewById(R.id.building_name_title);
        buildingName = (EditText) findViewById(R.id.building_name);
        addLocationBackground = (FrameLayout) findViewById(R.id.add_location_background);
        addMarkerButton = (Button) findViewById(R.id.button);
        addMarkerButton.setOnClickListener(this);
    }

    @Override
    public void displayPlace(Place place) {
        placeName.setText(place.getName());
        if(place.getType()==Place.TYPE_VILLAGE_COMMUNITY){
            buildingNameTitle.setText(R.string.house_no);
        }else{
            buildingNameTitle.setText(R.string.building_name);
        }
    }

    @Override
    public void alertPlaceNotFound() {
        Alert.highLevel().show(R.string.place_not_found);
    }

    @Override
    public void onClick(View view) {
        openMapMarkerActivity();
    }

    private void openMapMarkerActivity() {
        Intent intent = new Intent(BuildingAddActivity.this, MapMarkerActivity.class);
        startActivity(intent);
    }
}
