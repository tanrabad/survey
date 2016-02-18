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

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.annotation.IdRes;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;
import th.or.nectec.tanrabad.domain.place.PlaceWithSurveyHistoryChooser;
import th.or.nectec.tanrabad.domain.place.PlaceWithSurveyHistoryListPresenter;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.entity.User;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.TanrabadApp;
import th.or.nectec.tanrabad.survey.job.AbsJobRunner;
import th.or.nectec.tanrabad.survey.job.Job;
import th.or.nectec.tanrabad.survey.job.SyncJobBuilder;
import th.or.nectec.tanrabad.survey.repository.BrokerSurveyRepository;
import th.or.nectec.tanrabad.survey.repository.StubUserRepository;
import th.or.nectec.tanrabad.survey.utils.alert.Alert;
import th.or.nectec.tanrabad.survey.utils.android.NetworkChangeReceiver;

import java.util.List;

public class MainActivity extends TanrabadActivity implements View.OnClickListener,
        PlaceWithSurveyHistoryListPresenter, AdapterView.OnItemClickListener {

    private PlaceSurveyAdapter placeSurveyAdapter;
    private CardView cardView;
    private NetworkChangeReceiver networkChangeReceiver;
    private ObjectAnimator syncProgressAnimator;
    private PlaceWithSurveyHistoryChooser placeWithSurveyHistoryChooser;

    public static void open(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViewOnClick();
        setupNetworkChangeReceiver();
        setupList();
        setupSyncAnimator();
        showRecentSurveyCard();
        doLoadingRecentSurveyData();
        if (!isUiTesting()) {
            startAnimation();
        }
    }

    private void setupViewOnClick() {
        findViewById(R.id.start_survey).setOnClickListener(this);
        findViewById(R.id.root).setOnClickListener(this);
        findViewById(R.id.magnifier).setOnClickListener(this);
        findViewById(R.id.sync_data).setOnClickListener(this);
    }

    private void setupNetworkChangeReceiver() {
        networkChangeReceiver = new NetworkChangeReceiver(new NetworkChangeReceiver.OnNetworkChangedListener() {
            @Override
            public void onNetworkChanged(boolean isConnected) {
                if (isConnected) {
                    findViewById(R.id.sync_data).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.sync_data).setVisibility(View.GONE);
                }
            }
        });
        registerReceiver(networkChangeReceiver, NetworkChangeReceiver.getIntentFilter());
    }

    private void setupList() {
        RecyclerView placeHistoryList = (RecyclerView) findViewById(R.id.place_history_list);
        placeSurveyAdapter = new PlaceSurveyAdapter(this, getSupportFragmentManager());
        placeHistoryList.setAdapter(placeSurveyAdapter);
        placeHistoryList.setLayoutManager(new LinearLayoutManager(this));
        placeHistoryList.addItemDecoration(new SimpleDividerItemDecoration(this));
        placeSurveyAdapter.setOnItemClickListener(this);
        RecyclerViewHeader recyclerViewHeader = (RecyclerViewHeader) findViewById(R.id.card_header);
        recyclerViewHeader.attachTo(placeHistoryList, true);
    }

    private void setupSyncAnimator() {
        syncProgressAnimator = (ObjectAnimator) AnimatorInflater.loadAnimator(
                this, R.animator.water_spin);
        syncProgressAnimator.setTarget(findViewById(R.id.water_shadow));
    }

    private void showRecentSurveyCard() {
        cardView = (CardView) findViewById(R.id.card_layout);
        placeWithSurveyHistoryChooser = new PlaceWithSurveyHistoryChooser(
                new StubUserRepository(),
                BrokerSurveyRepository.getInstance(),
                this);
    }

    private void doLoadingRecentSurveyData() {
        User user = AccountUtils.getUser();
        if (user != null)
            placeWithSurveyHistoryChooser.showSurveyPlaceList(user.getUsername());
        else finish();
    }

    private void startAnimation() {
        startAnimation(R.id.magnifier, R.anim.magnifier);
        startAnimation(R.id.larvae, R.anim.larvae);
        startAnimation(R.id.water_shadow, R.anim.water_shadow);
        startAnimation(R.id.larvae_deep, R.anim.larvae_deep);
        startAnimation(R.id.start_survey_hint, R.anim.scale_in);
    }

    private void startAnimation(@IdRes int viewId, @AnimRes int animId) {
        Animation anim = AnimationUtils.loadAnimation(this, animId);
        findViewById(viewId).startAnimation(anim);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        doLoadingRecentSurveyData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkChangeReceiver);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_survey:
                PlaceListActivity.open(this);
                break;
            case R.id.magnifier:
            case R.id.root:
                startAnimation(R.id.larvae, R.anim.dook_digg);
                break;
            case R.id.sync_data:
                startOrResumeSyncAnimation();
                findViewById(R.id.sync_data).setEnabled(false);
                SyncJobBuilder.build(new SyncJobRunner()).start();
                break;
        }
    }

    private void startOrResumeSyncAnimation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && syncProgressAnimator.isPaused()) {
            syncProgressAnimator.resume();
        } else {
            syncProgressAnimator.start();
        }
    }

    @Override
    public void displaySurveyPlaceList(List<Place> surveyPlace) {
        placeSurveyAdapter.updateData(surveyPlace);
        cardView.setVisibility(View.VISIBLE);
    }

    @Override
    public void alertUserNotFound() {
        Alert.highLevel().show(R.string.user_not_found);
    }

    @Override
    public void displaySurveyPlacesNotFound() {
        placeSurveyAdapter.clearData();
        cardView.setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Place place = placeSurveyAdapter.getItem(position);
        SurveyBuildingHistoryActivity.open(this, place);
    }

    private void stopSyncAnimation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            syncProgressAnimator.pause();
        } else {
            syncProgressAnimator.cancel();
        }
    }

    public class SyncJobRunner extends AbsJobRunner {

        @Override
        protected void onJobError(Job errorJob, Exception exception) {
            super.onJobError(errorJob, exception);
            TanrabadApp.log(exception);
        }

        @Override
        protected void onJobStart(Job startingJob) {

        }

        @Override
        protected void onRunFinish() {
            findViewById(R.id.sync_data).setEnabled(true);
            stopSyncAnimation();
            if (errorJobs() == 0) {
                Alert.mediumLevel().show("ปรับปรุงข้อมูลเรียบร้อยแล้วนะ");
            } else {
                Alert.mediumLevel().show("ปรับปรุงข้อมูลไม่สำเร็จ ลองใหม่อีกครั้งนะ");
            }
        }
    }
}
