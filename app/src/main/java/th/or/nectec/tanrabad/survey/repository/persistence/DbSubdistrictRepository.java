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

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import th.or.nectec.tanrabad.domain.address.SubdistrictRepository;
import th.or.nectec.tanrabad.entity.lookup.Subdistrict;
import th.or.nectec.tanrabad.survey.TanrabadApp;

import java.util.List;

public class DbSubdistrictRepository implements SubdistrictRepository {

    private static DbSubdistrictRepository instance;
    private Context context;

    private DbSubdistrictRepository(Context context) {
        this.context = context;
    }

    public static DbSubdistrictRepository getInstance() {
        if (instance == null)
            instance = new DbSubdistrictRepository(TanrabadApp.getInstance());
        return instance;
    }

    @Override
    public List<Subdistrict> findByDistrictCode(String districtCode) {
        return null;
    }

    @Override
    public Subdistrict findByCode(String subdistrictCode) {
        return null;
    }

    @Override
    public boolean save(Subdistrict subdistrict) {
        return false;
    }

    @Override
    public boolean update(Subdistrict subdistrict) {
        return false;
    }

    @Override
    public void updateOrInsert(List<Subdistrict> subdistricts) {
        SQLiteDatabase db = new SurveyLiteDatabase(context).getWritableDatabase();
        db.beginTransaction();
        for (Subdistrict district : subdistricts) {
            ContentValues cv = provinceContentValues(district);
            boolean update = updateWithContentValues(db, cv);
            if (!update)
                insertWithContentValue(db, cv);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    private boolean insertWithContentValue(SQLiteDatabase db, ContentValues cv) {
        return db.insert("subdistrict", null, cv) != -1;
    }

    private boolean updateWithContentValues(SQLiteDatabase db, ContentValues cv) {
        return db.update("subdistrict", cv, "subdistrict_code=?", new String[]{cv.getAsString("subdistrict_code")}) > 0;
    }

    private ContentValues provinceContentValues(Subdistrict subDistrict) {
        ContentValues cv = new ContentValues();
        cv.put("subdistrict_code", subDistrict.getCode());
        cv.put("name", subDistrict.getName());
        cv.put("district_code", subDistrict.getDistrictCode());
        return cv;
    }
}
