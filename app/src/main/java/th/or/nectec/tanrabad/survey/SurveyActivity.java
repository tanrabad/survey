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

import java.util.List;

import th.or.nectec.tanrabad.domain.ContainerController;
import th.or.nectec.tanrabad.domain.ContainerPresenter;
import th.or.nectec.tanrabad.entity.ContainerType;
import th.or.nectec.tanrabad.survey.repository.InMemoryContainerTypeRepository;

public class SurveyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        final TextView containerList = (TextView) findViewById(R.id.container_list);

        ContainerController containerController = new ContainerController(InMemoryContainerTypeRepository.getInstance(), new ContainerPresenter() {
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
        });

        containerController.showList();

    }

}
