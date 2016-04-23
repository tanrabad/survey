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
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;
import it.sephiroth.android.library.tooltip.Tooltip;
import it.sephiroth.android.library.tooltip.Typefaces;
import org.tanrabad.survey.TanrabadApp;
import org.tanrabad.survey.job.*;
import org.tanrabad.survey.presenter.view.EmptyLayoutView;
import org.tanrabad.survey.repository.BrokerPlaceRepository;
import org.tanrabad.survey.repository.BrokerSurveyRepository;
import org.tanrabad.survey.repository.BrokerUserRepository;
import org.tanrabad.survey.service.SurveyRestService;
import org.tanrabad.survey.utils.alert.Alert;
import org.tanrabad.survey.utils.android.InternetConnection;
import org.tanrabad.survey.utils.prompt.AlertDialogPromptMessage;
import org.tanrabad.survey.utils.prompt.PromptMessage;
import org.tanrabad.survey.utils.showcase.BaseShowcase;
import org.tanrabad.survey.utils.showcase.ShowcaseFactory;
import th.or.nectec.tanrabad.domain.entomology.HouseIndex;
import th.or.nectec.tanrabad.domain.place.PlaceController;
import th.or.nectec.tanrabad.domain.place.PlacePresenter;
import th.or.nectec.tanrabad.domain.survey.SurveyBuildingHistoryController;
import th.or.nectec.tanrabad.domain.survey.SurveyBuildingPresenter;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.entity.Survey;
import th.or.nectec.tanrabad.survey.R;

import java.util.List;
import java.util.UUID;

public class SurveyBuildingHistoryActivity extends TanrabadActivity implements SurveyBuildingPresenter, PlacePresenter {

    public static final String PLACE_UUID_ARG = "place_uuid_arg";
    private static final int NEED_REFRESH_REQ_CODE = 30001;
    private TextView placeName;
    private ImageButton surveyMoreBuildingButton;
    private SurveyBuildingHistoryAdapter surveyBuildingHistoryAdapter;
    private Place place;
    private EmptyLayoutView emptyLayoutView;

    public static void open(Activity activity, Place placeData) {
        Intent intent = new Intent(activity, SurveyBuildingHistoryActivity.class);
        intent.putExtra(SurveyBuildingHistoryActivity.PLACE_UUID_ARG, placeData.getId().toString());
        activity.startActivityForResult(intent, NEED_REFRESH_REQ_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_building_list);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        setupHomeButton();
        setupView();
        showPlaceInfo();
        setupBuildingHistoryList();
        setupEmptyLayout();
        showSurveyBuildingHistoryList();
        if (InternetConnection.isAvailable(this)) {
            startSyncJob();
        }
    }

