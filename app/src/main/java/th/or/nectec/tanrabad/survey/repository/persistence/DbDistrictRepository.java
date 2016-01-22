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
import th.or.nectec.tanrabad.domain.address.DistrictRepository;
import th.or.nectec.tanrabad.entity.District;
import th.or.nectec.tanrabad.survey.TanrabadApp;

import java.util.List;

public class DbDistrictRepository implements DistrictRepository {

    private static DbDistrictRepository instance;
    private Context context;

    private DbDistrictRepository(Context context) {
        this.context = context;
    }

    public static DbDistrictRepository getInstance() {
        if (instance == null) {
            instance = new DbDistrictRepository(TanrabadApp.getInstance());
        }
        return instance;
    }

    @Override
    public List<District> findByProvinceCode(String provinceCode) {
        return null;
    }

    @Override
    public District findByCode(String districtCode) {
        return null;
    }

    @Override
    public boolean save(District district) {
        return false;
    }

    @Override
    public boolean update(District district) {
        return false;
    }

    @Override
    public void updateOrInsert(List<District> districts) {
        SQLiteDatabase db = new SurveyLiteDatabase(context).getWritableDatabase();
        db.beginTransaction();
        for (District district : districts) {
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
        return db.insert("district", null, cv) != -1;
    }

    private boolean updateWithContentValues(SQLiteDatabase db, ContentValues cv) {
        return db.update("district", cv, "district_code=?", new String[]{cv.getAsString("district_code")}) > 0;
    }

    private ContentValues provinceContentValues(District district) {
        ContentValues cv = new ContentValues();
        cv.put("district_code", district.getCode());
        cv.put("name", district.getName());
        cv.put("province_code", district.getProvinceCode());
        return cv;
    }
}
