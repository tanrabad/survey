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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import th.or.nectec.tanrabad.domain.UserRepository;
import th.or.nectec.tanrabad.domain.place.PlaceRepository;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.survey.repository.StubUserRepository;
import th.or.nectec.tanrabad.survey.utils.collection.CursorList;
import th.or.nectec.tanrabad.survey.utils.collection.CursorMapper;

import java.util.List;
import java.util.UUID;

public class DbPlaceRepository implements PlaceRepository {

    public static final String TABLE_NAME = "place";
    public static final int ERROR_INSERT_ID = -1;
    private final Context context;
    private UserRepository userRepository;


    public DbPlaceRepository(Context context) {
        this.context = context;
        this.userRepository = new StubUserRepository();
    }

    public DbPlaceRepository(Context context, UserRepository userRepository) {
        this.context = context;
        this.userRepository = userRepository;
    }

    @Override
    public List<Place> find() {
        SQLiteDatabase db = new SurveyLiteDatabase(context).getReadableDatabase();
        Cursor placeCursor = db.query(TABLE_NAME, PlaceColumn.wildcard(),
                null, null, null, null, null);
        return getPlaceList(placeCursor);
    }

    @Override
    public Place findByUUID(UUID placeUUID) {
        SQLiteDatabase db = new SurveyLiteDatabase(context).getReadableDatabase();
        Cursor placeCursor = db.query(TABLE_NAME, PlaceColumn.wildcard(),
                PlaceColumn.ID + "=?", new String[]{placeUUID.toString()}, null, null, null);
        return getPlace(placeCursor);
    }

    private Place getPlace(Cursor cursor) {
        if (cursor.moveToFirst()) {
            Place place = getMapper(cursor).map(cursor);
            cursor.close();
            return place;
        } else {
            cursor.close();
            return null;
        }
    }

    @Override
    public List<Place> findByPlaceType(int placeType) {
        SQLiteDatabase db = new SurveyLiteDatabase(context).getReadableDatabase();
        String[] placeColumn = new String[]{PlaceColumn.ID, TABLE_NAME + "." + PlaceColumn.NAME, PlaceColumn.SUBTYPE_ID,
                PlaceColumn.SUBDISTRICT_CODE, PlaceColumn.LATITUDE, PlaceColumn.LONGITUDE,
                PlaceColumn.UPDATE_BY, PlaceColumn.UPDATE_TIME, PlaceColumn.CHANGED_STATUS};
        Cursor placeCursor = db.query(TABLE_NAME + " INNER JOIN place_subtype using(subtype_id)", placeColumn,
                PlaceColumn.TYPE_ID + "=?", new String[]{String.valueOf(placeType)}, null, null, null);
        return getPlaceList(placeCursor);
    }

    @Override
    public List<Place> findByName(String placeName) {
        SQLiteDatabase db = new SurveyLiteDatabase(context).getReadableDatabase();
        Cursor placeCursor = db.query(TABLE_NAME, PlaceColumn.wildcard(),
                PlaceColumn.NAME + " LIKE ?", new String[]{"%" + placeName + "%"}, null, null, null);
        return getPlaceList(placeCursor);
    }

    private List<Place> getPlaceList(Cursor placeCursor) {
        List<Place> placeList = new CursorList<>(placeCursor, getMapper(placeCursor));
        return placeList.isEmpty() ? null : placeList;
    }

    private CursorMapper<Place> getMapper(Cursor cursor) {
        return new PlaceCursorMapper(cursor, userRepository);
    }

    @Override
    public boolean save(Place place) {
        ContentValues values = placeContentValues(place);
        values.put(PlaceColumn.CHANGED_STATUS, ChangedStatus.ADD);
        return saveByContentValues(new SurveyLiteDatabase(context).getWritableDatabase(), values);
    }

    private boolean saveByContentValues(SQLiteDatabase db, ContentValues place) {
        return db.insert(TABLE_NAME, null, place) != ERROR_INSERT_ID;
    }

    @Override
    public boolean update(Place place) {
        ContentValues values = placeContentValues(place);
        values.put(PlaceColumn.CHANGED_STATUS, getAddOrChangedStatus(place));
        return updateByContentValues(new SurveyLiteDatabase(context).getWritableDatabase(), values);
    }

    private int getAddOrChangedStatus(Place place) {
        Cursor placeCursor = new SurveyLiteDatabase(context).getReadableDatabase().query(TABLE_NAME, new String[]{PlaceColumn.CHANGED_STATUS},
                PlaceColumn.ID + "=?", new String[]{place.getId().toString()}, null, null, null);
        if (placeCursor.moveToNext()) {
            if (placeCursor.getInt(0) == ChangedStatus.ADD)
                return ChangedStatus.ADD;
            else
                return ChangedStatus.CHANGED;
        }
        placeCursor.close();
        return ChangedStatus.CHANGED;
    }

    private boolean updateByContentValues(SQLiteDatabase db, ContentValues place) {
        return db.update(TABLE_NAME, place, PlaceColumn.ID + "=?", new String[]{place.getAsString(PlaceColumn.ID)}) > 0;
    }

    @Override
    public void updateOrInsert(List<Place> updateList) {
        SQLiteDatabase db = new SurveyLiteDatabase(context).getWritableDatabase();
        db.beginTransaction();
        for (Place place : updateList) {
            ContentValues values = placeContentValues(place);
            values.put(PlaceColumn.CHANGED_STATUS, ChangedStatus.UNCHANGED);
            boolean updated = updateByContentValues(db, values);
            if (!updated)
                saveByContentValues(db, values);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    private ContentValues placeContentValues(Place place) {
        ContentValues values = new ContentValues();
        values.put(PlaceColumn.ID, place.getId().toString());
        values.put(PlaceColumn.NAME, place.getName());
        values.put(PlaceColumn.SUBTYPE_ID, place.getSubType());
        values.put(PlaceColumn.SUBDISTRICT_CODE, place.getSubdistrictCode());
        if (place.getLocation() != null) {
            values.put(PlaceColumn.LATITUDE, place.getLocation().getLatitude());
            values.put(PlaceColumn.LONGITUDE, place.getLocation().getLongitude());
        }
        if (place.getUpdateBy() != null) {
            values.put(PlaceColumn.UPDATE_BY, place.getUpdateBy());
        }
        values.put(PlaceColumn.UPDATE_TIME, place.getUpdateTimestamp().toString());
        return values;
    }
}
