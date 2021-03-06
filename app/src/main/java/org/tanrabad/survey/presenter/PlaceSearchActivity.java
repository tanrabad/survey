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

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import org.tanrabad.survey.R;
import org.tanrabad.survey.TanrabadApp;
import org.tanrabad.survey.domain.place.PlaceChooser;
import org.tanrabad.survey.domain.place.PlaceListPresenter;
import org.tanrabad.survey.entity.Building;
import org.tanrabad.survey.entity.Place;
import org.tanrabad.survey.job.AbsJobRunner;
import org.tanrabad.survey.job.Job;
import org.tanrabad.survey.job.WritableRepoUpdateJob;
import org.tanrabad.survey.repository.BrokerBuildingRepository;
import org.tanrabad.survey.repository.BrokerPlaceRepository;
import org.tanrabad.survey.service.BuildingRestService;
import org.tanrabad.survey.service.PlaceRestService;
import org.tanrabad.survey.utils.android.InternetConnection;

import java.util.List;

public class PlaceSearchActivity extends TanrabadActivity implements
        SearchView.OnQueryTextListener, PlaceListPresenter {

    private PlaceChooser placeChooser = new PlaceChooser(BrokerPlaceRepository.getInstance(), this);
    private SearchRecentSuggestions suggestions;
    private PlaceAdapter placeAdapter;
    private SearchView searchView;
    private ListView searchHistoryListView;
    private SimpleCursorAdapter searchHistoryAdapter;
    private RecyclerView placeListView;
    private TextView emptyText;
    private TextView clearSearchHistory;

    public static void open(Activity activity) {
        Intent intent = new Intent(activity, PlaceSearchActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_search);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        emptyText = (TextView) findViewById(R.id.place_name_notfound);
        setupClearSearchHistoryButton();
        setupHomeButton();
        setupSearchHistoryList();
        querySuggestion(null);
        setupPlaceList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (InternetConnection.isAvailable(this)) new SyncJobRunner().start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupClearSearchHistoryButton() {
        clearSearchHistory = (TextView) findViewById(R.id.clear_search_history);
        clearSearchHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                suggestions.clearHistory();
                querySuggestion(null);
                clearSearchHistory.setVisibility(View.GONE);
            }
        });
    }

    private void querySuggestion(String queryString) {
        Cursor recentQuery = PlaceSuggestionProvider.querySuggestion(this, queryString);
        searchHistoryAdapter.changeCursor(recentQuery);
        searchHistoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor selectedItem = (Cursor) adapterView.getItemAtPosition(position);
                searchView.setQuery(selectedItem.getString(
                        selectedItem.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1)), true);
            }
        });
        if (recentQuery.getCount() > 0) {
            clearSearchHistory.setVisibility(View.VISIBLE);
        } else {
            clearSearchHistory.setVisibility(View.GONE);
        }
    }

    private void setupSearchHistoryList() {
        searchHistoryAdapter = new SimpleCursorAdapter(this,
                R.layout.list_item_search_history, null,
                new String[]{SearchManager.SUGGEST_COLUMN_TEXT_1}, new int[]{R.id.text_item},
                SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        searchHistoryListView = (ListView) findViewById(R.id.place_search_history_list);
        searchHistoryListView.setAdapter(searchHistoryAdapter);
    }

    private void setupPlaceList() {
        placeAdapter = new PlaceAdapter(this);
        placeAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Place selectedPlace = placeAdapter.getItem(position);
                TanrabadApp.action().startSurvey(selectedPlace, "search");
                SurveyBuildingHistoryActivity.open(PlaceSearchActivity.this, selectedPlace);
                finish();
            }
        });
        placeListView = (RecyclerView) findViewById(R.id.place_list);
        placeListView.setAdapter(placeAdapter);
        placeListView.addItemDecoration(new SimpleDividerItemDecoration(this));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        placeListView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_search, menu);
        setupSearchView(menu);
        suggestions = new SearchRecentSuggestions(
                this, PlaceSuggestionProvider.AUTHORITY, PlaceSuggestionProvider.MODE);
        return true;
    }

    private void setupSearchView(Menu menu) {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        searchView.setIconified(false);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setMaxWidth(Integer.MAX_VALUE);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        TanrabadApp.action().searchPlace(query);
        placeChooser.searchByName(query);
        suggestions.saveRecentQuery(query, null);
        searchHistoryListView.setVisibility(View.GONE);
        clearSearchHistory.setVisibility(View.GONE);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        searchHistoryListView.setVisibility(View.VISIBLE);
        clearSearchHistory.setVisibility(View.VISIBLE);
        placeListView.setVisibility(View.GONE);
        emptyText.setVisibility(View.GONE);
        querySuggestion(query);
        if (TextUtils.isEmpty(query)) {
            placeAdapter.clearData();
        } else {
            placeChooser.searchByName(query);
        }
        return true;
    }

    @Override
    public void displayPlaceList(final List<Place> places) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                placeAdapter.updateData(places);
                placeListView.setVisibility(View.VISIBLE);
                emptyText.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void displayPlaceNotFound() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                placeAdapter.clearData();
                placeListView.setVisibility(View.GONE);
                emptyText.setVisibility(View.VISIBLE);
                String message = String.format(getString(R.string.place_name_not_found),
                    searchView.getQuery().toString());
                emptyText.setText(Html.fromHtml(message));
            }
        });
    }

    private class SyncJobRunner extends AbsJobRunner {

        private WritableRepoUpdateJob<Place> placeUpdateJob = new WritableRepoUpdateJob<>(
                new PlaceRestService(), BrokerPlaceRepository.getInstance());
        private WritableRepoUpdateJob<Building> buildingUpdateJob = new WritableRepoUpdateJob<>(
                new BuildingRestService(), BrokerBuildingRepository.getInstance());

        public SyncJobRunner() {
            addJob(placeUpdateJob);
            addJob(buildingUpdateJob);
        }

        @Override
        protected void onJobStart(Job startingJob) {
            //do nothing
        }

        @Override
        protected void onRunFinish() {
            if (searchView == null) return; //Activity is closed
            CharSequence query = searchView.getQuery();
            if (!TextUtils.isEmpty(query)) {
                onQueryTextSubmit(query.toString());
            }
        }
    }
}
