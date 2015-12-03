/*
 * Copyright (c) 2015 NECTEC
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

package th.or.nectec.tanrabad.survey.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;

import java.util.List;
import java.util.UUID;

import th.or.nectec.tanrabad.domain.place.PlaceController;
import th.or.nectec.tanrabad.domain.place.PlacePresenter;
import th.or.nectec.tanrabad.domain.survey.SurveyBuildingHistoryController;
import th.or.nectec.tanrabad.domain.survey.SurveyBuildingPresenter;
import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.entity.Survey;
import th.or.nectec.tanrabad.entity.utils.HouseIndex;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.presenter.view.EmptyLayoutView;
import th.or.nectec.tanrabad.survey.repository.InMemorySurveyRepository;
import th.or.nectec.tanrabad.survey.repository.StubPlaceRepository;
import th.or.nectec.tanrabad.survey.repository.StubUserRepository;
import th.or.nectec.tanrabad.survey.utils.alert.Alert;
import th.or.nectec.tanrabad.survey.utils.prompt.AlertDialogPromptMessage;
import th.or.nectec.tanrabad.survey.utils.prompt.PromptMessage;

public class SurveyBuildingHistoryActivity extends TanrabadActivity implements SurveyBuildingPresenter, PlacePresenter {

    public static final String PLACE_UUID_ARG = "place_uuid_arg";
    public static final String USER_NAME_ARG = "username_arg";

    private TextView placeName;
    private Button surveyMoreBuildingButton;

    private SurveyBuildingHistoryAdapter surveyBuildingHistoryAdapter;
    private Place place;
    private TextView cardSubhead;
    private EmptyLayoutView emptyLayoutView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_building_list);

        placeName = (TextView) findViewById(R.id.place_name);

        surveyMoreBuildingButton = (Button) findViewById(R.id.survey_more_building_button);

        showPlaceInfo();
        setupBuildingHistoryList();
        setupEmptyLayout();
        showSurveyBuildingHistoryList();
    }

    private void showPlaceInfo() {
        PlaceController placeController = new PlaceController(new StubPlaceRepository(), this);
        placeController.showPlace(UUID.fromString(getPlaceUuidFromIntent()));
    }

    private void setupBuildingHistoryList() {
        surveyBuildingHistoryAdapter = new SurveyBuildingHistoryAdapter(this, BuildingIconMapping.getBuildingIcon(place));
        RecyclerView surveyBuildingHistoryList = (RecyclerView) findViewById(R.id.survey_building_history_list);
        surveyBuildingHistoryList.setAdapter(surveyBuildingHistoryAdapter);
        surveyBuildingHistoryList.setLayoutManager(new LinearLayoutManager(this));
        surveyBuildingHistoryList.addItemDecoration(new SimpleDividerItemDecoration(this));
        surveyBuildingHistoryAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Survey survey = surveyBuildingHistoryAdapter.getItem(position);
                openSurveyActivity(survey.getSurveyBuilding());
            }
        });
        RecyclerViewHeader recyclerViewHeader = (RecyclerViewHeader) findViewById(R.id.card_header);

        recyclerViewHeader.attachTo(surveyBuildingHistoryList, true);
    }

    private void setupEmptyLayout() {
        emptyLayoutView = (EmptyLayoutView) findViewById(R.id.empty_layout);
        emptyLayoutView.setEmptyButtonVisibility(false);
        emptyLayoutView.setEmptyText(R.string.survey_building_history_not_found);
    }

    private void showSurveyBuildingHistoryList() {
        SurveyBuildingHistoryController surveyBuildingHistoryController = new SurveyBuildingHistoryController(new StubUserRepository(),
                new StubPlaceRepository(),
                InMemorySurveyRepository.getInstance(),
                this);
        surveyBuildingHistoryController.showSurveyBuildingOf(getPlaceUuidFromIntent(), getUsernameFromIntent());
        surveyMoreBuildingButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                openBuildingListActivity();
            }
        });
    }

    private String getPlaceUuidFromIntent() {
        return getIntent().getStringExtra(PLACE_UUID_ARG);
    }

    private void openSurveyActivity(Building building) {
        Intent intent = new Intent(SurveyBuildingHistoryActivity.this, SurveyActivity.class);
        intent.putExtra(SurveyActivity.BUILDING_UUID_ARG, building.getId().toString());
        intent.putExtra(SurveyActivity.USERNAME_ARG, getUsernameFromIntent());
        startActivity(intent);
    }

    private String getUsernameFromIntent() {
        return getIntent().getStringExtra(USER_NAME_ARG);
    }

    private void openBuildingListActivity() {
        Intent intent = new Intent(SurveyBuildingHistoryActivity.this, BuildingListActivity.class);
        intent.putExtra(BuildingListActivity.PLACE_UUID_ARG, getPlaceUuidFromIntent());
        intent.putExtra(SurveyActivity.USERNAME_ARG, getUsernameFromIntent());
        startActivity(intent);
    }

    @Override
    public void displayPlace(Place place) {
        this.place = place;
        placeName.setText(place.getName());
    }

    @Override
    public void alertUserNotFound() {
        Alert.highLevel().show(R.string.user_not_found);
    }

    @Override
    public void alertPlaceNotFound() {
        Alert.highLevel().show(R.string.place_not_found);
    }

    @Override
    public void alertSurveyBuildingsNotFound() {
        emptyLayoutView.setVisibility(View.VISIBLE);
        surveyBuildingHistoryAdapter.clearData();
    }

    @Override
    public void displaySurveyBuildingList(List<Survey> surveys) {
        emptyLayoutView.setVisibility(View.GONE);
        cardSubhead = (TextView) findViewById(R.id.card_subhead);
        HouseIndex hi = new HouseIndex(surveys);
        hi.calculate();
        cardSubhead.setText(getString(R.string.format_house_survey, hi.getTotalSurveyHouse(), hi.getFoundLarvaeHouse()));
        surveyBuildingHistoryAdapter.updateData(surveys);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_activity_survey_building_history, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.finish:
                showFinishSurveyPrompt();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showFinishSurveyPrompt() {
        PromptMessage promptMessage = new AlertDialogPromptMessage(this);
        promptMessage.setOnConfirm(getString(R.string.confirm), new PromptMessage.OnConfirmListener() {
            @Override
            public void onConfirm() {
                openMainActivity();
            }
        });
        promptMessage.setOnCancel(getString(R.string.cancel), null);
        promptMessage.show(getString(R.string.finish_place_survey), place.getName());
    }

    private void openMainActivity() {
        Intent intent = new Intent(SurveyBuildingHistoryActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        showFinishSurveyPrompt();
    }
}


