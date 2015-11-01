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
import th.or.nectec.tanrabad.domain.BuildingChooser;
import th.or.nectec.tanrabad.domain.BuildingPresenter;
import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.survey.repository.StubBuildingRepository;

import java.util.List;
import java.util.UUID;

public class BuildingListActivity extends AppCompatActivity {

    private TextView text;
    private BuildingPresenter buildingPresenter = new BuildingPresenter() {
        @Override
        public void showBuildingList(List<Building> buildings) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Building building : buildings) {
                stringBuilder.append(building.getName());
                stringBuilder.append("\n");
            }
            text.setText(stringBuilder.toString());
        }

        @Override
        public void showNotFoundBuilding() {
            text.setText("not found any building");

        }

        @Override
        public void showPleaseSpecityPlace() {
            text.setText("please specify place");
        }
    };
    BuildingChooser buildingChooser = new BuildingChooser(new StubBuildingRepository(), this.buildingPresenter);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_list);

        text = (TextView) findViewById(R.id.text);

        buildingChooser.showBuildingOf(getUuidFromIntent());
    }

    private UUID getUuidFromIntent() {
//        String uuid = getIntent().getStringExtra("place_uuid");
//        return UUID.fromString(uuid);
        return UUID.randomUUID();
    }


}
