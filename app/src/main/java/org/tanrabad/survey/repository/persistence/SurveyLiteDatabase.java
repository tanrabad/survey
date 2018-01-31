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

package org.tanrabad.survey.repository.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.tanrabad.survey.R;
import org.tanrabad.survey.service.ApiSyncInfoPreference;

public final class SurveyLiteDatabase extends SQLiteOpenHelper {

    public static final String DB_NAME = "trb_survey.db";
    public static final int DB_VERSION = 5;
    private static SurveyLiteDatabase instance;
    private Context context;

    private SurveyLiteDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    public static SurveyLiteDatabase getInstance(Context context) {
        if (instance == null)
            instance = new SurveyLiteDatabase(context);
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        SqlScript.readAndExecute(context, db, R.raw.create);
        SqlScript.readAndExecute(context, db, R.raw.setup);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int fromVersion, int newVersion) {
        ApiSyncInfoPreference.clear(context);
        switch (fromVersion) {
            case 1:
                onCreate(db);
                break;
            case 2:
                SqlScript.readAndExecute(context, db, R.raw.alter2to3);
                // fall through
            case 3:
                SqlScript.readAndExecute(context, db, R.raw.alter3to4);
                // fall through
            case 4:
                SqlScript.readAndExecute(context, db, R.raw.alter4to5);
                break;
        }

    }

}
