package th.or.nectec.tanrabad.survey;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import th.or.nectec.tanrabad.domain.PlaceChooser;
import th.or.nectec.tanrabad.domain.PlaceListPresenter;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.survey.repository.StubPlaceRepository;

public class PlaceListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, PlaceListPresenter {

    PlaceAdapter placeAdapter;
    PlaceChooser placeChooser;
    private TextView placeCountView;
    private ListView placeListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_list);

        this.placeListView = (ListView) findViewById(R.id.place_list);
        this.placeCountView = (TextView) findViewById(R.id.place_count);
        placeListView.setOnItemClickListener(this);

        placeChooser = new PlaceChooser(new StubPlaceRepository(), this);
        placeChooser.getPlaceList();
    }

    @Override
    public void displayPlaceList(List<Place> places) {
        placeAdapter = new PlaceAdapter(PlaceListActivity.this, places);
        placeListView.setAdapter(placeAdapter);
        placeCountView.setText(String.valueOf(places.size()));
    }

    @Override
    public void displayPlaceNotFound() {
        Toast.makeText(PlaceListActivity.this, R.string.place_not_found, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(PlaceListActivity.this, BuildingListActivity.class);
        intent.putExtra(BuildingListActivity.PLACE_UUID_ARG, placeAdapter.getItem(i).getId().toString());
        startActivity(intent);
    }
}
