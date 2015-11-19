package th.or.nectec.tanrabad.survey;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.UUID;

import th.or.nectec.tanrabad.domain.PlaceController;
import th.or.nectec.tanrabad.domain.PlacePresenter;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.survey.repository.StubPlaceRepository;

public class BuildingAddActivity extends TanrabadActivity implements PlacePresenter {

    public static final String PLACE_UUID_ARG = "place_uuid_arg";
    private TextView placeName;
    private Toolbar toolbar;
    private TextView buildingNameTitle;
    private EditText buildingName;
    private FrameLayout mapContainer;
    private FrameLayout addLocationBackground;
    private Button button;

    private PlaceController placeController = new PlaceController(new StubPlaceRepository(), this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_add);
        assignViews();

        setSupportActionBar(toolbar);
        placeController.showPlace(UUID.fromString(getPlaceUUID()));


    }

    private String getPlaceUUID() {
        return getIntent().getStringExtra(PLACE_UUID_ARG);
    }

    private void assignViews() {
        placeName = (TextView) findViewById(R.id.place_name);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        buildingNameTitle = (TextView) findViewById(R.id.building_name_title);
        buildingName = (EditText) findViewById(R.id.building_name);
        mapContainer = (FrameLayout) findViewById(R.id.map_container);
        addLocationBackground = (FrameLayout) findViewById(R.id.add_location_background);
        button = (Button) findViewById(R.id.button);
    }

    @Override
    public void displayPlace(Place place) {
        placeName.setText(place.getName());
    }

    @Override
    public void alertPlaceNotFound() {

    }

}
