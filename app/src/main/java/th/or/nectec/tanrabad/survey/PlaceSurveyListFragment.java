package th.or.nectec.tanrabad.survey;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import th.or.nectec.tanrabad.domain.PlaceWithSurveyStatusChooser;
import th.or.nectec.tanrabad.domain.PlaceWithSurveyStatusChooserPresenter;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.survey.repository.InMemorySurveyRepository;
import th.or.nectec.tanrabad.survey.repository.StubUserRepository;
import th.or.nectec.tanrabad.survey.utils.alert.Alert;

public class PlaceSurveyListFragment extends Fragment implements PlaceWithSurveyStatusChooserPresenter, AdapterView.OnItemClickListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String USERNAME_ARG = "username_arg";
    private String username;

    private PlaceAdapter placeAdapter;
    private PlaceWithSurveyStatusChooser placeChooser = new PlaceWithSurveyStatusChooser(new StubUserRepository(), InMemorySurveyRepository.getInstance(), this);
    private TextView placeCountView;
    private ListView placeListView;

    public static PlaceSurveyListFragment newInstance(String username) {
        PlaceSurveyListFragment fragment = new PlaceSurveyListFragment();
        Bundle args = new Bundle();
        args.putString(USERNAME_ARG, username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString(USERNAME_ARG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place_survey_list, container, false);
        setupViews(view);
        setupPlaceList();
        placeChooser.showSurveyPlaceList(username);
        return view;
    }

    private void setupViews(View view) {
        this.placeListView = (ListView) view.findViewById(R.id.place_list);
        this.placeCountView = (TextView) view.findViewById(R.id.place_count);
    }

    private void setupPlaceList() {
        placeListView.setOnItemClickListener(this);
        placeAdapter = new PlaceAdapter(getActivity());
        placeListView.setAdapter(placeAdapter);
    }

    @Override
    public void displaySurveyPlaceList(ArrayList<Place> surveyPlace) {
        placeAdapter.updateData(surveyPlace);
        placeCountView.setText(String.valueOf(surveyPlace.size()));
    }

    @Override
    public void alertUserNotFound() {
        Alert.highLevel().show(R.string.user_not_found);
    }

    @Override
    public void displaySurveyPlacesNotFound() {
        placeAdapter.clearData();
        placeCountView.setText(String.valueOf(0));
        Alert.lowLevel().show(R.string.survey_place_not_found);
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

}