    private void setupView() {
        placeName = (TextView) findViewById(R.id.place_name);
        surveyMoreBuildingButton = (ImageButton) findViewById(R.id.survey_more_building_button);
        surveyMoreBuildingButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                BuildingListActivity.open(SurveyBuildingHistoryActivity.this, getPlaceUuidFromIntent());
            }
        });
        startSurveyMoreBuildingButtonAnimation();
        displaySurveyMoreBuildingShowcase();
    }

    private String getPlaceUuidFromIntent() {
        return getIntent().getStringExtra(PLACE_UUID_ARG);
    }

    private void startSurveyMoreBuildingButtonAnimation() {
        Animation moreBuildingAnim = AnimationUtils.loadAnimation(this, R.anim.survey_more_building_button);
        moreBuildingAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Typeface typeface = Typefaces.get(SurveyBuildingHistoryActivity.this,
                        "fonts/ThaiSansNeue-Regular.otf");

                Tooltip.ClosePolicy policy = new Tooltip.ClosePolicy()
                        .insidePolicy(true, false)
                        .outsidePolicy(false, false);

                Tooltip.make(SurveyBuildingHistoryActivity.this,
                        new Tooltip.Builder(101)
                                .typeface(typeface)
                                .showDelay(5000)
                                .closePolicy(policy, 20000)
                                .withArrow(true)
                                .withOverlay(true)
                                .text(getString(R.string.choose_building_to_survey))
                                .withStyleId(R.style.ToolTipLayoutCustomStyle)
                                .anchor(surveyMoreBuildingButton, Tooltip.Gravity.LEFT)
                                .floatingAnimation(Tooltip.AnimationBuilder.SLOW)
                                .build()
                ).show();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        surveyMoreBuildingButton.startAnimation(moreBuildingAnim);
    }

    private void displaySurveyMoreBuildingShowcase() {
        BaseShowcase showcase = ShowcaseFactory.viewShowcase(R.id.survey_more_building_button);
        showcase.setTitle(getString(R.string.showcase_survey_more_building_title));
        showcase.setMessage(getString(R.string.showcase_survey_more_building));
        //showcase.display();
    }

    private void showPlaceInfo() {
        PlaceController placeController = new PlaceController(BrokerPlaceRepository.getInstance(), this);
        placeController.showPlace(UUID.fromString(getPlaceUuidFromIntent()));
    }

    private void setupBuildingHistoryList() {
        surveyBuildingHistoryAdapter = new SurveyBuildingHistoryAdapter(this,
                BuildingIcon.getWhite(place));
        RecyclerView surveyBuildingHistoryList = (RecyclerView) findViewById(R.id.survey_building_history_list);
        surveyBuildingHistoryList.setAdapter(surveyBuildingHistoryAdapter);
        surveyBuildingHistoryList.setLayoutManager(new LinearLayoutManager(this));
        surveyBuildingHistoryList.addItemDecoration(new SimpleDividerItemDecoration(this));
        surveyBuildingHistoryAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Survey survey = surveyBuildingHistoryAdapter.getItem(position);
                SurveyActivity.open(SurveyBuildingHistoryActivity.this, survey.getSurveyBuilding());
            }
        });
        surveyBuildingHistoryAdapter.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long id) {
                if (InternetConnection.isAvailable(SurveyBuildingHistoryActivity.this)) {
                    startSyncJob();
                    PromptMessage promptMessage = new AlertDialogPromptMessage(
                            SurveyBuildingHistoryActivity.this, R.mipmap.ic_delete);
                    promptMessage.setOnConfirm(getString(R.string.delete), new PromptMessage.OnConfirmListener() {
                        @Override
                        public void onConfirm() {
                            deleteSurvey(position);
                        }
                    });
                    promptMessage.setOnCancel(getString(R.string.cancel), null);
                    promptMessage.show(getString(R.string.delete_survey), getString(R.string.delete_survey_msg));
                    return true;
                } else {
                    Alert.highLevel().show(R.string.please_enable_internet_before_delete);
                    return false;
                }
            }
        });
        RecyclerViewHeader recyclerViewHeader = (RecyclerViewHeader) findViewById(R.id.card_header);

        recyclerViewHeader.attachTo(surveyBuildingHistoryList, true);
    }

    private void deleteSurvey(int position) {
        Survey survey = surveyBuildingHistoryAdapter.getItem(position);
        DeleteDataJob<Survey> dataJob = new DeleteDataJob<>(BrokerSurveyRepository.getInstance(),
                new SurveyRestService(), survey);
        SurveySyncJobRunner runner = new SurveySyncJobRunner();
        runner.addJob(dataJob);
        runner.start();
    }

    private void startSyncJob() {
        AbsJobRunner jobRunner = new SurveySyncJobRunner();
        jobRunner.addJobs(new UploadJobBuilder().getJobs());
        jobRunner.addJobs(new DownloadJobBuilder().getJobs());
        jobRunner.start();
    }

    private void setupEmptyLayout() {
        emptyLayoutView = (EmptyLayoutView) findViewById(R.id.empty_layout);
        emptyLayoutView.setEmptyButtonVisibility(false);
        emptyLayoutView.setEmptyText(R.string.survey_building_history_not_found);
    }

    private void showSurveyBuildingHistoryList() {
        SurveyBuildingHistoryController surveyBuildingHistoryController = new SurveyBuildingHistoryController(
                BrokerUserRepository.getInstance(),
                BrokerPlaceRepository.getInstance(),
                BrokerSurveyRepository.getInstance(),
                this);
        emptyLayoutView.showProgressBar();
        surveyBuildingHistoryController.showSurveyBuildingOf(getPlaceUuidFromIntent(),
                AccountUtils.getUser().getUsername());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.finish:
                TanrabadApp.action().finishSurvey(place, true);
                MainActivity.open(this);
                break;
            case android.R.id.home:
                TanrabadApp.action().finishSurvey(place, false);
                MainActivity.open(this);
                break;
        }
        return true;
    }

    @Override
    public void displayPlace(Place place) {
        this.place = place;
        placeName.setText(place.getName());
        ImageView icon = (ImageView) findViewById(R.id.building_icon);
        icon.setImageResource(BuildingIcon.get(place));
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
    public void displaySurveyBuildingsNotFound() {
        emptyLayoutView.showEmptyLayout();
        setResult(RESULT_OK);
        finish();
        BuildingListActivity.open(this, getPlaceUuidFromIntent());
    }

    @Override
    public void displaySurveyBuildingList(List<Survey> surveys) {
        emptyLayoutView.hide();
        TextView cardSubhead = (TextView) findViewById(R.id.place_subhead);
        HouseIndex hi = new HouseIndex(surveys);
        hi.calculate();
        cardSubhead.setText(Html.fromHtml(
                getString(R.string.format_house_survey, hi.getTotalSurveyHouse(), hi.getFoundLarvaeHouse())));
        surveyBuildingHistoryAdapter.updateData(surveys);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_activity_survey_building_history, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        showPlaceInfo();
        showSurveyBuildingHistoryList();
    }

    @Override
    public void onBackPressed() {
        TanrabadApp.action().finishSurvey(place, false);
        MainActivity.open(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        showSurveyBuildingHistoryList();
        if (InternetConnection.isAvailable(this)) {
            startSyncJob();
        }
    }

    public class SurveySyncJobRunner extends UploadJobRunner {

        private int successCount;

        @Override
        protected void onJobDone(Job job) {
            super.onJobDone(job);
            if (job instanceof AbsUploadJob) {
                successCount += ((AbsUploadJob) job).getSuccessCount();
            } else if (job instanceof DeleteDataJob) {
                successCount += ((DeleteDataJob) job).getSuccessCount();
            }
        }

        @Override
        protected void onRunFinish() {
            if (successCount > 0) {
                super.onRunFinish();
                showSurveyBuildingHistoryList();
            }
        }
    }
}


