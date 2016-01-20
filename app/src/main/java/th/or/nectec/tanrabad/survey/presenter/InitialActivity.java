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
import th.or.nectec.tanrabad.entity.*;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.TanrabadApp;
import th.or.nectec.tanrabad.survey.job.*;
import th.or.nectec.tanrabad.survey.repository.BuildingRepoBroker;
import th.or.nectec.tanrabad.survey.repository.InMemoryContainerTypeRepository;
import th.or.nectec.tanrabad.survey.repository.PlaceRepoBroker;
import th.or.nectec.tanrabad.survey.repository.persistence.CreateDatabaseJob;
import th.or.nectec.tanrabad.survey.repository.persistence.DbDistrictRepository;
import th.or.nectec.tanrabad.survey.repository.persistence.DbProvinceRepository;
import th.or.nectec.tanrabad.survey.repository.persistence.DbSubdistrictRepository;
import th.or.nectec.tanrabad.survey.service.*;

public class InitialActivity extends TanrabadActivity {

    WritableRepoUpdateJob<Province> provinceUpdateJob = new WritableRepoUpdateJob<>(new ProvinceRestService(), new DbProvinceRepository(TanrabadApp.getInstance()));
    WritableRepoUpdateJob<District> districtUpdateJob = new WritableRepoUpdateJob<>(new AmphurRestService(), new DbDistrictRepository(TanrabadApp.getInstance()));
    WritableRepoUpdateJob<Subdistrict> subDistrictUpdateJob = new WritableRepoUpdateJob<>(new TambonRestService(), new DbSubdistrictRepository(TanrabadApp.getInstance()));
    WritableRepoUpdateJob<Place> placeUpdateJob = new WritableRepoUpdateJob<>(new PlaceRestService(), PlaceRepoBroker.getInstance());
    WritableRepoUpdateJob<Building> buildingUpdateJob = new WritableRepoUpdateJob<>(new BuildingRestService(), BuildingRepoBroker.getInstance());
    private TextView loadingText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

        loadingText = (TextView) findViewById(R.id.loading);

        new InitialJobRunner()
                .addJob(new CreateDatabaseJob(this))
                .addJob(new InMemoryInitializeJob())
                .addJob(provinceUpdateJob)
                .addJob(districtUpdateJob)
                .addJob(subDistrictUpdateJob)
                .addJob(placeUpdateJob)
                .addJob(buildingUpdateJob)
                .addJob(new ContainerTypeUpdateJob(InMemoryContainerTypeRepository.getInstance()))
                .start();
    }

    public void updateLoadingText(Job startingJob) {
        switch (startingJob.id()) {
            case InMemoryInitializeJob.ID:
                loadingText.setText("หาลูกน้ำมาให้สำรวจ");
                break;
            case ContainerTypeUpdateJob.ID:
                loadingText.setText("ดึงประเภทภาชนะมา");
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
        else if (startingJob.equals(placeUpdateJob))
            loadingText.setText("สถานที่");
        else if (startingJob.equals(buildingUpdateJob))
            loadingText.setText("อาคาร");
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
            openMainActivityThenFinish();
        }
    }

}
