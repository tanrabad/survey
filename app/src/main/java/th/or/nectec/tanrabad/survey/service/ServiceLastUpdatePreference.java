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

public class ServiceLastUpdatePreference implements ServiceLastUpdate {

    private static final String PREF_NAME = "api-last-update";
    private final Context context;
    private final String path;

    public ServiceLastUpdatePreference(Context context, String path) {
        this.context = context;
        this.path = path;
    }

    public static void clear(Context context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit().clear().apply();
    }

    public void backLastUpdateTimeToYesterday() {
        DateTimeFormatter rfc1123Formatter = DateTimeFormat.forPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'");
        DateTime dateTime = rfc1123Formatter.parseDateTime(get());
        save(dateTime.minusDays(1).toString());
    }

    @Override
    public void save(String dateTime) {
        SharedPreferences.Editor spEditor = getSharedPreferences().edit();
        spEditor.putString(path, dateTime);
        spEditor.apply();
    }

    @Override
    public String get() {
        return getSharedPreferences().getString(path, null);
    }

    private SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

}
