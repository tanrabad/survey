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
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;
import th.or.nectec.tanrabad.domain.place.PlaceWithSurveyStatus;
import th.or.nectec.tanrabad.domain.place.PlaceWithSurveyStatusListPresenter;
import th.or.nectec.tanrabad.domain.survey.SurveyPlaceChooser;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.presenter.view.EmptyLayoutView;
import th.or.nectec.tanrabad.survey.repository.InMemorySurveyRepository;
import th.or.nectec.tanrabad.survey.repository.StubPlaceRepository;
import th.or.nectec.tanrabad.survey.repository.StubUserRepository;
import th.or.nectec.tanrabad.survey.utils.prompt.AlertDialogPromptMessage;
import th.or.nectec.tanrabad.survey.utils.prompt.PromptMessage;

import java.util.List;

public class PlaceListInDatabaseFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener, PlaceWithSurveyStatusListPresenter {

    private PlaceWithSurveyStatusAdapter placeAdapter;
    private PlaceTypeAdapter placeTypeAdapter;
    private SurveyPlaceChooser placeChooser = new SurveyPlaceChooser(new StubUserRepository(), new StubPlaceRepository(), InMemorySurveyRepository.getInstance(), this);
    private TextView placeCountView;
    private RecyclerView placeListView;
    private AppCompatSpinner placeFilterView;
    private RecyclerViewHeader recyclerViewHeader;
    private EmptyLayoutView emptyLayoutView;

    public static PlaceListInDatabaseFragment newInstance() {
        PlaceListInDatabaseFragment fragment = new PlaceListInDatabaseFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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
        setupEmptyList(view);
        setupPlaceFilterSpinner();
        setupPlaceList();
        return view;
    }

    private void setupEmptyList(View view) {
        emptyLayoutView = (EmptyLayoutView) view.findViewById(R.id.empty_layout);
        emptyLayoutView.setEmptyButtonText(R.string.add_place);
        emptyLayoutView.setEmptyText(R.string.places_not_found);
    }

    private void setupViews(View view) {
        this.placeListView = (RecyclerView) view.findViewById(R.id.place_list);
        this.placeCountView = (TextView) view.findViewById(R.id.place_count);
        this.placeFilterView = (AppCompatSpinner) view.findViewById(R.id.place_filter);
        recyclerViewHeader = (RecyclerViewHeader) view.findViewById(R.id.card_header);

    }

    private void setupPlaceFilterSpinner() {
        placeFilterView.setOnItemSelectedListener(this);
        placeTypeAdapter = new PlaceTypeAdapter(getActivity());
        placeFilterView.setAdapter(placeTypeAdapter);
    }

    private void setupPlaceList() {
        placeAdapter = new PlaceWithSurveyStatusAdapter(getActivity());
        placeAdapter.setOnItemClickListener(this);
        placeListView.setAdapter(placeAdapter);
        placeListView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        placeListView.setLayoutManager(linearLayoutManager);
        recyclerViewHeader.attachTo(placeListView, true);
    }

    @Override
    public void alertUserNotFound() {

    }

    @Override
    public void displayPlacesNotfound() {
        placeAdapter.clearData();
        placeCountView.setText(String.valueOf(0));
        emptyLayoutView.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayAllSurveyPlaceList(List<PlaceWithSurveyStatus> buildingsWithSurveyStatuses) {
        placeAdapter.updateData(buildingsWithSurveyStatuses);
        placeCountView.setText(getString(R.string.format_place_count, buildingsWithSurveyStatuses.size()));
        emptyLayoutView.setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
        final PlaceWithSurveyStatus placeWithSurveyStatus = placeAdapter.getItem(position);
        PromptMessage promptMessage = new AlertDialogPromptMessage(getActivity());
        promptMessage.setOnConfirm(getString(R.string.survey), new PromptMessage.OnConfirmListener() {
            @Override
            public void onConfirm() {
                if (placeWithSurveyStatus.isSurvey()) {
                    openBuildingSurveyHistoryActivity(placeWithSurveyStatus);
                } else {
                    openBuildingListActivity(placeWithSurveyStatus);
                }
            }
        });
        promptMessage.setOnCancel(getString(R.string.cancel), null);
        promptMessage.show(getString(R.string.start_survey), placeAdapter.getItem(position).getPlace().getName());

    }

    private void openBuildingSurveyHistoryActivity(PlaceWithSurveyStatus placeWithSurveyStatus) {
        Intent intent = new Intent(getActivity(), SurveyBuildingHistoryActivity.class);
        intent.putExtra(SurveyBuildingHistoryActivity.PLACE_UUID_ARG, placeWithSurveyStatus.getPlace().getId().toString());
        intent.putExtra(SurveyBuildingHistoryActivity.USER_NAME_ARG, getUsername());
        startActivity(intent);
    }

    private void openBuildingListActivity(PlaceWithSurveyStatus placeWithSurveyStatus) {
        Intent intent = new Intent(getActivity(), BuildingListActivity.class);
        intent.putExtra(BuildingListActivity.PLACE_UUID_ARG, placeWithSurveyStatus.getPlace().getId().toString());
        intent.putExtra(BuildingListActivity.IS_NEW_SURVEY_ARG, true);
        startActivity(intent);
    }

    private String getUsername() {
        return "sara";
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        int selectedID = (int) placeTypeAdapter.getItemId(position);
        if (selectedID > 0) {
            placeChooser.getPlaceListWithPlaceFilter(selectedID, getUsername());
        } else {
            placeChooser.displaySurveyBuildingOf(getUsername());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
