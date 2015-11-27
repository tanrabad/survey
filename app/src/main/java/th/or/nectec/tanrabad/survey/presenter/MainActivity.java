package th.or.nectec.tanrabad.survey.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import th.or.nectec.tanrabad.domain.place.PlaceWithSurveyHistoryChooser;
import th.or.nectec.tanrabad.domain.place.PlaceWithSurveyHistoryListPresenter;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.repository.InMemorySurveyRepository;
import th.or.nectec.tanrabad.survey.repository.StubUserRepository;
import th.or.nectec.tanrabad.survey.utils.alert.Alert;

public class MainActivity extends TanrabadActivity implements View.OnClickListener, PlaceWithSurveyHistoryListPresenter {

    private PlaceAdapter placeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button startSurveyButton = (Button) findViewById(R.id.start_survey);
        setupList();
        startSurveyButton.setOnClickListener(this);
        PlaceWithSurveyHistoryChooser placeWithSurveyHistoryChooser = new PlaceWithSurveyHistoryChooser(
                new StubUserRepository(),
                InMemorySurveyRepository.getInstance(),
                this);
        placeWithSurveyHistoryChooser.showSurveyPlaceList("sara");
    }

    private void setupList() {
        RecyclerView placeHistoryList = (RecyclerView) findViewById(R.id.place_history_list);
        placeAdapter = new PlaceAdapter(this);
        placeHistoryList.setAdapter(placeAdapter);
        placeHistoryList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onClick(View view) {
        openPlaceActivity();
    }

    private void openPlaceActivity() {
        Intent intent = new Intent(MainActivity.this, PlaceListActivity.class);
        intent.putExtra(PlaceListActivity.USER_NAME_ARG, "sara");
        startActivity(intent);
    }

    @Override
    public void displaySurveyPlaceList(ArrayList<Place> surveyPlace) {
        placeAdapter.updateData(surveyPlace);
    }

    @Override
    public void alertUserNotFound() {
        Alert.highLevel().show(R.string.user_not_found);
    }

    @Override
    public void displaySurveyPlacesNotFound() {
        placeAdapter.clearData();
    }
}
