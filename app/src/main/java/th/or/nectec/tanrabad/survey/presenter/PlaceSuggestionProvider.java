package th.or.nectec.tanrabad.survey.presenter;

import android.app.SearchManager;
import android.content.Context;
import android.content.SearchRecentSuggestionsProvider;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

public class PlaceSuggestionProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "th.or.nectec.tanrabad.survey.PlaceSuggestionProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public PlaceSuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }

    public static Cursor querySuggestion(Context context, String queryString) {
        if (TextUtils.isEmpty(queryString))
            queryString = "";
        Uri queryUri = Uri.parse("content://" + PlaceSuggestionProvider.AUTHORITY + "/" + SearchManager.SUGGEST_URI_PATH_QUERY);
        return context.getContentResolver().query(queryUri, null, "word MATCH ?", new String[]{queryString}, null);
    }
}
