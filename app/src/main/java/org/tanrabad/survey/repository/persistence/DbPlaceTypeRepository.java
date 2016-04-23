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
import org.tanrabad.survey.utils.collection.CursorList;
import org.tanrabad.survey.utils.collection.CursorMapper;
import org.tanrabad.survey.domain.place.PlaceTypeRepository;
import th.or.nectec.tanrabad.entity.lookup.PlaceType;

import java.util.List;

public class DbPlaceTypeRepository extends DbRepository implements PlaceTypeRepository {

    public static final String TABLE_NAME = "place_type";

    public DbPlaceTypeRepository(Context context) {
        super(context);
    }

    @Override
    public List<PlaceType> find() {
        SQLiteDatabase db = readableDatabase();
        Cursor placeTypeCursor = db.query(TABLE_NAME, PlaceTypeColumn.wildcard(),
                null, null, null, null, null);
        return new CursorList<>(placeTypeCursor, getMapper(placeTypeCursor));
    }

    @Override
    public PlaceType findById(int placeTypeId) {
        SQLiteDatabase db = readableDatabase();
        Cursor placeTypeCursor = db.query(TABLE_NAME, PlaceTypeColumn.wildcard(),
                PlaceTypeColumn.ID + "=?", new String[]{String.valueOf(placeTypeId)}, null, null, null);
        return getPlaceType(placeTypeCursor);
    }

    private PlaceType getPlaceType(Cursor cursor) {
        if (cursor.moveToFirst()) {
            PlaceType placeType = getMapper(cursor).map(cursor);
            cursor.close();
            return placeType;
        } else {
            cursor.close();
            return null;
        }
    }

    private CursorMapper<PlaceType> getMapper(Cursor cursor) {
        return new PlaceTypeCursorMapper(cursor);
    }

    @Override
    public boolean save(PlaceType placeType) {
        return saveByContentValues(writableDatabase(),
                placeTypeContentValues(placeType));
    }

    @Override
    public boolean update(PlaceType placeType) {
        return updateByContentValues(writableDatabase(),
                placeTypeContentValues(placeType));
    }

    @Override
    public boolean delete(PlaceType data) {
        return false;
    }

    @Override
    public void updateOrInsert(List<PlaceType> updateList) {
        SQLiteDatabase db = writableDatabase();
        db.beginTransaction();
        for (PlaceType eachPlaceType : updateList) {
            ContentValues values = placeTypeContentValues(eachPlaceType);
            boolean updated = updateByContentValues(db, values);
            if (!updated)
                saveByContentValues(db, values);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    private boolean updateByContentValues(SQLiteDatabase db, ContentValues placeType) {
        return db.update(TABLE_NAME, placeType, PlaceTypeColumn.ID + "=?",
                new String[]{placeType.getAsString(PlaceTypeColumn.ID)}) > 0;
    }

    private boolean saveByContentValues(SQLiteDatabase db, ContentValues placeType) {
        return db.insert(TABLE_NAME, null, placeType) != ERROR_INSERT_ID;
    }

    private ContentValues placeTypeContentValues(PlaceType placeType) {
        ContentValues values = new ContentValues();
        values.put(PlaceTypeColumn.ID, placeType.getId());
        values.put(PlaceTypeColumn.NAME, placeType.getName());
        return values;
    }
}
