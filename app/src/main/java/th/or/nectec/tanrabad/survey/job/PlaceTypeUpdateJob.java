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

package th.or.nectec.tanrabad.survey.job;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.repository.persistence.SqlScript;
import th.or.nectec.tanrabad.survey.repository.persistence.SurveyLiteDatabase;

public class PlaceTypeUpdateJob implements Job {

    public static final int ID = 748970;

    private Context context;
    private SQLiteOpenHelper sqLiteOpenHelper;

    public PlaceTypeUpdateJob(Context context) {
        this.context = context;
        this.sqLiteOpenHelper = new SurveyLiteDatabase(context);
    }

    @Override
    public int id() {
        return ID;
    }

    @Override
    public void execute() throws JobException {
        SqlScript.readAndExecute(context, sqLiteOpenHelper.getWritableDatabase(), R.raw.setup);
        sqLiteOpenHelper.close();
    }
}
