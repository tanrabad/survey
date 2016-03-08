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

package th.or.nectec.tanrabad.survey.repository.persistence;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DbRepository {

    public static final int ERROR_INSERT_ID = -1;
    private static SQLiteDatabase readableDatabase;
    private static SQLiteDatabase writableDatabase;
    private Context context;

    public DbRepository(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    protected SQLiteDatabase readableDatabase() {
        if (readableDatabase == null || !readableDatabase.isOpen())
            readableDatabase = SurveyLiteDatabase.getInstance(context).getReadableDatabase();
        return readableDatabase;
    }

    protected SQLiteDatabase writableDatabase() {
        if (writableDatabase == null || !writableDatabase.isOpen())
            writableDatabase = SurveyLiteDatabase.getInstance(context).getReadableDatabase();
        return writableDatabase;
    }
}
