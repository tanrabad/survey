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

package org.tanrabad.survey.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import static org.tanrabad.survey.presenter.InitialActivityController.SyncStatus;

public class ApiSyncInfoPreference implements ServiceLastUpdate, SyncStatus {

    public static final String SYNC_SUCCESS_KEY = "sync_success";
    private static final String PREF_NAME = "api-sync-info";
    private final Context context;
    private final String path;

    public ApiSyncInfoPreference(Context context) {
        this(context, null);
    }

    public ApiSyncInfoPreference(Context context, String path) {
        this.context = context;
        this.path = path;
    }

    public static void clear(Context context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit().clear().commit();
    }

    public void backLastUpdateTimeToYesterday() {
        DateTimeFormatter rfc1123Formatter = DateTimeFormat.forPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'");
        DateTime dateTime = rfc1123Formatter.parseDateTime(get());
        save(dateTime.minusDays(1).toString());
    }

    @Override
    public void save(String dateTime) {
        if (TextUtils.isEmpty(path))
            throw new RuntimeException("Please define path before save timestamp");

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

    @Override
    public void setSyncStatus(boolean isSuccess) {
        SharedPreferences.Editor spEditor = getSharedPreferences().edit();
        spEditor.putBoolean(SYNC_SUCCESS_KEY, isSuccess);
        spEditor.apply();
    }

    public boolean isPreviousSyncStatusSuccess() {
        return getSharedPreferences().getBoolean(SYNC_SUCCESS_KEY, false);
    }

}
