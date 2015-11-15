/*
 * Copyright (c) 2015 NECTEC
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

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import th.or.nectec.tanrabad.domain.PlaceChooser;
import th.or.nectec.tanrabad.domain.PlaceListPresenter;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.survey.repository.StubPlaceRepository;

import java.util.List;

public class PlaceListActivity extends TanrabadActivity implements AdapterView.OnItemClickListener, PlaceListPresenter {

    PlaceAdapter placeAdapter;
    PlaceChooser placeChooser;
    private TextView placeCountView;
    private ListView placeListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_list);

        this.placeListView = (ListView) findViewById(R.id.place_list);
        this.placeCountView = (TextView) findViewById(R.id.place_count);
        placeListView.setOnItemClickListener(this);

        placeChooser = new PlaceChooser(new StubPlaceRepository(), this);
        placeChooser.getPlaceList();

    }

    @Override
    public void displayPlaceList(List<Place> places) {
        placeAdapter = new PlaceAdapter(PlaceListActivity.this, places);
        placeListView.setAdapter(placeAdapter);
        placeCountView.setText(String.valueOf(places.size()));
    }

    @Override
    public void displayPlaceNotFound() {
        Toast.makeText(PlaceListActivity.this, R.string.place_not_found, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(PlaceListActivity.this, BuildingListActivity.class);
        intent.putExtra(BuildingListActivity.PLACE_UUID_ARG, placeAdapter.getItem(i).getId().toString());
        startActivity(intent);
    }
}
