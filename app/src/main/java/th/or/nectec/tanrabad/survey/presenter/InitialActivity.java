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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import net.frakbot.jumpingbeans.JumpingBeans;
import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.entity.lookup.*;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.TanrabadApp;
import th.or.nectec.tanrabad.survey.job.AbsJobRunner;
import th.or.nectec.tanrabad.survey.job.InMemoryInitializeJob;
import th.or.nectec.tanrabad.survey.job.Job;
import th.or.nectec.tanrabad.survey.job.WritableRepoUpdateJob;
import th.or.nectec.tanrabad.survey.repository.*;
import th.or.nectec.tanrabad.survey.repository.persistence.*;
import th.or.nectec.tanrabad.survey.service.*;
import th.or.nectec.tanrabad.survey.utils.alert.Alert;
import th.or.nectec.tanrabad.survey.utils.android.InternetConnection;

import java.io.IOException;

public class InitialActivity extends TanrabadActivity {

    private WritableRepoUpdateJob<Province> provinceUpdateJob = new WritableRepoUpdateJob<>(
            new ProvinceRestService(), DbProvinceRepository.getInstance());
    private WritableRepoUpdateJob<District> districtUpdateJob = new WritableRepoUpdateJob<>(
            new AmphurRestService(), DbDistrictRepository.getInstance());
    private WritableRepoUpdateJob<Subdistrict> subDistrictUpdateJob = new WritableRepoUpdateJob<>(
            new TambonRestService(), DbSubdistrictRepository.getInstance());
    private WritableRepoUpdateJob<PlaceType> placeTypeUpdateJob = new WritableRepoUpdateJob<>(
            new PlaceTypeRestService(), BrokerPlaceTypeRepository.getInstance());
    private WritableRepoUpdateJob<PlaceSubType> placeSubTypeUpdateJob = new WritableRepoUpdateJob<>(
            new PlaceSubTypeRestService(), BrokerPlaceSubTypeRepository.getInstance());
    private WritableRepoUpdateJob<ContainerType> containerTypeUpdateJob = new WritableRepoUpdateJob<>(
            new ContainerTypeRestService(), BrokerContainerTypeRepository.getInstance());
    private WritableRepoUpdateJob<ContainerLocation> containerLocationUpdateJob = new WritableRepoUpdateJob<>(
            new ContainerLocationRestService(), new DbContainerLocationRepository(TanrabadApp.getInstance()));
    private WritableRepoUpdateJob<Place> placeUpdateJob = new WritableRepoUpdateJob<>(
            new PlaceRestService(), BrokerPlaceRepository.getInstance());
    private WritableRepoUpdateJob<Building> buildingUpdateJob = new WritableRepoUpdateJob<>(
            new BuildingRestService(), BrokerBuildingRepository.getInstance());

    private TextView loadingText;
    private JumpingBeans pleaseWaitBeans;

    public static void open(Activity activity) {
        Intent intent = new Intent(activity, InitialActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.drop_in, R.anim.drop_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);
        loadingText = (TextView) findViewById(R.id.loading);
        startPleaseWaitBeansJump();

        if (InternetConnection.isAvailable(this)) {
            new InitialJobRunner()
                    .addJob(new CreateDatabaseJob(this))
                    .addJob(new InMemoryInitializeJob())
                    .addJob(containerLocationUpdateJob)
                    .addJob(containerTypeUpdateJob)
                    .addJob(provinceUpdateJob)
                    .addJob(districtUpdateJob)
                    .addJob(subDistrictUpdateJob)
                    .addJob(placeTypeUpdateJob)
                    .addJob(placeSubTypeUpdateJob)
                    .addJob(placeUpdateJob)
                    .addJob(buildingUpdateJob)
                    .start();
        } else {
            MainActivity.open(InitialActivity.this);
            finish();
        }
    }


    private void startPleaseWaitBeansJump() {
        pleaseWaitBeans = JumpingBeans.with((TextView) findViewById(R.id.please_wait))
                .appendJumpingDots()
                .build();
    }

    @SuppressLint("SetTextI18n")
    private void updateLoadingText(Job startingJob) {
        switch (startingJob.id()) {
            case InMemoryInitializeJob.ID:
                loadingText.setText("ทำสมาธิ");
                break;
            case CreateDatabaseJob.ID:
                loadingText.setText("เตรียมกระดาษสำหรับจดข้อมูล");
                break;
            case WritableRepoUpdateJob.ID:
                updateLoadingTextByWritableJobInstance(startingJob);
                break;
            default:
                loadingText.setText("มายังไงหละนี้");
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    private void updateLoadingTextByWritableJobInstance(Job startingJob) {
        if (startingJob.equals(provinceUpdateJob))
            loadingText.setText("รอพบกับท่านผู้ว่า");
        else if (startingJob.equals(districtUpdateJob))
            loadingText.setText("ไปหานายอำเภอ");
        else if (startingJob.equals(subDistrictUpdateJob))
            loadingText.setText("กินข้าวกับ อบต.");
        else if (startingJob.equals(placeTypeUpdateJob))
            loadingText.setText("เข้าเทศบาล");
        else if (startingJob.equals(placeSubTypeUpdateJob))
            loadingText.setText("คุยกับคนพื้นที่");
        else if (startingJob.equals(containerTypeUpdateJob))
            loadingText.setText("ค้นกระเป๋าโดเรม่อน");
        else if (startingJob.equals(containerLocationUpdateJob))
            loadingText.setText("หาตำแหน่งเป้าหมาย");
        else if (startingJob.equals(placeUpdateJob))
            loadingText.setText("สแกนพื้นที่ปฎิบัติงาน");
        else if (startingJob.equals(buildingUpdateJob))
            loadingText.setText("เตรียมตัวกำจัดเหล่าร้าย");
    }

    public class InitialJobRunner extends AbsJobRunner {

        IOException ioException;
        RestServiceException restServiceException;

        @Override
        protected void onJobError(Job errorJob, Exception exception) {
            super.onJobError(errorJob, exception);
            if (exception instanceof IOException)
                ioException = (IOException) exception;
            else if (exception instanceof RestServiceException)
                restServiceException = (RestServiceException) exception;
            if (InternetConnection.isAvailable(InitialActivity.this)) TanrabadApp.log(exception);
        }

        @Override
        protected void onJobStart(Job startingJob) {
            updateLoadingText(startingJob);
        }

        @Override
        protected void onRunFinish() {
            pleaseWaitBeans.stopJumping();
            MainActivity.open(InitialActivity.this);
            finish();
            showErrorMessage();
        }

        private void showErrorMessage() {
            if (ioException != null)
                Alert.mediumLevel().show(R.string.error_connection_problem);
            else if (restServiceException != null)
                Alert.mediumLevel().show(R.string.error_rest_service);
        }
    }
}
