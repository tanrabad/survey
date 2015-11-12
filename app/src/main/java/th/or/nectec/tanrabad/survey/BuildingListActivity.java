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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import th.or.nectec.tanrabad.domain.BuildingChooser;
import th.or.nectec.tanrabad.domain.BuildingPresenter;
import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.survey.repository.StubBuildingRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BuildingListActivity extends AppCompatActivity {

    private TextView placeName;
    private ListView buildingList;
    private TextView buildingCountView;


    private BuildingAdapter buildingAdapter;
    private BuildingChooser buildingChooser;
    private BuildingPresenter buildingPresenter = new BuildingPresenter() {
        @Override
        public void displayBuildingList(List<Building> buildings) {
            buildingAdapter = new BuildingAdapter(BuildingListActivity.this, buildings);
            buildingList.setAdapter(buildingAdapter);
            buildingCountView.setText(String.valueOf(buildings.size()));
        }

        @Override
        public void displayNotFoundBuilding() {
            Toast.makeText(BuildingListActivity.this, "ไม่พบข้อมูลอาคาร", Toast.LENGTH_LONG).show();
        }

        @Override
        public void displayPleaseSpecityPlace() {
            Toast.makeText(BuildingListActivity.this, "โปรดระบุสถานที่", Toast.LENGTH_LONG).show();
        }

        @Override
        public void displayBuilding(Building building) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_list);

        placeName = (TextView) findViewById(R.id.place_name);
        buildingList = (ListView) findViewById(R.id.building_list);
        buildingCountView = (TextView) findViewById(R.id.building_count);

        buildingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Building building = (Building) buildingAdapter.getItem(position);
                Toast.makeText(BuildingListActivity.this, building.getName(), Toast.LENGTH_LONG).show();
            }
        });

        buildingChooser = new BuildingChooser(new StubBuildingRepository(), this.buildingPresenter);
        buildingChooser.showBuildingOf(getUuidFromIntent());
    }

    private UUID getUuidFromIntent() {
//        String uuid = getIntent().getStringExtra("place_uuid");
//        return UUID.fromString(uuid);
        return UUID.randomUUID();
    }

}
