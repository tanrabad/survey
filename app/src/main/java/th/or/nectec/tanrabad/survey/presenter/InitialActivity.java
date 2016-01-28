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

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import net.frakbot.jumpingbeans.JumpingBeans;
import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.entity.lookup.*;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.TanrabadApp;
import th.or.nectec.tanrabad.survey.job.*;
import th.or.nectec.tanrabad.survey.repository.BrokerBuildingRepository;
import th.or.nectec.tanrabad.survey.repository.BrokerContainerTypeRepository;
import th.or.nectec.tanrabad.survey.repository.BrokerPlaceRepository;
import th.or.nectec.tanrabad.survey.repository.persistence.*;
import th.or.nectec.tanrabad.survey.service.*;

public class InitialActivity extends TanrabadActivity {

    WritableRepoUpdateJob<Province> provinceUpdateJob = new WritableRepoUpdateJob<>(new ProvinceRestService(), DbProvinceRepository.getInstance());
    WritableRepoUpdateJob<District> districtUpdateJob = new WritableRepoUpdateJob<>(new AmphurRestService(), DbDistrictRepository.getInstance());
    WritableRepoUpdateJob<Subdistrict> subDistrictUpdateJob = new WritableRepoUpdateJob<>(new TambonRestService(), DbSubdistrictRepository.getInstance());
    WritableRepoUpdateJob<PlaceType> placeTypeUpdateJob = new WritableRepoUpdateJob<>(new PlaceTypeRestService(), new DbPlaceTypeRepository(TanrabadApp.getInstance()));
    WritableRepoUpdateJob<PlaceSubType> placeSubTypeUpdateJob = new WritableRepoUpdateJob<>(new PlaceSubTypeRestService(), new DbPlaceSubTypeRepository(TanrabadApp.getInstance()));
    WritableRepoUpdateJob<ContainerType> containerTypeUpdateJob = new WritableRepoUpdateJob<>(new ContainerTypeRestService(), BrokerContainerTypeRepository.getInstance());
    WritableRepoUpdateJob<ContainerLocation> containerLocationUpdateJob = new WritableRepoUpdateJob<>(new ContainerLocationRestService(), new DbContainerLocationRepository(TanrabadApp.getInstance()));
    WritableRepoUpdateJob<Place> placeUpdateJob = new WritableRepoUpdateJob<>(new PlaceRestService(), BrokerPlaceRepository.getInstance());
    WritableRepoUpdateJob<Building> buildingUpdateJob = new WritableRepoUpdateJob<>(new BuildingRestService(), BrokerBuildingRepository.getInstance());

    private TextView loadingText;
    private JumpingBeans pleaseWaitBeans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);
        loadingText = (TextView) findViewById(R.id.loading);
        startPleaseWaitBeansJump();
        new InitialJobRunner()
                .addJob(new CreateDatabaseJob(this))
                .addJob(new InMemoryInitializeJob())
                .addJob(new SetupScriptJob(this))
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
    }


    private void startPleaseWaitBeansJump() {
        pleaseWaitBeans = JumpingBeans.with((TextView) findViewById(R.id.please_wait))
                .appendJumpingDots()
                .build();
    }

    public void updateLoadingText(Job startingJob) {
        switch (startingJob.id()) {
            case InMemoryInitializeJob.ID:
                loadingText.setText("หาลูกน้ำมาให้สำรวจ");
                break;
            case SetupScriptJob.ID:
                loadingText.setText("ดึงประเภทย่อยสถานที่มา");
                break;
            case CreateDatabaseJob.ID:
                loadingText.setText("กำลังสร้างฐานข้อมูลชั่วคราว");
                break;
            case WritableRepoUpdateJob.ID:
                updateLoadingTextByWritableJobInstance(startingJob);
                break;
            default:
                loadingText.setText("ยังไงหละนี้");
                break;
        }
    }

    private void updateLoadingTextByWritableJobInstance(Job startingJob) {
        if (startingJob.equals(provinceUpdateJob))
            loadingText.setText("จังหวัด");
        else if (startingJob.equals(districtUpdateJob))
            loadingText.setText("อำเภอ");
        else if (startingJob.equals(subDistrictUpdateJob))
            loadingText.setText("ตำบล");
        else if (startingJob.equals(placeTypeUpdateJob))
            loadingText.setText("ประเภทสถานที่");
        else if (startingJob.equals(placeSubTypeUpdateJob))
            loadingText.setText("ประเภทย่อยสถานที่");
        else if (startingJob.equals(placeUpdateJob))
            loadingText.setText("สถานที่");
        else if (startingJob.equals(buildingUpdateJob))
            loadingText.setText("อาคาร");
        else if (startingJob.equals(containerTypeUpdateJob))
            loadingText.setText("ดึงประเภทภาชนะมา");
        else if (startingJob.equals(containerLocationUpdateJob))
            loadingText.setText("ดึงที่ตั้งภาชนะมา");
    }

    private void openMainActivityThenFinish() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public class InitialJobRunner extends AbsJobRunner {
        @Override
        protected void onJobStart(Job startingJob) {
            updateLoadingText(startingJob);
        }

        @Override
        protected void onRunFinish() {
            pleaseWaitBeans.stopJumping();
            openMainActivityThenFinish();
        }
    }

}
