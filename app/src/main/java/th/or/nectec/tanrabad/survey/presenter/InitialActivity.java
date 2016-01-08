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

import android.os.Bundle;
import android.widget.TextView;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.presenter.job.AbsJobRunner;
import th.or.nectec.tanrabad.survey.presenter.job.BuildingUpdateJob;
import th.or.nectec.tanrabad.survey.presenter.job.InMemoryInitializeJob;
import th.or.nectec.tanrabad.survey.presenter.job.Job;
import th.or.nectec.tanrabad.survey.repository.InMemoryBuildingRepository;
import th.or.nectec.tanrabad.survey.repository.persistence.CreateDatabaseJob;

public class InitialActivity extends TanrabadActivity {

    private TextView loadingText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

        loadingText = (TextView) findViewById(R.id.loading);

        new InitialJobRunner()
                .addJob(new CreateDatabaseJob(this))
                .addJob(new MockJob(1))
                .addJob(new InMemoryInitializeJob())
                .addJob(new MockJob(2))
                .addJob(new MockJob(3))
                .addJob(new BuildingUpdateJob(InMemoryBuildingRepository.getInstance()))
                .start();
    }

    public void updateLoadingText(Job startingJob) {
        switch (startingJob.id()) {
            case InMemoryInitializeJob.ID:
                loadingText.setText("หาลูกน้ำมาให้สำรวจ");
                break;
            case BuildingUpdateJob.ID:
                loadingText.setText("ตรวจสอบคุณภาพอาคาร");
                break;
            case CreateDatabaseJob.ID:
                loadingText.setText("กำลังสร้างฐานข้อมูลชั่วคราว");
                break;
            case 1:
                loadingText.setText("กำลังจะนอน");
                break;
            case 2:
                loadingText.setText("ง่วงสัสแล้วนะ");
                break;
            default:
                loadingText.setText("ยังไงหละนี้");
                break;
        }
    }

    private void openMainActivityThenFinish() {
        MainActivity.open(InitialActivity.this);
        finish();
    }

    public static class MockJob implements Job {

        private int id;

        public MockJob(int id) {
            this.id = id;
        }

        @Override
        public int id() {
            return id;
        }

        @Override
        public void execute() {
            try {
                Thread.sleep(2500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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
