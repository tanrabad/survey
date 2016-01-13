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

package th.or.nectec.tanrabad.survey.service;

import android.content.Context;
import android.content.SharedPreferences;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


public class LastUpdatePreference implements LastUpdate {

    public static final String PREF_NAME = "Last-Update";
    public static final String DEFAULT_DATETIME = "Tue, 01 Dec 2014 17:00:00 GMT";
    private static final DateTimeFormatter RFC1123_FORMATTER =
            DateTimeFormat.forPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'");
    private final Context context;
    private final String path;

    public LastUpdatePreference(Context context, String path) {
        this.context = context;
        this.path = path;
    }

    @Override
    public void save(DateTime dateTime) {
        SharedPreferences.Editor spEditor = getSharedPreferences().edit();
        spEditor.putString(path, RFC1123_FORMATTER.print(dateTime));
        spEditor.apply();
    }

    private SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public DateTime get() {
        String text = getSharedPreferences().getString(path, DEFAULT_DATETIME);
        return RFC1123_FORMATTER.parseDateTime(text);
    }

}
