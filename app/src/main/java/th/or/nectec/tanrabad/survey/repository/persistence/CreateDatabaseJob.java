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
import th.or.nectec.tanrabad.survey.job.Job;

public class CreateDatabaseJob implements Job {

    public static final int ID = 1029422;
    private final Context context;

    public CreateDatabaseJob(Context context) {
        this.context = context;
    }

    @Override
    public int id() {
        return ID;
    }

    @Override
    public void execute() {
        createDatabaseIfNotExist();
    }

    private void createDatabaseIfNotExist() {
        SurveyLiteDatabase surveyLiteDatabase = SurveyLiteDatabase.getInstance(context);
        surveyLiteDatabase.getReadableDatabase();
        surveyLiteDatabase.close();
    }
}
