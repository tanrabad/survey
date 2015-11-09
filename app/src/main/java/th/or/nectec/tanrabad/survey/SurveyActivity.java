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

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import th.or.nectec.tanrabad.domain.ContainerController;
import th.or.nectec.tanrabad.domain.ContainerPresenter;
import th.or.nectec.tanrabad.domain.ContainerRepository;
import th.or.nectec.tanrabad.entity.Container;

import java.util.ArrayList;
import java.util.List;

public class SurveyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        final TextView containerList = (TextView) findViewById(R.id.container_list);

        ContainerController containerController = new ContainerController(new ContainerRepository() {
            @Override
            public List<Container> find() {
                ArrayList<Container> containers = new ArrayList<>();
                containers.add(new Container(1));
                containers.add(new Container(2));
                containers.add(new Container(3));
                return containers;
            }
        }, new ContainerPresenter() {
            @Override
            public void showContainerList(List<Container> containers) {
                String containerListStr = "";
                for (Container eachContainer : containers) {
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
