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
import android.support.test.InstrumentationRegistry;
import org.junit.rules.ExternalResource;
import th.or.nectec.tanrabad.survey.R;

public class SurveyDbTestRule extends ExternalResource {

    private SQLiteOpenHelper sqLiteOpenHelper;

    public SQLiteDatabase getReadable() {
        return sqLiteOpenHelper.getReadableDatabase();
    }

    public SQLiteDatabase getWritable() {
        return sqLiteOpenHelper.getWritableDatabase();
    }

    @Override
    protected void before() throws Throwable {
        sqLiteOpenHelper = new SurveyLiteDatabase(getContext());
        SqlScript.readAndExecute(getContext(), sqLiteOpenHelper.getWritableDatabase(), R.raw.teardown);
        SqlScript.readAndExecute(getContext(), sqLiteOpenHelper.getWritableDatabase(), R.raw.test_setup);
    }

    private Context getContext() {
        return InstrumentationRegistry.getTargetContext();
    }

    @Override
    protected void after() {
        SqlScript.readAndExecute(getContext(), sqLiteOpenHelper.getWritableDatabase(), R.raw.teardown);
        sqLiteOpenHelper.close();
    }
}
