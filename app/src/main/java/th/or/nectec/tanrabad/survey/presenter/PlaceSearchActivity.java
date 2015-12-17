package th.or.nectec.tanrabad.survey.presenter;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import java.util.List;

import th.or.nectec.tanrabad.domain.place.PlaceChooser;
import th.or.nectec.tanrabad.domain.place.PlaceListPresenter;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.repository.InMemoryPlaceRepository;

public class PlaceSearchActivity extends TanrabadActivity implements SearchView.OnQueryTextListener, PlaceListPresenter {

    PlaceChooser placeChooser = new PlaceChooser(InMemoryPlaceRepository.getInstance(), this);
    private SearchRecentSuggestions suggestions;
    private PlaceAdapter placeAdapter;

    public static void open(Activity activity) {
        Intent intent = new Intent(activity, PlaceSearchActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_search);
        setupActionBar();
        setupPlaceList();
    }

    private void setupActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    private void setupPlaceList() {
        placeAdapter = new PlaceAdapter(this);
        placeAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Place selectedPlace = placeAdapter.getItem(position);
                SurveyBuildingHistoryActivity.openBuildingSurveyHistoryActivity(PlaceSearchActivity.this, selectedPlace, "sara");
                finish();
            }
        });
        RecyclerView placeListView = (RecyclerView) findViewById(R.id.place_list);
        placeListView.setAdapter(placeAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        placeListView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_search, menu);
        setupSearchView(menu);
        suggestions = new SearchRecentSuggestions(this, PlaceSuggestionProvider.AUTHORITY, PlaceSuggestionProvider.MODE);
        return true;
    }

    private void setupSearchView(Menu menu) {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconified(false);
        searchView.setIconifiedByDefault(false);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(this);
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

    @Override
    public boolean onQueryTextSubmit(String query) {
        placeChooser.searchByName(query);
        suggestions.saveRecentQuery(query, null);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        placeChooser.searchByName(query);
        return false;
    }

    @Override
    public void displayPlaceList(List<Place> places) {
        placeAdapter.updateData(places);
    }

    @Override
    public void displayPlaceNotFound() {
        placeAdapter.clearData();
    }
}
