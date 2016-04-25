/*
 * Copyright (c) 2016 NECTEC
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

package org.tanrabad.survey.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.tanrabad.survey.TanrabadApp;
import org.tanrabad.survey.entity.*;
import org.tanrabad.survey.presenter.view.AdvanceStepperDialog;
import org.tanrabad.survey.presenter.view.SurveyContainerView;
import org.tanrabad.survey.presenter.view.TorchButton;
import org.tanrabad.survey.repository.BrokerBuildingRepository;
import org.tanrabad.survey.repository.BrokerContainerTypeRepository;
import org.tanrabad.survey.repository.BrokerSurveyRepository;
import org.tanrabad.survey.repository.BrokerUserRepository;
import org.tanrabad.survey.utils.EditTextStepper;
import org.tanrabad.survey.utils.GpsUtils;
import org.tanrabad.survey.utils.MacAddressUtils;
import org.tanrabad.survey.utils.PlayLocationService;
import org.tanrabad.survey.utils.alert.Alert;
import org.tanrabad.survey.utils.android.SoftKeyboard;
import org.tanrabad.survey.utils.prompt.AlertDialogPromptMessage;
import org.tanrabad.survey.utils.prompt.PromptMessage;
import org.tanrabad.survey.validator.SaveSurveyValidator;
import org.tanrabad.survey.validator.ValidatorException;
import org.tanrabad.survey.domain.survey.*;
import org.tanrabad.survey.entity.field.Location;
import org.tanrabad.survey.entity.lookup.ContainerType;
import org.tanrabad.survey.entity.lookup.PlaceType;
import org.tanrabad.survey.entity.utils.UuidUtils;
import org.tanrabad.survey.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SurveyActivity extends TanrabadActivity implements ContainerPresenter, SurveyPresenter,
        SurveySavePresenter {

    public static final String BUILDING_UUID_ARG = "building_uuid";
    private static final String HOUSE_NO_PREFIX = "บ้านเลขที่ ";
    private static final int offsetStep = 80;
    private boolean firstLoad = true;
    private int containerViewAnimOffset = 240;
    private Map<Integer, SurveyContainerView> indoorContainerViews;
    private Map<Integer, SurveyContainerView> outdoorContainerViews;
    private LinearLayout outdoorContainerLayout;
    private LinearLayout indoorContainerLayout;
    private EditText residentCountView;
    private SurveyRepository surveyRepository;
    private Survey survey;
    private boolean isEditSurvey;
    private PlayLocationService locationService = PlayLocationService.getInstance();

    public static void open(Activity activity, Building building) {
        Intent intent = new Intent(activity, SurveyActivity.class);
        intent.putExtra(SurveyActivity.BUILDING_UUID_ARG, building.getId().toString());
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        setupToolbar();
        setupHomeButton();
        enableGps();
    }

    private void enableGps() {
        if (!GpsUtils.isGpsEnabled(this))
            GpsUtils.showGpsSettingsDialog(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationService.connect();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                SoftKeyboard.hideOn(this);
                saveSurveyData();
                break;
            case android.R.id.home:
                showAbortSurveyPrompt();
                break;
        }
        return true;
    }

    private void saveSurveyData() {
        try {
            survey.setResidentCount(getResidentCount());
            survey.setIndoorDetail(getSurveyDetail(indoorContainerViews));
            survey.setOutdoorDetail(getSurveyDetail(outdoorContainerViews));
            survey.setLocation(getCurrentLocation());
            survey.finishSurvey();
            if (isEditSurvey) {
                SurveySaver surveySaver = new SurveySaver(this, new SaveSurveyValidator(), surveyRepository);
                surveySaver.update(survey);
            } else {
                SurveySaver surveySaver = new SurveySaver(this, new SaveSurveyValidator(), surveyRepository);
                surveySaver.save(survey);
            }
        } catch (SurveyDetail.ContainerFoundLarvaOverTotalException exception) {
            Alert.highLevel().show(R.string.over_total_container);
            validateSurveyContainerViews(indoorContainerViews);
            validateSurveyContainerViews(outdoorContainerViews);
            TanrabadApp.log(exception);
        } catch (ValidatorException exception) {
            Alert.highLevel().show(exception.getMessageId());
            if (exception.getMessageId() == R.string.please_enter_resident)
                residentCountView.requestFocus();
        }
    }

    private void showAbortSurveyPrompt() {
        PromptMessage promptMessage = new AlertDialogPromptMessage(this);
        promptMessage.setOnCancel(getString(R.string.no), null);
        promptMessage.setOnConfirm(getString(R.string.yes), new PromptMessage.OnConfirmListener() {
            @Override
            public void onConfirm() {
                TanrabadApp.action().finishSurvey(survey, false);
                finish();
                if (!isEditSurvey)
                    BuildingListActivity.open(SurveyActivity.this, survey.getSurveyBuilding().getPlaceId().toString());
            }
        });
        promptMessage.show(getString(R.string.abort_survey), getBuildingNameWithPrefix(survey.getSurveyBuilding()));
    }

    private int getResidentCount() {
        String residentCountStr = residentCountView.getText().toString();
        return TextUtils.isEmpty(residentCountStr) ? 0 : Integer.valueOf(residentCountStr);
    }

    private List<SurveyDetail> getSurveyDetail(Map<Integer, SurveyContainerView> containerViews) {
        ArrayList<SurveyDetail> surveyDetails = new ArrayList<>();
        for (Map.Entry<Integer, SurveyContainerView> eachView : containerViews.entrySet()) {
            SurveyDetail surveyDetail = eachView.getValue().getSurveyDetail();
            if (surveyDetail != null) {
                surveyDetails.add(surveyDetail);
            }
        }
        return surveyDetails;
    }

    private Location getCurrentLocation() {
        android.location.Location gpsLocation = PlayLocationService.getInstance().getCurrentLocation();
        if (gpsLocation != null)
            return new Location(gpsLocation.getLatitude(), gpsLocation.getLongitude());
        return null;
    }

    private boolean validateSurveyContainerViews(Map<Integer, SurveyContainerView> containerViews) {
        boolean isValid = true;
        for (Map.Entry<Integer, SurveyContainerView> eachView : containerViews.entrySet()) {
            if (!eachView.getValue().isValid()) isValid = false;
        }
        return isValid;
    }

    private String getBuildingNameWithPrefix(Building building) {
        String buildName = building.getName();
        Place place = survey.getSurveyBuilding().getPlace();
        if (place.getType() == PlaceType.VILLAGE_COMMUNITY) {
            buildName = HOUSE_NO_PREFIX + buildName;
        }
        return buildName;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && firstLoad) {
            findViewsFromLayout();
            showContainerList();
            initSurvey();
            firstLoad = false;
        }
    }

    private void findViewsFromLayout() {
        indoorContainerLayout = (LinearLayout) findViewById(R.id.indoor_container);
        outdoorContainerLayout = (LinearLayout) findViewById(R.id.outdoor_container);
        residentCountView = (EditText) findViewById(R.id.resident_count);
        residentCountView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AdvanceStepperDialog(SurveyActivity.this, (TextView) view).show();
                return true;
            }
        });
    }

    private void showContainerList() {
        ContainerController containerController =
                new ContainerController(BrokerContainerTypeRepository.getInstance(), this);
        containerController.showList();
    }

    private void initSurvey() {
        surveyRepository = BrokerSurveyRepository.getInstance();
        SurveyController surveyController = new SurveyController(surveyRepository,
                BrokerBuildingRepository.getInstance(), BrokerUserRepository.getInstance(), this);

        String buildingUuid = getIntent().getStringExtra(BUILDING_UUID_ARG);
        String username = AccountUtils.getUser().getUsername();

        surveyController.checkThisBuildingAndUserCanSurvey(buildingUuid, username);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_activity_survey, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onEditSurvey(Survey survey) {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null)
            supportActionBar.setTitle(R.string.title_activity_edit_survey);

        isEditSurvey = true;
        this.survey = survey;
        TanrabadApp.action().updateSurvey(survey);
        setBuildingInfo();
        loadSurveyData(survey);
    }

    @Override
    public void onNewSurvey(Building building, User user) {
        survey = new Survey(UuidUtils.generateOrdered(MacAddressUtils.getMacAddress(this)), user, building);
        survey.startSurvey();
        isEditSurvey = false;
        TanrabadApp.action().startSurvey(survey);
        setBuildingInfo();
    }

    @Override
    public void alertUserNotFound() {
        Alert.highLevel().show(R.string.user_not_found);
    }

    @Override
    public void alertBuildingNotFound() {
        Alert.highLevel().show(R.string.building_not_found);
    }

    @Override
    public void alertPlaceNotFound() {
        Alert.highLevel().show(R.string.place_not_found);
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

    private void loadSurveyDetail(List<SurveyDetail> indoorDetails,
                                  Map<Integer, SurveyContainerView> surveyContainerViews) {
        for (SurveyDetail eachDetail : indoorDetails) {
            SurveyContainerView surveyContainerView = surveyContainerViews.get(eachDetail.getContainerType().getId());
            surveyContainerView.setSurveyDetail(eachDetail);
        }
    }

    @Override
    public void displayContainerList(List<ContainerType> containers) {
        indoorContainerLayout.removeAllViews();
        outdoorContainerLayout.removeAllViews();
        indoorContainerViews = new HashMap<>();
        outdoorContainerViews = new HashMap<>();

        for (ContainerType eachContainerType : containers) {
            displayContainerView(eachContainerType, indoorContainerViews, indoorContainerLayout);
            displayContainerView(eachContainerType, outdoorContainerViews, outdoorContainerLayout);
        }
    }

    @Override
    public void alertContainerNotFound() {
        Alert.highLevel().show(R.string.container_not_found);
    }

    private void displayContainerView(ContainerType containerType, Map<Integer, SurveyContainerView> containerViews,
                                      LinearLayout layout) {
        SurveyContainerView view = new SurveyContainerView(this, containerType);
        layout.addView(view);
        containerViews.put(containerType.getId(), view);
        view.startAnimation(containerViewEnterAnimation());
    }

    private Animation containerViewEnterAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.scale_in);
        animation.setStartOffset(containerViewAnimOffset);
        containerViewAnimOffset += offsetStep;
        return animation;
    }

    @Override
    public void displaySaveSuccess() {
        TanrabadApp.action().finishSurvey(survey, true);
        finish();
        SurveyBuildingHistoryActivity.open(this, survey.getSurveyBuilding().getPlace());
    }

    @Override
    public void displaySaveFail() {
        Alert.highLevel().show(R.string.save_survey_failed);
    }

    @Override
    public void displayUpdateSuccess() {
        finish();
        SurveyBuildingHistoryActivity.open(this, survey.getSurveyBuilding().getPlace());
    }

    @Override
    public void displayUpdateFail() {
        Alert.highLevel().show(R.string.save_survey_failed);
    }

    @Override
    public void onBackPressed() {
        showAbortSurveyPrompt();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        try {
            switch (keyCode) {
                case KeyEvent.KEYCODE_VOLUME_UP:
                case KeyEvent.KEYCODE_NUMPAD_ADD:
                case KeyEvent.KEYCODE_PLUS:
                    EditTextStepper.stepUp((EditText) this.getCurrentFocus());
                    return true;
                case KeyEvent.KEYCODE_VOLUME_DOWN:
                case KeyEvent.KEYCODE_NUMPAD_SUBTRACT:
                case KeyEvent.KEYCODE_MINUS:
                    EditTextStepper.stepDown((EditText) this.getCurrentFocus());
                    return true;
                default:
                    return super.onKeyDown(keyCode, event);
            }
        } catch (NullPointerException | ClassCastException exception) {
            Alert.lowLevel().show("กดที่ช่องสำหรับกรอกตัวเลข แล้วลองกด เพิ่ม+/ลด- เสียงดูจิ ");
        } catch (EditTextStepper.NotSupportEditTextInputTypeException nse) {
            TanrabadApp.log(nse);
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        ((TorchButton) findViewById(R.id.torch)).safeTurnOff();
        locationService.disconnect();
    }

    public void onRootViewClick(View view) {
        SoftKeyboard.hideOn(this);
    }
}
