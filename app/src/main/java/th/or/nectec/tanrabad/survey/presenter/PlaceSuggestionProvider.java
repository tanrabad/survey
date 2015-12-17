package th.or.nectec.tanrabad.survey.presenter;

import android.content.SearchRecentSuggestionsProvider;

public class PlaceSuggestionProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "th.or.nectec.tanrabad.survey.PlaceSuggestionProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public PlaceSuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}
