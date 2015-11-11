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
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.UUID;

import th.or.nectec.tanrabad.domain.ContainerController;
import th.or.nectec.tanrabad.domain.ContainerPresenter;
import th.or.nectec.tanrabad.domain.SurveyController;
import th.or.nectec.tanrabad.domain.SurveyPresenter;
import th.or.nectec.tanrabad.domain.SurveyRepository;
import th.or.nectec.tanrabad.domain.UserRepository;
import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.entity.ContainerType;
import th.or.nectec.tanrabad.entity.Survey;
import th.or.nectec.tanrabad.entity.User;
import th.or.nectec.tanrabad.survey.repository.InMemoryContainerTypeRepository;
import th.or.nectec.tanrabad.survey.repository.StubBuildingRepository;
import th.or.nectec.tanrabad.survey.repository.StubPlaceRepository;

public class SurveyActivity extends AppCompatActivity {

    Building surveyBuilding;
    User surveyUser;

    SurveyController surveyController;
    ContainerController containerController;

    TextView containerList;
    ContainerPresenter containerPresenter = new ContainerPresenter() {
        @Override
        public void showContainerList(List<ContainerType> containers) {
            String containerListStr = "";
            for (ContainerType eachContainer : containers) {
                containerListStr += eachContainer.toString() + "\n";
            }
            containerList.setText(containerListStr);
        }

        @Override
        public void showContainerNotFound() {
            containerList.setText("ไม่เจอรายการภาชนะ");
        }
    };
    SurveyPresenter surveyPresenter = new SurveyPresenter() {
        @Override
        public void onEditSurvey(Survey survey) {
            Toast.makeText(SurveyActivity.this, "แก้ไขสำรวจ", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onNewSurvey(Building building, User user) {
            surveyBuilding = building;
            surveyUser = user;
            Toast.makeText(SurveyActivity.this, "สำรวจใหม่", Toast.LENGTH_LONG).show();
        }

        @Override
        public void alertUserNotFound() {
            Toast.makeText(SurveyActivity.this, "ไม่พบข้อมูลผู้ใช้งาน", Toast.LENGTH_LONG).show();
        }

        @Override
        public void alertBuildingNotFound() {
            Toast.makeText(SurveyActivity.this, "ไม่พบข้อมูลอาคาร", Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        containerList = (TextView) findViewById(R.id.container_list);

        initSurvey();
    }

    private void initSurvey() {
        initController();
        containerController.showList();
        surveyController.checkThisBuildingAndUserCanSurvey(UUID.nameUUIDFromBytes("2xyz".getBytes()).toString(), "sara");
    }

    private void initController() {

        UserRepository userRepository = new UserRepository() {
            @Override
            public User findUserByName(String userName) {
                User user = User.fromUsername("sara");
                if (user.getUsername().equals(userName)) {
                    return user;
                }
                return null;
            }
        };

        SurveyRepository surveyRepository = new SurveyRepository() {
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

        containerController = new ContainerController(InMemoryContainerTypeRepository.getInstance(), containerPresenter);
        surveyController = new SurveyController(surveyRepository, new StubBuildingRepository(), userRepository, surveyPresenter);
    }

}
