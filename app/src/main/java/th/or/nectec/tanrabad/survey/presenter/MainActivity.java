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
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;

import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;

import java.util.ArrayList;

import th.or.nectec.tanrabad.domain.place.PlaceWithSurveyHistoryChooser;
import th.or.nectec.tanrabad.domain.place.PlaceWithSurveyHistoryListPresenter;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.repository.InMemorySurveyRepository;
import th.or.nectec.tanrabad.survey.repository.StubUserRepository;
import th.or.nectec.tanrabad.survey.utils.alert.Alert;

public class MainActivity extends TanrabadActivity implements View.OnClickListener, PlaceWithSurveyHistoryListPresenter, AdapterView.OnItemClickListener {

    private PlaceAdapter placeAdapter;
    private RecyclerView placeHistoryList;
    private CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupList();
        cardView = (CardView) findViewById(R.id.card_layout);
        Button startSurveyButton = (Button) findViewById(R.id.start_survey);
        startSurveyButton.setOnClickListener(this);
        PlaceWithSurveyHistoryChooser placeWithSurveyHistoryChooser = new PlaceWithSurveyHistoryChooser(
                new StubUserRepository(),
                InMemorySurveyRepository.getInstance(),
                this);
        placeWithSurveyHistoryChooser.showSurveyPlaceList(getUsername());

        if (!isUiTesting()) {
            startAnimation();
        }
    }

    private void setupList() {
        placeHistoryList = (RecyclerView) findViewById(R.id.place_history_list);
        placeAdapter = new PlaceAdapter(this);
        placeHistoryList.setAdapter(placeAdapter);
        placeHistoryList.setLayoutManager(new LinearLayoutManager(this));
        placeHistoryList.addItemDecoration(new SimpleDividerItemDecoration(this));
        placeAdapter.setOnItemClickListener(this);
        RecyclerViewHeader recyclerViewHeader = (RecyclerViewHeader) findViewById(R.id.card_header);
        recyclerViewHeader.attachTo(placeHistoryList, true);
    }

    @NonNull
    private String getUsername() {
        return "sara";
    }

    private void startAnimation() {
        View waterShadow = findViewById(R.id.water_shadow);
        Animation waterShadowRotate = AnimationUtils.loadAnimation(this, R.anim.water_shadow_rotate);
        waterShadow.startAnimation(waterShadowRotate);
    }

    @Override
    public void onClick(View view) {
        openPlaceActivity();
    }

    private void openPlaceActivity() {
        Intent intent = new Intent(MainActivity.this, PlaceListActivity.class);
        intent.putExtra(PlaceListActivity.USER_NAME_ARG, getUsername());
        startActivity(intent);
    }

    @Override
    public void displaySurveyPlaceList(ArrayList<Place> surveyPlace) {
        placeAdapter.updateData(surveyPlace);
        cardView.setVisibility(View.VISIBLE);
    }

    @Override
    public void alertUserNotFound() {
        Alert.highLevel().show(R.string.user_not_found);
    }

    @Override
    public void displaySurveyPlacesNotFound() {
        placeAdapter.clearData();
        cardView.setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Place place = placeAdapter.getItem(i);
        openSurveyBuildingHistoryActivity(place);
    }

    private void openSurveyBuildingHistoryActivity(Place place) {
        Intent intent = new Intent(MainActivity.this, SurveyBuildingHistoryActivity.class);
        intent.putExtra(SurveyBuildingHistoryActivity.PLACE_UUID_ARG, place.getId().toString());
        intent.putExtra(SurveyBuildingHistoryActivity.USER_NAME_ARG, getUsername());
        startActivity(intent);
    }
}
