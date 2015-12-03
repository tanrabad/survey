package th.or.nectec.tanrabad.survey.presenter;

import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.utils.android.SoftKeyboard;

public class PlaceAddActivity extends TanrabadActivity {

    private TextView placeNameLabel;
    private EditText placeName;
    private EditText addressSelect;
    private AppCompatSpinner placeTypeSelector;
    private RelativeLayout placeSubtypeLayout;
    private TextView placeSubtypeLabel;
    private AppCompatSpinner placeSubtypeSelector;
    private Button editLocation;
    private FrameLayout mapContainer;
    private FrameLayout addLocationBackground;
    private Button addMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_add);
        setupViews();
    }

    private void setupViews() {
        placeNameLabel = (TextView) findViewById(R.id.place_name_label);
        placeName = (EditText) findViewById(R.id.place_name);
        addressSelect = (EditText) findViewById(R.id.address_select);
        placeTypeSelector = (AppCompatSpinner) findViewById(R.id.place_type_selector);
        placeSubtypeLayout = (RelativeLayout) findViewById(R.id.place_subtype_layout);
        placeSubtypeLabel = (TextView) findViewById(R.id.place_subtype_label);
        placeSubtypeSelector = (AppCompatSpinner) findViewById(R.id.place_subtype_selector);
        editLocation = (Button) findViewById(R.id.edit_location);
        mapContainer = (FrameLayout) findViewById(R.id.map_container);
        addLocationBackground = (FrameLayout) findViewById(R.id.add_location_background);
        addMarker = (Button) findViewById(R.id.add_marker);
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

    public void onRootViewClick(View view) {
        SoftKeyboard.hideOn(this);
    }
}
