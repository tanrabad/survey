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

package org.tanrabad.survey.presenter;

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
import org.tanrabad.survey.presenter.view.EmptyLayoutView;
import org.tanrabad.survey.repository.BrokerSurveyRepository;
import org.tanrabad.survey.repository.BrokerUserRepository;
import org.tanrabad.survey.utils.alert.Alert;
import org.tanrabad.survey.domain.place.PlaceWithSurveyHistoryChooser;
import org.tanrabad.survey.domain.place.PlaceWithSurveyHistoryListPresenter;
import org.tanrabad.survey.entity.Place;
import org.tanrabad.survey.R;

import java.util.List;

public class PlaceSurveyListFragment extends TanrabadFragment implements PlaceWithSurveyHistoryListPresenter,
        AdapterView.OnItemClickListener {

    private static final String USERNAME_ARG = "username_arg";
    private String username;

    private PlaceSurveyAdapter placeAdapter;
    private PlaceWithSurveyHistoryChooser placeChooser = new PlaceWithSurveyHistoryChooser(
            BrokerUserRepository.getInstance(),
            BrokerSurveyRepository.getInstance(), this);
    private TextView placeCountView;
    private RecyclerView placeListView;
    private RecyclerViewHeader surveyPlaceListHeader;
    private EmptyLayoutView emptySurveyPlaceListView;

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
        emptySurveyPlaceListView.showProgressBar();
        placeChooser.showSurveyPlaceList(username);
        return view;
    }

    private void setupViews(View view) {
        placeListView = (RecyclerView) view.findViewById(R.id.place_list);
        placeCountView = (TextView) view.findViewById(R.id.place_count);
        surveyPlaceListHeader = (RecyclerViewHeader) view.findViewById(R.id.card_header);
        emptySurveyPlaceListView = (EmptyLayoutView) view.findViewById(R.id.empty_layout);
        emptySurveyPlaceListView.setEmptyIcon(R.mipmap.ic_place);
        emptySurveyPlaceListView.setEmptyButtonVisibility(false);
        emptySurveyPlaceListView.setEmptyText(R.string.survey_place_not_found);
    }

    private void setupPlaceList() {
        placeAdapter = new PlaceSurveyAdapter(getActivity(), getActivity().getSupportFragmentManager());
        placeAdapter.setOnItemClickListener(this);
        placeListView.setAdapter(placeAdapter);
        placeListView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        placeListView.setLayoutManager(linearLayoutManager);
        surveyPlaceListHeader.attachTo(placeListView, true);
    }

    @Override
    public void displaySurveyPlaceList(final List<Place> surveyPlace) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                emptySurveyPlaceListView.hide();
                placeAdapter.updateData(surveyPlace);
                placeCountView.setText(getString(R.string.format_place_count, surveyPlace.size()));
                placeCountView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void alertUserNotFound() {
        Alert.highLevel().show(R.string.user_not_found);
    }

    @Override
    public void displaySurveyPlacesNotFound() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                emptySurveyPlaceListView.showEmptyLayout();
                placeAdapter.clearData();
                placeCountView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        SurveyBuildingHistoryActivity.open(getActivity(), placeAdapter.getItem(position));
    }
}
