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

package th.or.nectec.tanrabad.survey.presenter;

import android.app.Activity;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import org.joda.time.DateTime;
import th.or.nectec.tanrabad.domain.survey.*;
import th.or.nectec.tanrabad.entity.*;
import th.or.nectec.tanrabad.entity.field.Location;
import th.or.nectec.tanrabad.entity.lookup.ContainerType;
import th.or.nectec.tanrabad.entity.utils.UUIDUtils;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.TanrabadApp;
import th.or.nectec.tanrabad.survey.job.AbsJobRunner;
import th.or.nectec.tanrabad.survey.job.Job;
import th.or.nectec.tanrabad.survey.job.PostDataJob;
import th.or.nectec.tanrabad.survey.job.PutDataJob;
import th.or.nectec.tanrabad.survey.presenter.view.AdvanceStepperDialog;
import th.or.nectec.tanrabad.survey.presenter.view.SurveyContainerView;
import th.or.nectec.tanrabad.survey.presenter.view.TorchButton;
import th.or.nectec.tanrabad.survey.repository.BrokerBuildingRepository;
import th.or.nectec.tanrabad.survey.repository.BrokerContainerTypeRepository;
import th.or.nectec.tanrabad.survey.repository.StubUserRepository;
import th.or.nectec.tanrabad.survey.repository.persistence.DbBuildingRepository;
import th.or.nectec.tanrabad.survey.repository.persistence.DbPlaceRepository;
import th.or.nectec.tanrabad.survey.repository.persistence.DbSurveyRepository;
import th.or.nectec.tanrabad.survey.service.BuildingRestService;
import th.or.nectec.tanrabad.survey.service.PlaceRestService;
import th.or.nectec.tanrabad.survey.service.json.SurveyRestService;
import th.or.nectec.tanrabad.survey.utils.EditTextStepper;
import th.or.nectec.tanrabad.survey.utils.MacAddressUtils;
import th.or.nectec.tanrabad.survey.utils.SnackToast;
import th.or.nectec.tanrabad.survey.utils.alert.Alert;
import th.or.nectec.tanrabad.survey.utils.android.SoftKeyboard;
import th.or.nectec.tanrabad.survey.utils.prompt.AlertDialogPromptMessage;
import th.or.nectec.tanrabad.survey.utils.prompt.PromptMessage;
import th.or.nectec.tanrabad.survey.validator.SaveSurveyValidator;
import th.or.nectec.tanrabad.survey.validator.ValidatorException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SurveyActivity extends TanrabadActivity implements ContainerPresenter, SurveyPresenter, SurveySavePresenter,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public static final String BUILDING_UUID_ARG = "building_uuid";
    public static final String USERNAME_ARG = "username_arg";
    ContainerIconMapping containerIconMapping;
    private HashMap<Integer, SurveyContainerView> indoorContainerViews;
    private HashMap<Integer, SurveyContainerView> outdoorContainerViews;
    private LinearLayout outdoorContainerLayout;
    private LinearLayout indoorContainerLayout;
    private EditText residentCountView;
    private SurveyRepository surveyRepository;
    private Survey survey;
    private boolean isEditSurvey;
    private GoogleApiClient locationApiClient;
    private android.location.Location location;

    public static void open(Activity activity, Building building) {
        Intent intent = new Intent(activity, SurveyActivity.class);
        intent.putExtra(SurveyActivity.BUILDING_UUID_ARG, building.getId().toString());
        intent.putExtra(SurveyActivity.USERNAME_ARG, "dpc-user");
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        setupToolbar();
        setupHomeButton();
        findViewsFromLayout();
        showContainerList();
        initSurvey();

        if (isGpsEnabled()) {
            setupLocationAPI();
        } else {
            showGpsSettingsDialog();
        }
    }

    private void setupLocationAPI() {
        locationApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    private boolean isGpsEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Activity.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void showGpsSettingsDialog() {
        PromptMessage promptMessage = new AlertDialogPromptMessage(this);
        promptMessage.setOnConfirm(getString(R.string.enable_gps), new PromptMessage.OnConfirmListener() {
            @Override
            public void onConfirm() {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
        promptMessage.setOnCancel(getResources().getString(R.string.cancel), null);
        promptMessage.show(getString(R.string.gps_dialog_tilte), getString(R.string.gps_dialog_message));
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
        ContainerController containerController = new ContainerController(BrokerContainerTypeRepository.getInstance(), this);
        containerController.showList();
    }

    private void initSurvey() {
        surveyRepository = new DbSurveyRepository(this);
        SurveyController surveyController = new SurveyController(surveyRepository, BrokerBuildingRepository.getInstance(), new StubUserRepository(), this);

        String buildingUUID = getIntent().getStringExtra(BUILDING_UUID_ARG);
        String username = getIntent().getStringExtra(USERNAME_ARG);

        surveyController.checkThisBuildingAndUserCanSurvey(buildingUUID, username);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (locationApiClient != null) {
            locationApiClient.disconnect();
        }
    }

    @Override
    public void onEditSurvey(Survey survey) {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null)
            supportActionBar.setTitle(R.string.title_activity_edit_survey);

        isEditSurvey = true;
        this.survey = survey;
        setBuildingInfo();
        loadSurveyData(survey);
    }

    @Override
    public void onNewSurvey(Building building, User user) {
        survey = new Survey(UUIDUtils.generateOrdered(MacAddressUtils.getMacAddress(this)), user, building);
        survey.startSurvey();
        isEditSurvey = false;
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

    private String getBuildingNameWithPrefix(Building building) {
        String houseNoPrefix = "บ้านเลขที่ ";
        String buildName = building.getName();
        Place place = survey.getSurveyBuilding().getPlace();
        if (place.getType() == Place.TYPE_VILLAGE_COMMUNITY) {
            buildName = houseNoPrefix + buildName;
        }
        return buildName;
    }

    private void loadSurveyData(Survey survey) {
        residentCountView.setText(String.valueOf(survey.getResidentCount()));
        loadSurveyDetail(survey.getIndoorDetail(), indoorContainerViews);
        loadSurveyDetail(survey.getOutdoorDetail(), outdoorContainerViews);
    }

    private void loadSurveyDetail(List<SurveyDetail> indoorDetails, HashMap<Integer, SurveyContainerView> surveyContainerViews) {
        for (SurveyDetail eachDetail : indoorDetails) {
            SurveyContainerView surveyContainerView = surveyContainerViews.get(eachDetail.getContainerType().getId());
            surveyContainerView.setSurveyDetail(eachDetail);
        }
    }

    @Override
    public void displayContainerList(List<ContainerType> containers) {
        containerIconMapping = new ContainerIconMapping();
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
        surveyContainerView.setContainerIcon(containerIconMapping.getContainerIcon(containerType));
        indoorContainerViews.put(containerType.getId(), surveyContainerView);
        indoorContainerLayout.addView(surveyContainerView);
    }

    private SurveyContainerView buildContainerView(ContainerType containerType) {
        SurveyContainerView surveyContainerView = new SurveyContainerView(SurveyActivity.this);
        surveyContainerView.setContainerType(containerType);
        return surveyContainerView;
    }

    private void buildOutdoorContainerView(ContainerType containerType) {
        SurveyContainerView surveyContainerView = buildContainerView(containerType);
        surveyContainerView.setContainerIcon(containerIconMapping.getContainerIcon(containerType));
        outdoorContainerViews.put(containerType.getId(), surveyContainerView);
        outdoorContainerLayout.addView(surveyContainerView);
    }

    @Override
    public void displaySaveSuccess() {
        doUploadData();
        finish();
        openSurveyBuildingHistory();
    }

    private void openSurveyBuildingHistory() {
        Intent intent = new Intent(SurveyActivity.this, SurveyBuildingHistoryActivity.class);
        intent.putExtra(SurveyBuildingHistoryActivity.PLACE_UUID_ARG, survey.getSurveyBuilding().getPlace().getId().toString());
        intent.putExtra(SurveyBuildingHistoryActivity.USER_NAME_ARG, survey.getUser().getUsername());
        startActivity(intent);
    }

    @Override
    public void displaySaveFail() {
        Toast.makeText(SurveyActivity.this, R.string.save_fail, Toast.LENGTH_LONG).show();
    }

    @Override
    public void displayUpdateSuccess() {
        doUploadData();
        finish();
        openSurveyBuildingHistory();
    }

    @Override
    public void displayUpdateFail() {

    }

    private void doUploadData() {
        SurveyUpdateJob surveyUpdateJob = new SurveyUpdateJob();
        surveyUpdateJob.addJob(new PostDataJob<>(new DbPlaceRepository(this), new PlaceRestService()));
        surveyUpdateJob.addJob(new PostDataJob<>(new DbBuildingRepository(this), new BuildingRestService()));
        surveyUpdateJob.addJob(new PostDataJob<>(new DbSurveyRepository(this), new SurveyRestService()));
        surveyUpdateJob.addJob(new PutDataJob<>(new DbSurveyRepository(this), new SurveyRestService()));
        surveyUpdateJob.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_activity_survey, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                SaveSurveyData();
                break;
            case android.R.id.home:
                showAbortSurveyPrompt();
                break;
        }
        return true;
    }

    private void SaveSurveyData() {
        try {
            survey.setResidentCount(getResidentCount());
            survey.setIndoorDetail(getSurveyDetail(indoorContainerViews));
            survey.setOutdoorDetail(getSurveyDetail(outdoorContainerViews));
            survey.setLocation(getLastLocation());
            survey.finishSurvey();
            if (isEditSurvey) {
                SurveySaver surveySaver = new SurveySaver(this, new SaveSurveyValidator(this), surveyRepository);
                surveySaver.update(survey);
            } else {
                SurveySaver surveySaver = new SurveySaver(this, new SaveSurveyValidator(this), surveyRepository);
                surveySaver.save(survey);
            }
        } catch (SurveyDetail.ContainerFoundLarvaOverTotalException e) {
            Alert.highLevel().show(R.string.over_total_container);
            validateSurveyContainerViews(indoorContainerViews);
            validateSurveyContainerViews(outdoorContainerViews);
            TanrabadApp.error().logException(e);
        } catch (ValidatorException e) {
            Alert.highLevel().show(e.getMessageID());
            if (e.getMessageID() == R.string.please_enter_resident)
                residentCountView.requestFocus();
        }
    }

    private int getResidentCount() {
        String residentCountStr = residentCountView.getText().toString();
        return TextUtils.isEmpty(residentCountStr) ? 0 : Integer.valueOf(residentCountStr);
    }

    private ArrayList<SurveyDetail> getSurveyDetail(HashMap<Integer, SurveyContainerView> containerViews) {
        ArrayList<SurveyDetail> surveyDetails = new ArrayList<>();
        for (Map.Entry<Integer, SurveyContainerView> eachView : containerViews.entrySet()) {
            SurveyDetail surveyDetail = eachView.getValue().getSurveyDetail();
            if (surveyDetail != null) {
                surveyDetails.add(surveyDetail);
            }
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

    public Location getLastLocation() {
        if (location == null)
            return null;
        int minutes = 5;
        int minutesInMillis = minutes * 60000;
        long differentTimeInMinutes = (DateTime.now().getMillis() - location.getTime()) / minutesInMillis;
        return differentTimeInMinutes <= minutes ? new Location(location.getLatitude(), location.getLongitude()) : null;
    }

    private void showAbortSurveyPrompt() {
        PromptMessage promptMessage = new AlertDialogPromptMessage(this);
        promptMessage.setOnCancel(getString(R.string.no), null);
        promptMessage.setOnConfirm(getString(R.string.yes), new PromptMessage.OnConfirmListener() {
            @Override
            public void onConfirm() {
                finish();
            }
        });
        promptMessage.show(getString(R.string.abort_survey), getBuildingNameWithPrefix(survey.getSurveyBuilding()));
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
        } catch (NullPointerException | ClassCastException e) {
            Alert.lowLevel().show("กดที่ช่องสำหรับกรอกตัวเลข แล้วลองกด เพิ่ม+/ลด- เสียงดูจิ ");
        } catch (EditTextStepper.NotSupportEditTextInputTypeException nse) {
            //Do Nothing
        }
        return true;
    }

    @Override
    protected void onPause() {
        ((TorchButton) findViewById(R.id.torch)).safeTurnOff();
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (locationApiClient != null) {
            locationApiClient.connect();
        }
    }

    public void onRootViewClick(View view) {
        SoftKeyboard.hideOn(this);
    }

    @Override
    public void onConnected(Bundle bundle) {
        setupLocationUpdateService();
    }

    private void setupLocationUpdateService() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        final long UPDATE_INTERVAL_MS = 500;
        final long FASTEST_INTERVAL_MS = 100;
        locationRequest.setInterval(UPDATE_INTERVAL_MS);
        locationRequest.setFastestInterval(FASTEST_INTERVAL_MS);
        LocationServices.FusedLocationApi.requestLocationUpdates(
                locationApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Alert.highLevel().show("ไม่สามารถเชื่อมต่อ Google Play Services ได้");
    }

    @Override
    public void onLocationChanged(android.location.Location location) {
        this.location = location;
    }

    public class SurveyUpdateJob extends AbsJobRunner {

        @Override
        protected void onJobError(Job errorJob, Exception exception) {
            super.onJobError(errorJob, exception);
            Log.d(errorJob.toString(), exception.getMessage());
        }

        @Override
        protected void onJobStart(Job startingJob) {

        }

        @Override
        protected void onRunFinish() {
            SnackToast.make(SurveyActivity.this, "SUCCESS", Toast.LENGTH_LONG).show();
        }
    }
}