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
import th.or.nectec.tanrabad.domain.address.ProvinceRepository;
import th.or.nectec.tanrabad.entity.lookup.Province;

import java.util.List;

public class DbProvinceRepository extends DbRepository implements ProvinceRepository {

    public static final String TABLE_NAME = "province";
    private static DbProvinceRepository instance;

    private DbProvinceRepository(Context context) {
        super(context);
    }

    public static DbProvinceRepository getInstance() {
        if (instance == null)
            instance = new DbProvinceRepository(TanrabadApp.getInstance());
        return instance;
    }

    @Override
    public List<Province> find() {
        SQLiteDatabase db = readableDatabase();
        Cursor provinceCursor = db.query(TABLE_NAME, ProvinceColumn.WILDCARD,
                null, null, null, null, ProvinceColumn.CODE);
        return new CursorList<>(provinceCursor, getMapper(provinceCursor));
    }

    private ProvinceCursorMapper getMapper(Cursor provinceCursor) {
        return new ProvinceCursorMapper(provinceCursor);
    }

    @Override
    public Province findByCode(String provinceCode) {
        SQLiteDatabase db = readableDatabase();
        Cursor provinceCursor = db.query(TABLE_NAME, ProvinceColumn.WILDCARD, ProvinceColumn.CODE + "=?",
                new String[]{provinceCode}, null, null, null);
        if (provinceCursor.moveToFirst()) {
            return getMapper(provinceCursor).map(provinceCursor);
        }
        return null;
    }

    @Override
    public boolean save(Province province) {
        return false;
    }

    @Override
    public boolean update(Province province) {
        return false;
    }

    @Override
    public boolean delete(Province data) {
        return false;
    }

    @Override
    public void updateOrInsert(List<Province> provinces) {
        SQLiteDatabase db = writableDatabase();
        db.beginTransaction();
        for (Province province : provinces) {
            ContentValues cv = provinceContentValues(province);
            boolean update = updateWithContentValues(db, cv);
            if (!update)
                insertWithContentValue(db, cv);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    private boolean insertWithContentValue(SQLiteDatabase db, ContentValues cv) {
        return db.insert(TABLE_NAME, null, cv) != -1;
    }

    private boolean updateWithContentValues(SQLiteDatabase db, ContentValues cv) {
        return db.update(TABLE_NAME, cv, "province_code=?", new String[]{cv.getAsString("province_code")}) > 0;
    }

    private ContentValues provinceContentValues(Province province) {
        ContentValues cv = new ContentValues();
        cv.put("province_code", province.getCode());
        cv.put("name", province.getName());
        return cv;
    }

    public static class ProvinceColumn {
        public static final String CODE = "province_code";
        public static final String NAME = "name";
        public static final String BOUNDARY = "boundary";
        public static final String[] WILDCARD = new String[]{CODE, NAME, BOUNDARY};
    }

    public static class ProvinceCursorMapper implements CursorMapper<Province> {

        int codeIdx;
        int nameIdx;
        int boundaryIdx;

        public ProvinceCursorMapper(Cursor cursor) {
            codeIdx = cursor.getColumnIndex(ProvinceColumn.CODE);
            nameIdx = cursor.getColumnIndex(ProvinceColumn.NAME);
            boundaryIdx = cursor.getColumnIndex(ProvinceColumn.BOUNDARY);
        }

        @Override
        public Province map(Cursor cursor) {
            Province province = new Province();
            province.setCode(cursor.getString(codeIdx));
            province.setName(cursor.getString(nameIdx));
            //TODO parse boundary text in db to BoundaryObject
            return province;
        }
    }
}
