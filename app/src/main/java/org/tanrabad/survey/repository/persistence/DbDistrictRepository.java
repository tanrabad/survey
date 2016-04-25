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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import org.tanrabad.survey.TanrabadApp;
import org.tanrabad.survey.utils.collection.CursorList;
import org.tanrabad.survey.utils.collection.CursorMapper;
import org.tanrabad.survey.domain.address.DistrictRepository;
import org.tanrabad.survey.entity.lookup.District;

import java.util.List;

public class DbDistrictRepository extends DbRepository implements DistrictRepository {

    public static final String TABLE_NAME = "district";
    private static DbDistrictRepository instance;

    private DbDistrictRepository(Context context) {
        super(context);
    }

    public static DbDistrictRepository getInstance() {
        if (instance == null) {
            instance = new DbDistrictRepository(TanrabadApp.getInstance());
        }
        return instance;
    }

    @Override
    public List<District> findByProvinceCode(String provinceCode) {
        SQLiteDatabase db = readableDatabase();
        Cursor districtCursor = db.query(TABLE_NAME, DistrictColumn.WILDCARD, DistrictColumn.PROVINCE_CODE + "=?",
                new String[]{provinceCode}, null, null, null);
        return new CursorList<>(districtCursor, getMapper(districtCursor));
    }

    private DistrictCursorMapper getMapper(Cursor districtCursor) {
        return new DistrictCursorMapper(districtCursor);
    }

    @Override
    public District findByCode(String districtCode) {
        SQLiteDatabase db = readableDatabase();
        Cursor districtCursor = db.query(TABLE_NAME, DistrictColumn.WILDCARD, DistrictColumn.CODE + "=?",
                new String[]{districtCode}, null, null, null);
        if (districtCursor.moveToFirst()) {
            return getMapper(districtCursor).map(districtCursor);
        }
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
    public boolean delete(District data) {
        return false;
    }

    @Override
    public void updateOrInsert(List<District> districts) {
        SQLiteDatabase db = writableDatabase();
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

    public static class DistrictColumn {
        public static final String CODE = "district_code";
        public static final String PROVINCE_CODE = "province_code";
        public static final String NAME = "name";
        public static final String BOUNDARY = "boundary";
        public static final String[] WILDCARD = new String[]{CODE, NAME, PROVINCE_CODE, BOUNDARY};
    }

    public static class DistrictCursorMapper implements CursorMapper<District> {

        int codeIdx;
        int nameIdx;
        int provinceCodeIdx;
        int boundaryIdx;

        public DistrictCursorMapper(Cursor cursor) {
            codeIdx = cursor.getColumnIndex(DistrictColumn.CODE);
            nameIdx = cursor.getColumnIndex(DistrictColumn.NAME);
            provinceCodeIdx = cursor.getColumnIndex(DistrictColumn.PROVINCE_CODE);
            boundaryIdx = cursor.getColumnIndex(DistrictColumn.BOUNDARY);
        }

        @Override
        public District map(Cursor cursor) {
            District district = new District();
            district.setCode(cursor.getString(codeIdx));
            district.setName(cursor.getString(nameIdx));
            district.setProvinceCode(cursor.getString(provinceCodeIdx));
            //TODO parse boundary text in db to BoundaryObject
            return district;
        }
    }
}
