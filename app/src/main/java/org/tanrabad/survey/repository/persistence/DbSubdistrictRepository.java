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
import th.or.nectec.tanrabad.domain.address.SubdistrictRepository;
import th.or.nectec.tanrabad.entity.lookup.Subdistrict;

import java.util.List;

public class DbSubdistrictRepository extends DbRepository implements SubdistrictRepository {

    public static final String TABLE_NAME = "subdistrict";
    private static DbSubdistrictRepository instance;


    private DbSubdistrictRepository(Context context) {
        super(context);
    }

    public static DbSubdistrictRepository getInstance() {
        if (instance == null)
            instance = new DbSubdistrictRepository(TanrabadApp.getInstance());
        return instance;
    }

    @Override
    public List<Subdistrict> findByDistrictCode(String districtCode) {
        SQLiteDatabase db = readableDatabase();
        Cursor subdistrictCursor = db.query(TABLE_NAME, SubdistrictColumn.WILDCARD,
                SubdistrictColumn.DISTRICT_CODE + "=?", new String[]{districtCode}, null, null, null);
        return new CursorList<>(subdistrictCursor, getMapper(subdistrictCursor));
    }

    @Override
    public Subdistrict findByCode(String subdistrictCode) {
        SQLiteDatabase db = readableDatabase();
        Cursor subdistrictCursor = db.query(TABLE_NAME, SubdistrictColumn.WILDCARD, SubdistrictColumn.CODE + "=?",
                new String[]{subdistrictCode}, null, null, null);
        if (subdistrictCursor.moveToFirst()) {
            return getMapper(subdistrictCursor).map(subdistrictCursor);
        }
        return null;
    }

    private SubdistrictCursorMapper getMapper(Cursor subdistrictCursor) {
        return new SubdistrictCursorMapper(subdistrictCursor);
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
    public boolean delete(Subdistrict data) {
        return false;
    }

    @Override
    public void updateOrInsert(List<Subdistrict> subdistricts) {
        SQLiteDatabase db = writableDatabase();
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

    public static class SubdistrictColumn {
        public static final String CODE = "subdistrict_code";
        public static final String DISTRICT_CODE = "district_code";
        public static final String NAME = "name";
        public static final String BOUNDARY = "boundary";
        public static final String[] WILDCARD = new String[]{CODE, NAME, DISTRICT_CODE, BOUNDARY};
    }

    public static class SubdistrictCursorMapper implements CursorMapper<Subdistrict> {

        int codeIdx;
        int nameIdx;
        int districtCodeIdx;
        int boundaryIdx;

        public SubdistrictCursorMapper(Cursor cursor) {
            codeIdx = cursor.getColumnIndex(SubdistrictColumn.CODE);
            nameIdx = cursor.getColumnIndex(SubdistrictColumn.NAME);
            districtCodeIdx = cursor.getColumnIndex(SubdistrictColumn.DISTRICT_CODE);
            boundaryIdx = cursor.getColumnIndex(SubdistrictColumn.BOUNDARY);
        }

        @Override
        public Subdistrict map(Cursor cursor) {
            Subdistrict subdistrict = new Subdistrict();
            subdistrict.setCode(cursor.getString(codeIdx));
            subdistrict.setName(cursor.getString(nameIdx));
            subdistrict.setDistrictCode(cursor.getString(districtCodeIdx));
            //TODO parse boundary text in db to BoundaryObject
            return subdistrict;
        }
    }
}
