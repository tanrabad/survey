package th.or.nectec.tanrabad.survey;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

public class BuildingAddActivity extends TanrabadActivity {

    public static final String PLACE_UUID_ARG = "place_uuid_arg";
    private TextView placeName;
    private Toolbar toolbar;
    private TextView buildingNameTitle;
    private EditText buildingName;
    private FrameLayout mapContainer;
    private FrameLayout addLocationBackground;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_add);
        assignViews();

        setSupportActionBar(toolbar);

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
}
