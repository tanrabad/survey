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

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import th.or.nectec.tanrabad.domain.ContainerController;
import th.or.nectec.tanrabad.domain.ContainerPresenter;
import th.or.nectec.tanrabad.domain.SurveyController;
import th.or.nectec.tanrabad.domain.SurveyPresenter;
import th.or.nectec.tanrabad.domain.SurveyRepository;
import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.entity.ContainerType;
import th.or.nectec.tanrabad.entity.Survey;
import th.or.nectec.tanrabad.entity.SurveyDetail;
import th.or.nectec.tanrabad.entity.User;
import th.or.nectec.tanrabad.survey.repository.InMemoryContainerTypeRepository;
import th.or.nectec.tanrabad.survey.repository.StubBuildingRepository;
import th.or.nectec.tanrabad.survey.repository.StubPlaceRepository;
import th.or.nectec.tanrabad.survey.repository.StubUserRepository;
import th.or.nectec.tanrabad.survey.view.SurveyContainerView;

public class SurveyActivity extends AppCompatActivity implements ContainerPresenter, SurveyPresenter {

    private Building surveyBuilding;
    private User surveyUser;

    private SurveyController surveyController;
    private ContainerController containerController;
    private HashMap<Integer, SurveyContainerView> indoorContainerViews;
    private HashMap<Integer, SurveyContainerView> outdoorContainerViews;
    private LinearLayout outdoorContainerLayout;
    private LinearLayout indoorContainerLayout;
    private TextView buildingNameView;
    private TextView placeNameView;
    private EditText residentCountView;
    private Survey surveyData;
    private SurveyRepository surveyRepository = new SurveyRepository() {
        @Override
        public boolean save(Survey survey) {
            return false;
        }

        @Override
        public Survey findByBuildingAndUserIn7Day(Building building, User user) {
            StubPlaceRepository stubPlaceRepository = new StubPlaceRepository();
            Building building2 = (new Building(UUID.nameUUIDFromBytes("2xyz".getBytes()), "214/44"));
            building2.setPlace(stubPlaceRepository.getPalazzettoVillage());
            return null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        findViewsFromLayout();

        showContainerList();
        initSurvey();
    }

    private void showContainerList() {
        containerController = new ContainerController(InMemoryContainerTypeRepository.getInstance(), this);
        containerController.showList();
    }

    private void findViewsFromLayout() {
        buildingNameView = (TextView) findViewById(R.id.building_name);
        placeNameView = (TextView) findViewById(R.id.place_name);
        indoorContainerLayout = (LinearLayout) findViewById(R.id.indoor_container);
        outdoorContainerLayout = (LinearLayout) findViewById(R.id.outdoor_container);
        residentCountView = (EditText) findViewById(R.id.resident_count);
    }

    private void initSurvey() {
        surveyController = new SurveyController(surveyRepository, new StubBuildingRepository(), new StubUserRepository(), this);
        surveyController.checkThisBuildingAndUserCanSurvey(UUID.nameUUIDFromBytes("2xyz".getBytes()).toString(), "sara");
    }

    @Override
    public void onNewSurvey(Building building, User user) {
        surveyBuilding = building;
        surveyUser = user;

        buildingNameView.setText(surveyBuilding.getName());
        placeNameView.setText(surveyBuilding.getPlace().getName());
    }

    @Override
    public void onEditSurvey(Survey survey) {
        Toast.makeText(SurveyActivity.this, "แก้ไขสำรวจ", Toast.LENGTH_LONG).show();
    }

    @Override
    public void alertUserNotFound() {
        Toast.makeText(SurveyActivity.this, "ไม่พบข้อมูลผู้ใช้งาน", Toast.LENGTH_LONG).show();
    }

    @Override
    public void alertBuildingNotFound() {
        Toast.makeText(SurveyActivity.this, "ไม่พบข้อมูลอาคาร", Toast.LENGTH_LONG).show();
    }

    @Override
    public void displayContainerList(List<ContainerType> containers) {
        initContainerView();
        for (ContainerType eachContainerType : containers) {
            buildIndoorContainerView(eachContainerType);
            buildOutdoorContainerView(eachContainerType);
        }
    }

    @Override
    public void displayContainerNotFound() {
        Toast.makeText(SurveyActivity.this, "ไม่เจอรายการภาชนะ", Toast.LENGTH_LONG).show();
    }

    private void initContainerView() {
        indoorContainerLayout.removeAllViews();
        outdoorContainerLayout.removeAllViews();
        indoorContainerViews = new HashMap<>();
        outdoorContainerViews = new HashMap<>();
    }

    private void buildIndoorContainerView(ContainerType containerType) {
        SurveyContainerView surveyContainerView = buildContainerView(containerType);
        indoorContainerViews.put(containerType.getId(), surveyContainerView);
        indoorContainerLayout.addView(surveyContainerView);
    }

    private void buildOutdoorContainerView(ContainerType containerType) {
        SurveyContainerView surveyContainerView = buildContainerView(containerType);
        outdoorContainerViews.put(containerType.getId(), surveyContainerView);
        outdoorContainerLayout.addView(surveyContainerView);
    }

    private SurveyContainerView buildContainerView(ContainerType containerType) {
        SurveyContainerView surveyContainerView = new SurveyContainerView(SurveyActivity.this);
        surveyContainerView.setContainerType(containerType);
        return surveyContainerView;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_save_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                if (surveyData == null) {
                    surveyData = new Survey(surveyUser, surveyBuilding);
                }

                String residentCountStr = residentCountView.getText().toString();
                int residentCount = TextUtils.isEmpty(residentCountStr) ? 0 : Integer.valueOf(residentCountStr);
                surveyData.setResidentCount(residentCount);
                surveyData.setIndoorDetail(buildSurveyDetail(indoorContainerViews));
                surveyData.setOutdoorDetail(buildSurveyDetail(outdoorContainerViews));
                surveyRepository.save(surveyData);

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private ArrayList<SurveyDetail> buildSurveyDetail(HashMap<Integer, SurveyContainerView> containerViews) {
        ArrayList<SurveyDetail> surveyDetails = new ArrayList<>();
        for (Map.Entry<Integer, SurveyContainerView> eachView : containerViews.entrySet()) {
            surveyDetails.add(eachView.getValue().getSurveyDetail());
        }
        return surveyDetails;
    }
}
