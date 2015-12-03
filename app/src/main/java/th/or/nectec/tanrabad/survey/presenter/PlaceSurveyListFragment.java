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


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;

import java.util.ArrayList;

import th.or.nectec.tanrabad.domain.place.PlaceWithSurveyHistoryChooser;
import th.or.nectec.tanrabad.domain.place.PlaceWithSurveyHistoryListPresenter;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.presenter.view.EmptyLayoutView;
import th.or.nectec.tanrabad.survey.repository.InMemorySurveyRepository;
import th.or.nectec.tanrabad.survey.repository.StubUserRepository;
import th.or.nectec.tanrabad.survey.utils.alert.Alert;

public class PlaceSurveyListFragment extends Fragment implements PlaceWithSurveyHistoryListPresenter, AdapterView.OnItemClickListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String USERNAME_ARG = "username_arg";
    private String username;

    private PlaceAdapter placeAdapter;
    private PlaceWithSurveyHistoryChooser placeChooser = new PlaceWithSurveyHistoryChooser(new StubUserRepository(), InMemorySurveyRepository.getInstance(), this);
    private TextView placeCountView;
    private RecyclerView placeListView;
    private RecyclerViewHeader recyclerViewHeader;
    private EmptyLayoutView emptyLayoutView;

    public static PlaceSurveyListFragment newInstance(String username) {
        PlaceSurveyListFragment fragment = new PlaceSurveyListFragment();
        Bundle args = new Bundle();
        args.putString(USERNAME_ARG, username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString(USERNAME_ARG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place_survey_list, container, false);
        setupViews(view);
        setupPlaceList();
        setupEmptyLayout();
        placeChooser.showSurveyPlaceList(username);
        return view;
    }

    private void setupViews(View view) {
        placeListView = (RecyclerView) view.findViewById(R.id.place_list);
        placeCountView = (TextView) view.findViewById(R.id.place_count);
        emptyLayoutView = (EmptyLayoutView) view.findViewById(R.id.empty_layout);
        recyclerViewHeader = (RecyclerViewHeader) view.findViewById(R.id.card_header);
    }

    private void setupPlaceList() {
        placeAdapter = new PlaceAdapter(getActivity());
        placeAdapter.setOnItemClickListener(this);
        placeListView.setAdapter(placeAdapter);
        placeListView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        placeListView.setLayoutManager(linearLayoutManager);
        recyclerViewHeader.attachTo(placeListView, true);
    }

    private void setupEmptyLayout() {
        emptyLayoutView.setEmptyButtonVisibility(false);
        emptyLayoutView.setEmptyText(R.string.survey_place_not_found);
    }

    @Override
    public void displaySurveyPlaceList(ArrayList<Place> surveyPlace) {
        emptyLayoutView.setVisibility(View.GONE);
        placeAdapter.updateData(surveyPlace);
        placeCountView.setText(getString(R.string.format_place_count, surveyPlace.size()));
        placeCountView.setVisibility(View.VISIBLE);
    }

    @Override
    public void alertUserNotFound() {
        Alert.highLevel().show(R.string.user_not_found);
    }

    @Override
    public void displaySurveyPlacesNotFound() {
        emptyLayoutView.setVisibility(View.VISIBLE);
        placeAdapter.clearData();
        placeCountView.setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        openBuildingListActivity(position);
    }

    private void openBuildingListActivity(int position) {
        Intent intent = new Intent(getActivity(), BuildingListActivity.class);
        intent.putExtra(BuildingListActivity.PLACE_UUID_ARG, placeAdapter.getItem(position).getId().toString());
        startActivity(intent);
    }
}
