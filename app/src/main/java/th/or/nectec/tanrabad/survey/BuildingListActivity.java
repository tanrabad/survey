/*
 * Copyright (c) 2015  NECTEC
 *   National Electronics and Computer Technology Center, Thailand
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

import th.or.nectec.tanrabad.domain.SurveyBuilding;
import th.or.nectec.tanrabad.domain.SurveyBuildingChooser;
import th.or.nectec.tanrabad.domain.SurveyBuildingListPresenter;
import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.survey.repository.InMemorySurveyRepository;
import th.or.nectec.tanrabad.survey.repository.StubBuildingRepository;
import th.or.nectec.tanrabad.survey.repository.StubPlaceRepository;
import th.or.nectec.tanrabad.survey.repository.StubUserRepository;

public class BuildingListActivity extends TanrabadActivity implements SurveyBuildingListPresenter {

    public static final String PLACE_UUID_ARG = "place_uuid_arg";
    private TextView placeName;
    private ListView buildingList;
    private TextView buildingCountView;
    private BuildingWithSurveyStatusAdapter buildingAdapter;
    private SurveyBuildingChooser surveyBuildingChooser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_list);

        placeName = (TextView) findViewById(R.id.place_name);
        buildingList = (ListView) findViewById(R.id.building_list);
        buildingCountView = (TextView) findViewById(R.id.building_count);

        buildingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SurveyBuilding building = (SurveyBuilding) buildingAdapter.getItem(position);
                bringToSurveyActivity(building.getBuilding());
            }
        });

        loadSurveyBuildingList();
    }

    private void loadSurveyBuildingList() {
        surveyBuildingChooser = new SurveyBuildingChooser(new StubUserRepository(), new StubPlaceRepository(), new StubBuildingRepository(), InMemorySurveyRepository.getInstance(), this);
        surveyBuildingChooser.displaySurveyBuildingOf(getUuidFromIntent().toString(), "sara");
    }

    private void bringToSurveyActivity(Building building) {
        Intent intent = new Intent(BuildingListActivity.this, SurveyActivity.class);
        intent.putExtra(SurveyActivity.BUILDING_UUID_ARG, building.getId().toString());
        intent.putExtra(SurveyActivity.USERNAME_ARG, "sara");
        startActivity(intent);
    }

    private UUID getUuidFromIntent() {
        String uuid = getIntent().getStringExtra(PLACE_UUID_ARG);
        return UUID.fromString(uuid);
    }

    @Override
    public void alertUserNotFound() {

    }

    @Override
    public void alertPlaceNotFound() {
        Toast.makeText(BuildingListActivity.this, R.string.please_enter_place, Toast.LENGTH_LONG).show();
    }

    @Override
    public void alertBuildingsNotFound() {
        Toast.makeText(BuildingListActivity.this, R.string.building_not_found, Toast.LENGTH_LONG).show();
    }

    @Override
    public void displayAllSurveyBuildingList(List<SurveyBuilding> surveyBuildingsWithStatus) {
        buildingAdapter = new BuildingWithSurveyStatusAdapter(BuildingListActivity.this, surveyBuildingsWithStatus);
        buildingList.setAdapter(buildingAdapter);
        buildingCountView.setText(String.valueOf(surveyBuildingsWithStatus.size()));
    }
}
