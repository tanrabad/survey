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
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import th.or.nectec.tanrabad.domain.survey.*;
import th.or.nectec.tanrabad.entity.*;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.TanrabadApp;
import th.or.nectec.tanrabad.survey.presenter.view.SurveyContainerView;
import th.or.nectec.tanrabad.survey.repository.InMemoryBuildingRepository;
import th.or.nectec.tanrabad.survey.repository.InMemoryContainerTypeRepository;
import th.or.nectec.tanrabad.survey.repository.InMemorySurveyRepository;
import th.or.nectec.tanrabad.survey.repository.StubUserRepository;
import th.or.nectec.tanrabad.survey.utils.CameraFlashLight;
import th.or.nectec.tanrabad.survey.utils.EditTextStepper;
import th.or.nectec.tanrabad.survey.utils.Torch;
import th.or.nectec.tanrabad.survey.utils.alert.Alert;
import th.or.nectec.tanrabad.survey.utils.android.SoftKeyboard;
import th.or.nectec.tanrabad.survey.validator.SaveSurveyValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SurveyActivity extends TanrabadActivity implements ContainerPresenter, SurveyPresenter, SurveySavePresenter {

    public static final String BUILDING_UUID_ARG = "building_uuid";
    public static final String USERNAME_ARG = "username_arg";

    private HashMap<Integer, SurveyContainerView> indoorContainerViews;
    private HashMap<Integer, SurveyContainerView> outdoorContainerViews;
    private LinearLayout outdoorContainerLayout;
    private LinearLayout indoorContainerLayout;
    private EditText residentCountView;
    private SurveyRepository surveyRepository;
    private Survey survey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        setupToolbar();
        findViewsFromLayout();
        showContainerList();
        initSurvey();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void findViewsFromLayout() {
        indoorContainerLayout = (LinearLayout) findViewById(R.id.indoor_container);
        outdoorContainerLayout = (LinearLayout) findViewById(R.id.outdoor_container);
        residentCountView = (EditText) findViewById(R.id.resident_count);
    }

    private void showContainerList() {
        ContainerController containerController = new ContainerController(InMemoryContainerTypeRepository.getInstance(), this);
        containerController.showList();
    }

    private void initSurvey() {
        surveyRepository = InMemorySurveyRepository.getInstance();
        SurveyController surveyController = new SurveyController(surveyRepository, InMemoryBuildingRepository.getInstance(), new StubUserRepository(), this);

        String buildingUUID = getIntent().getStringExtra(BUILDING_UUID_ARG);
        String username = getIntent().getStringExtra(USERNAME_ARG);

        surveyController.checkThisBuildingAndUserCanSurvey(buildingUUID, username);
    }

    @Override
    public void onEditSurvey(Survey survey) {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null)
            supportActionBar.setTitle(R.string.title_activity_edit_survey);

        this.survey = survey;
        setBuildingInfo();
        loadSurveyData(survey);
    }

    @Override
    public void onNewSurvey(Building building, User user) {
        survey = new Survey(user, building);
        survey.startSurvey();

        setBuildingInfo();
    }

    @Override
    public void alertUserNotFound() {
        Toast.makeText(SurveyActivity.this, R.string.user_not_found, Toast.LENGTH_LONG).show();
    }

    @Override
    public void alertBuildingNotFound() {
        Toast.makeText(SurveyActivity.this, R.string.building_not_found, Toast.LENGTH_LONG).show();
    }

    @Override
    public void alertPlaceNotFound() {
        Toast.makeText(SurveyActivity.this, "ไม่พบข้อมูลสถานที่", Toast.LENGTH_LONG).show();
    }

    private void setBuildingInfo() {
        Building building = survey.getSurveyBuilding();
        ((TextView) findViewById(R.id.building_name)).setText(getBuildingNameWithPrefix(building));
        ((TextView) findViewById(R.id.place_name)).setText(building.getPlace().getName());
    }

    private void loadSurveyData(Survey survey) {
        residentCountView.setText(String.valueOf(survey.getResidentCount()));
        loadSurveyDetail(survey.getIndoorDetail(), indoorContainerViews);
        loadSurveyDetail(survey.getOutdoorDetail(), outdoorContainerViews);
    }

    private String getBuildingNameWithPrefix(Building building) {
        String houseNoPrefix = "บ้านเลขที่ ";
        String buildName = building.getName();
        Place place = survey.getSurveyBuilding().getPlace();
        if (place.getType() == Place.TYPE_VILLAGE_COMMUNITY) {
            buildName = houseNoPrefix + buildName;
        }
        return buildName;
    }

    private void loadSurveyDetail(List<SurveyDetail> indoorDetails, HashMap<Integer,SurveyContainerView> surveyContainerViews) {
        for (SurveyDetail eachDetail : indoorDetails) {
            SurveyContainerView surveyContainerView = surveyContainerViews.get(eachDetail.getContainerType().getId());
            surveyContainerView.setSurveyDetail(eachDetail);
        }
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
    public void alertContainerNotFound() {
        Toast.makeText(SurveyActivity.this, R.string.container_not_found, Toast.LENGTH_LONG).show();
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
    public void displaySaveSuccess() {
        Toast.makeText(SurveyActivity.this, R.string.save_success, Toast.LENGTH_LONG).show();
        finish();
        openSurveyBuildingHistory();
    }

    private void openSurveyBuildingHistory() {
        Intent intent = new Intent(SurveyActivity.this, SurveyBuildingHistoryActivity.class);
        intent.putExtra(SurveyBuildingHistoryActivity.PLACE_UUID_ARG, survey.getSurveyBuilding().getPlace().getId().toString());
        intent.putExtra(SurveyBuildingHistoryActivity.USERNAME_ARG, survey.getUser().getUsername());
        startActivity(intent);
    }

    @Override
    public void displaySaveFail() {
        Toast.makeText(SurveyActivity.this, R.string.save_fail, Toast.LENGTH_LONG).show();
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
                SaveSurveyData();
                break;
            case R.id.torch:
                toggleTorchLight();
        }
        return super.onOptionsItemSelected(item);
    }


    private void SaveSurveyData() {
        try {
            survey.setResidentCount(getResidentCount());
            survey.setIndoorDetail(getSurveyDetail(indoorContainerViews));
            survey.setOutdoorDetail(getSurveyDetail(outdoorContainerViews));
            survey.finishSurvey();

            SurveySaver surveySaver = new SurveySaver(this, new SaveSurveyValidator(this), surveyRepository);
            surveySaver.save(survey);
        } catch (SurveyDetail.ContainerFoundLarvaOverTotalException e) {
            Toast.makeText(SurveyActivity.this, R.string.over_total_container, Toast.LENGTH_LONG).show();
            validateSurveyContainerViews(indoorContainerViews);
            validateSurveyContainerViews(outdoorContainerViews);
            TanrabadApp.error().logException(e);
        }
    }

    private void toggleTorchLight() {
        Torch torch = CameraFlashLight.getInstance(this);
        if (!torch.isAvailable())
            return;

        if (torch.isTurningOn())
            torch.turnOff();
        else
            torch.turnOn();
    }

    private int getResidentCount() {
        String residentCountStr = residentCountView.getText().toString();
        return TextUtils.isEmpty(residentCountStr) ? 0 : Integer.valueOf(residentCountStr);
    }

    private ArrayList<SurveyDetail> getSurveyDetail(HashMap<Integer, SurveyContainerView> containerViews) {
        ArrayList<SurveyDetail> surveyDetails = new ArrayList<>();
        for (Map.Entry<Integer, SurveyContainerView> eachView : containerViews.entrySet()) {
            surveyDetails.add(eachView.getValue().getSurveyDetail());
        }
        return surveyDetails;
    }

    private boolean validateSurveyContainerViews(HashMap<Integer, SurveyContainerView> containerViews) {
        boolean isValid = true;
        for (Map.Entry<Integer, SurveyContainerView> eachView : containerViews.entrySet()) {
            if (!eachView.getValue().isValid()) isValid = false;
        }
        return isValid;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        try {
            switch (keyCode) {
                case KeyEvent.KEYCODE_VOLUME_UP:
                    EditTextStepper.stepUp((EditText) this.getCurrentFocus());
                    return true;
                case KeyEvent.KEYCODE_VOLUME_DOWN:
                    EditTextStepper.stepDown((EditText) this.getCurrentFocus());
                    return true;
                default:
                    return super.onKeyDown(keyCode, event);
            }
        } catch (NullPointerException | ClassCastException e) {
            Alert.lowLevel().show("กดที่ช่องสำหรับกรอกตัวเลข แล้วลองกด เพิ่ม+/ลด- เสียงดูจิ ");
        } catch (EditTextStepper.NotSupportEditTextInputTypeException nse) {
            //Do Nothing
        }
        return true;
    }

    @Override
    protected void onPause() {
        Torch torch = CameraFlashLight.getInstance(this);
        if (torch.isAvailable() && torch.isTurningOn())
            torch.turnOff();
        super.onPause();
    }

    public void onRootViewClick(View view) {
        SoftKeyboard.hideOn(this);
    }
}
