package th.or.nectec.tanrabad.survey.view;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import th.or.nectec.tanrabad.domain.PlaceChooser;
import th.or.nectec.tanrabad.domain.PlaceListPresenter;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.survey.BuildingListActivity;
import th.or.nectec.tanrabad.survey.PlaceAdapter;
import th.or.nectec.tanrabad.survey.PlaceTypeAdapter;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.repository.StubPlaceRepository;

public class PlaceListInDatabaseFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener, PlaceListPresenter {

    private PlaceAdapter placeAdapter;
    private PlaceTypeAdapter placeTypeAdapter;
    private PlaceChooser placeChooser = new PlaceChooser(new StubPlaceRepository(), this);
    private TextView placeCountView;
    private ListView placeListView;
    private AppCompatSpinner placeFilterView;

    public static PlaceListInDatabaseFragment newInstance() {
        PlaceListInDatabaseFragment fragment = new PlaceListInDatabaseFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupPlaceFilterSpinner();
        setupPlaceList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_place_list_in_database, container, false);
        setupViews(view);
        return view;
    }

    private void setupViews(View view) {
        this.placeListView = (ListView) view.findViewById(R.id.place_list);
        this.placeCountView = (TextView) view.findViewById(R.id.place_count);
        this.placeFilterView = (AppCompatSpinner) view.findViewById(R.id.place_filter);
    }

    private void setupPlaceFilterSpinner() {
        placeFilterView.setOnItemSelectedListener(this);
        placeTypeAdapter = new PlaceTypeAdapter(getActivity());
        placeFilterView.setAdapter(placeTypeAdapter);
    }

    private void setupPlaceList() {
        placeListView.setOnItemClickListener(this);
        placeAdapter = new PlaceAdapter(getActivity());
        placeListView.setAdapter(placeAdapter);
    }

    @Override
    public void displayPlaceList(List<Place> places) {
        placeAdapter.updateData(places);
        placeCountView.setText(String.valueOf(places.size()));
    }

    @Override
    public void displayPlaceNotFound() {
        placeAdapter.clearData();
        placeCountView.setText(String.valueOf(0));
        Toast.makeText(getActivity(), R.string.place_not_found, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        openBuildingListActivity(position);
    }

    private void openBuildingListActivity(int position) {
        Intent intent = new Intent(getActivity(), BuildingListActivity.class);
        intent.putExtra(BuildingListActivity.PLACE_UUID_ARG, placeAdapter.getItem(position).getId().toString());
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        int selectedID = (int) placeTypeAdapter.getItemId(position);
        if (selectedID > 0) {
            placeChooser.getPlaceListWithPlaceFilter(selectedID);
        } else {
            placeChooser.getPlaceList();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
