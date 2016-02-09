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

package th.or.nectec.tanrabad.survey.presenter;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.AdapterView;
import android.widget.TextView;
import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;
import th.or.nectec.tanrabad.domain.place.PlaceChooser;
import th.or.nectec.tanrabad.domain.place.PlaceListPresenter;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.presenter.view.EmptyLayoutView;
import th.or.nectec.tanrabad.survey.repository.BrokerPlaceRepository;
import th.or.nectec.tanrabad.survey.utils.alert.Alert;
import th.or.nectec.tanrabad.survey.utils.prompt.AlertDialogPromptMessage;
import th.or.nectec.tanrabad.survey.utils.prompt.PromptMessage;

import java.util.List;

public class PlaceListInDatabaseFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, AdapterView.OnItemSelectedListener, PlaceListPresenter {

    private PlaceAdapter placeAdapter;
    private PlaceTypeAdapter placeTypeAdapter;
    private PlaceChooser placeChooser = new PlaceChooser(BrokerPlaceRepository.getInstance(), this);
    private TextView placeCountView;
    private RecyclerView placeListView;
    private AppCompatSpinner placeTypeFilterView;
    private RecyclerViewHeader recyclerViewHeader;
    private EmptyLayoutView emptyLayoutView;

    private int placeTypeID = -1;

    public static PlaceListInDatabaseFragment newInstance() {
        PlaceListInDatabaseFragment fragment = new PlaceListInDatabaseFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void displayPlaceList(List<Place> places) {
        placeAdapter.updateData(places);
        placeCountView.setText(getString(R.string.format_place_count, places.size()));
        placeCountView.setVisibility(View.VISIBLE);
        emptyLayoutView.setVisibility(View.GONE);
    }

    @Override
    public void displayPlaceNotFound() {
        placeAdapter.clearData();
        placeCountView.setVisibility(View.GONE);
        emptyLayoutView.setVisibility(View.VISIBLE);
    }


    private String getUsername() {
        return "sara";
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        placeTypeID = (int) placeTypeAdapter.getItemId(position);
        loadPlaceList();
    }

    protected void loadPlaceList() {
        if (placeTypeID > 0) {
            placeChooser.getPlaceListWithPlaceTypeFilter(this.placeTypeID);
        } else {
            placeChooser.getPlaceList();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_place_list_in_database, container, false);
        setupViews(view);
        setupEmptyList();
        setupPlaceFilterSpinner();
        setupPlaceList();
        setHasOptionsMenu(true);
        return view;
    }

    private void setupViews(View view) {
        this.placeListView = (RecyclerView) view.findViewById(R.id.place_list);
        this.placeCountView = (TextView) view.findViewById(R.id.place_count);
        this.placeTypeFilterView = (AppCompatSpinner) view.findViewById(R.id.place_filter);
        recyclerViewHeader = (RecyclerViewHeader) view.findViewById(R.id.card_header);
        emptyLayoutView = (EmptyLayoutView) view.findViewById(R.id.empty_layout);
        emptyLayoutView.setEmptyIcon(R.mipmap.ic_place);
    }

    private void setupEmptyList() {
        emptyLayoutView.setEmptyButtonText(R.string.add_place, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlaceFormActivity.startAdd(getActivity(), placeTypeID);
            }
        });
        emptyLayoutView.setEmptyText(R.string.places_not_found);
    }

    private void setupPlaceFilterSpinner() {
        placeTypeFilterView.setOnItemSelectedListener(this);
        placeTypeAdapter = new PlaceTypeAdapter(getActivity());
        placeTypeFilterView.setAdapter(placeTypeAdapter);
    }

    private void setupPlaceList() {
        placeAdapter = new PlaceAdapter(getActivity());
        placeAdapter.setOnItemClickListener(this);
        placeAdapter.setOnItemLongClickListener(this);
        placeListView.setAdapter(placeAdapter);
        placeListView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        placeListView.setLayoutManager(linearLayoutManager);
        recyclerViewHeader.attachTo(placeListView, true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.action_activity_place_add, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_place_menu:
                PlaceFormActivity.startAdd(getActivity(), placeTypeID);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
        final Place placeData = placeAdapter.getItem(position);
        PromptMessage promptMessage = new AlertDialogPromptMessage(getActivity());
        promptMessage.setOnConfirm(getString(R.string.survey), new PromptMessage.OnConfirmListener() {
            @Override
            public void onConfirm() {
                SurveyBuildingHistoryActivity.open(getActivity(), placeData);
            }
        });
        promptMessage.setOnCancel(getString(R.string.cancel), null);
        promptMessage.show(getString(R.string.start_survey), placeAdapter.getItem(position).getName());
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
        final Place placeData = placeAdapter.getItem(position);

        if (placeData.getType() == Place.TYPE_VILLAGE_COMMUNITY) {
            Alert.lowLevel().show(R.string.cant_edit_community_village);
            return false;
        } else {
            PlaceFormActivity.startEdit(getActivity(), placeData);
            return true;
        }
    }
}
