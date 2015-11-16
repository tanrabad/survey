package th.or.nectec.tanrabad.survey;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.UUID;

import th.or.nectec.tanrabad.domain.SurveyBuildingHistoryController;
import th.or.nectec.tanrabad.domain.SurveyBuildingPresenter;
import th.or.nectec.tanrabad.entity.Building;

public class SurveyBuildingHistoryActivity extends TanrabadActivity {

    public static final String SURVEY_ARG = "survey_arg";
    private TextView placeName;
    private ListView surveyBuildingHistoryList;
    private SurveyBuildingHistoryAdapter surveyBuildingHistoryAdapter;
    private SurveyBuildingHistoryController surveyBuildingHistoryController;
    private SurveyBuildingPresenter surveyBuildingPresenter = new SurveyBuildingPresenter() {
        @Override
        public void alertUserNotFound() {
            Toast.makeText(SurveyBuildingHistoryActivity.this, R.string.user_not_found, Toast.LENGTH_LONG).show();
        }

        @Override
        public void alertPlaceNotFound() {
            Toast.makeText(SurveyBuildingHistoryActivity.this, R.string.place_not_found, Toast.LENGTH_LONG).show();
        }

        @Override
        public void alertSurveyBuildingsNotFound() {
            Toast.makeText(SurveyBuildingHistoryActivity.this, R.string.survey_building_history_not_found, Toast.LENGTH_LONG).show();
        }

        @Override
        public void displaySurveyBuildingList(List<Building> buildings) {
            surveyBuildingHistoryAdapter = new SurveyBuildingHistoryAdapter(SurveyBuildingHistoryActivity.this, buildings);
            surveyBuildingHistoryList.setAdapter(surveyBuildingHistoryAdapter);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        placeName = (TextView) findViewById(R.id.place_name);
        surveyBuildingHistoryList = (ListView) findViewById(R.id.survey_building_history_list);

        surveyBuildingHistoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Building building = (Building) surveyBuildingHistoryAdapter.getItem(position);
                bringToSurveyActivity(building);
            }
        });
        surveyBuildingHistoryController = new SurveyBuildingHistoryController(new );
    }

    private void bringToSurveyActivity(Building building) {
        Intent intent = new Intent(SurveyBuildingHistoryActivity.this, SurveyActivity.class);
        intent.putExtra(SurveyActivity.BUILDING_UUID_ARG, building.getId().toString());
        intent.putExtra(SurveyActivity.USERNAME_ARG, "sara");
        startActivity(intent);
    }

    private UUID getUuidFromIntent() {
        String uuid = getIntent().getStringExtra(SURVEY_ARG);
        return UUID.fromString(uuid);
    }
}


