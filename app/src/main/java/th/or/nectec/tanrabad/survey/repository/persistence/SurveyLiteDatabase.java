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
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.support.annotation.RawRes;
import th.or.nectec.tanrabad.survey.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SurveyLiteDatabase extends SQLiteOpenHelper {

    public static final String DB_NAME = "trb_survey.db";
    public static final int DB_VERSION = 1;
    private Context context;

    public SurveyLiteDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        enableForeignKeyForJellyBeanAndAbove(db);
    }

    private void enableForeignKeyForJellyBeanAndAbove(SQLiteDatabase db) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        readAndExecuteSQLScript(db, R.raw.create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    private void readAndExecuteSQLScript(SQLiteDatabase db, @RawRes Integer sqlScriptResId) {
        db.beginTransaction();
        try {
            RawReader raw = new RawReader(sqlScriptResId);

            executeSQLScript(db, raw.getReader());
            db.setTransactionSuccessful();

            raw.close();
        } catch (IOException e) {
            throw new RuntimeException("Unable to read SQL script", e);
        } finally {
            db.endTransaction();
        }
    }

    private void executeSQLScript(SQLiteDatabase db, BufferedReader reader)
            throws IOException {
        String line;
        StringBuilder statement = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            statement.append(line);
            statement.append("\n");
            if (line.endsWith(";")) {
                db.execSQL(statement.toString());
                statement = new StringBuilder();
            }
        }
    }

    private class RawReader {
        private Integer sqlScriptResId;
        private InputStream is;
        private InputStreamReader isr;
        private BufferedReader reader;

        public RawReader(Integer sqlScriptResId) {
            this.sqlScriptResId = sqlScriptResId;
        }

        public BufferedReader getReader() {
            invoke();
            return reader;
        }

        private void invoke() {
            is = context.getResources().openRawResource(sqlScriptResId);
            isr = new InputStreamReader(is);
            reader = new BufferedReader(isr);
        }

        public void close() throws IOException {
            reader.close();
            is.close();
            isr.close();
        }
    }
}
