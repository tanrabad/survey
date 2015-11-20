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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
import java.util.UUID;

import th.or.nectec.tanrabad.domain.BuildingListPresenter;
import th.or.nectec.tanrabad.domain.BuildingWithSurveyStatus;
import th.or.nectec.tanrabad.domain.PlaceController;
import th.or.nectec.tanrabad.domain.PlacePresenter;
import th.or.nectec.tanrabad.domain.SurveyBuildingChooser;
import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.survey.repository.InMemoryBuildingRepository;
import th.or.nectec.tanrabad.survey.repository.InMemorySurveyRepository;
import th.or.nectec.tanrabad.survey.repository.StubPlaceRepository;
import th.or.nectec.tanrabad.survey.repository.StubUserRepository;
import th.or.nectec.tanrabad.survey.utils.alert.Alert;

public class BuildingListActivity extends TanrabadActivity implements BuildingListPresenter, PlacePresenter {

    public static final String PLACE_UUID_ARG = "place_uuid_arg";
    public static final int ADD_BUILDING_REQ_CODE = 40000;
    private ListView buildingList;
    private TextView buildingCountView;
    private BuildingWithSurveyStatusAdapter buildingAdapter;
    private SurveyBuildingChooser surveyBuildingChooser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_list);

        buildingList = (ListView) findViewById(R.id.building_list);
        buildingCountView = (TextView) findViewById(R.id.building_count);

        buildingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BuildingWithSurveyStatus building = (BuildingWithSurveyStatus) buildingAdapter.getItem(position);
                openSurveyActivity(building.getBuilding());
            }
        });
        showPlaceName();
        loadSurveyBuildingList();
    }

    private void showPlaceName() {
        PlaceController placeController = new PlaceController(new StubPlaceRepository(), this);
        placeController.showPlace(getPlaceUuidFromIntent());
    }

    @Override
    public void displayPlace(Place place) {
        TextView placeName = (TextView) findViewById(R.id.place_name);
        placeName.setText(place.getName());
    }

    @Override
    public void alertPlaceNotFound() {
        Alert.highLevel().show(R.string.place_not_found);
    }

    private void loadSurveyBuildingList() {
        surveyBuildingChooser = new SurveyBuildingChooser(new StubUserRepository(), new StubPlaceRepository(), InMemoryBuildingRepository.getInstance(), InMemorySurveyRepository.getInstance(), this);
        surveyBuildingChooser.displaySurveyBuildingOf(getPlaceUuidFromIntent().toString(), "sara");
    }

    private void openSurveyActivity(Building building) {
        Intent intent = new Intent(BuildingListActivity.this, SurveyActivity.class);
        intent.putExtra(SurveyActivity.BUILDING_UUID_ARG, building.getId().toString());
        intent.putExtra(SurveyActivity.USERNAME_ARG, "sara");
        startActivity(intent);
    }

    private UUID getPlaceUuidFromIntent() {
        String uuid = getIntent().getStringExtra(PLACE_UUID_ARG);
        return UUID.fromString(uuid);
    }

    @Override
    public void alertUserNotFound() {

    }

    @Override
    public void alertBuildingsNotFound() {
        buildingCountView.setText(String.valueOf(0));
        Alert.lowLevel().show(R.string.building_not_found);
    }

    @Override
    public void displayAllSurveyBuildingList(List<BuildingWithSurveyStatus> buildingsWithSurveyStatuses) {
        buildingAdapter = new BuildingWithSurveyStatusAdapter(BuildingListActivity.this, buildingsWithSurveyStatuses);
        buildingList.setAdapter(buildingAdapter);
        buildingCountView.setText(String.valueOf(buildingsWithSurveyStatuses.size()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_add_building_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_building_menu:
                openBuildingAddActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openBuildingAddActivity() {
        Intent intent = new Intent(BuildingListActivity.this, BuildingAddActivity.class);
        intent.putExtra(PLACE_UUID_ARG, getIntent().getStringExtra(PLACE_UUID_ARG));
        startActivityForResult(intent, ADD_BUILDING_REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ADD_BUILDING_REQ_CODE:
                if (resultCode == RESULT_OK) {
                    loadSurveyBuildingList();
                }
        }
    }
}
