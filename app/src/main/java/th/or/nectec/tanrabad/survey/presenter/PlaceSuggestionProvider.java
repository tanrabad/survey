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

package th.or.nectec.tanrabad.survey.presenter;

import android.app.SearchManager;
import android.content.Context;
import android.content.SearchRecentSuggestionsProvider;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

public class PlaceSuggestionProvider extends SearchRecentSuggestionsProvider {
    public static final String AUTHORITY = "th.or.nectec.tanrabad.survey.PlaceSuggestionProvider";
    public static final int MODE = DATABASE_MODE_QUERIES;

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
