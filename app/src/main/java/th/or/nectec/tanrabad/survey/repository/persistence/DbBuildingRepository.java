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

import java.util.List;
import java.util.UUID;

import th.or.nectec.tanrabad.domain.UserRepository;
import th.or.nectec.tanrabad.domain.building.BuildingRepository;
import th.or.nectec.tanrabad.domain.place.PlaceRepository;
import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.survey.repository.BrokerPlaceRepository;
import th.or.nectec.tanrabad.survey.repository.ChangedRepository;
import th.or.nectec.tanrabad.survey.repository.StubUserRepository;
import th.or.nectec.tanrabad.survey.utils.collection.CursorList;
import th.or.nectec.tanrabad.survey.utils.collection.CursorMapper;

public class DbBuildingRepository implements BuildingRepository, ChangedRepository<Building> {

    public static final String TABLE_NAME = "building";
    public static final int ERROR_INSERT_ID = -1;
    private Context context;
    private UserRepository userRepository;
    private PlaceRepository placeRepository;


    public DbBuildingRepository(Context context) {
        this(context, new StubUserRepository(), BrokerPlaceRepository.getInstance());
    }

    public DbBuildingRepository(Context context, UserRepository userRepository, PlaceRepository placeRepository) {
        this.context = context;
        this.userRepository = userRepository;
        this.placeRepository = placeRepository;
    }

    @Override
    public List<Building> findByPlaceUUID(UUID placeUuid) {
        SQLiteDatabase db = readableDatabase();
        Cursor buildingCursor = db.query(TABLE_NAME, BuildingColumn.wildcard(),
                BuildingColumn.PLACE_ID + "=?", new String[]{placeUuid.toString()}, null, null, BuildingColumn.NAME);
        return getBuildingList(buildingCursor);
    }

    @Override
    public List<Building> findByPlaceUUIDAndBuildingName(UUID placeUuid, String buildingName) {
        SQLiteDatabase db = readableDatabase();
        Cursor cursor = db.query(TABLE_NAME, BuildingColumn.wildcard(),
                BuildingColumn.PLACE_ID + "=? AND " + BuildingColumn.NAME + " LIKE ?",
                new String[]{placeUuid.toString(), "%" + buildingName + "%"}, null, null, BuildingColumn.NAME);
        return getBuildingList(cursor);
    }

    @Override
    public Building findByUUID(UUID uuid) {
        SQLiteDatabase db = readableDatabase();
        Cursor cursor = db.query(TABLE_NAME, BuildingColumn.wildcard(),
                BuildingColumn.ID + "=?", new String[]{uuid.toString()}, null, null, null);
        return getBuilding(cursor);
    }

    private Building getBuilding(Cursor cursor) {
        if (cursor.moveToFirst()) {
            Building building = getMapper(cursor).map(cursor);
            cursor.close();
            return building;
        } else {
            cursor.close();
            return null;
        }
    }

    private SQLiteDatabase readableDatabase() {
        return new SurveyLiteDatabase(context).getReadableDatabase();
    }

    private List<Building> getBuildingList(Cursor cursor) {
        List<Building> buildingList = new CursorList<>(cursor, getMapper(cursor));
        return buildingList.isEmpty() ? null : buildingList;
    }

    private CursorMapper<Building> getMapper(Cursor cursor) {
        return new BuildingCursorMapper(cursor, placeRepository);
    }

    @Override
    public boolean save(Building building) {
        ContentValues values = buildingContentValues(building);
        values.put(PlaceColumn.CHANGED_STATUS, ChangedStatus.ADD);
        return saveByContentValues(writableDatabase(), values);
    }

    @Override
    public boolean update(Building building) {
        ContentValues values = buildingContentValues(building);
        values.put(PlaceColumn.CHANGED_STATUS, getAddOrChangedStatus(building));
        return updateByContentValues(writableDatabase(), values);
    }

    private int getAddOrChangedStatus(Building building) {
        Cursor placeCursor = readableDatabase().query(TABLE_NAME, new String[]{BuildingColumn.CHANGED_STATUS},
                BuildingColumn.ID + "=?", new String[]{building.getId().toString()}, null, null, null);
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
        return db.update(TABLE_NAME, place, BuildingColumn.ID + "=?",
                new String[]{place.getAsString(BuildingColumn.ID)}) > 0;
    }

    @Override
    public void updateOrInsert(List<Building> buildings) {
        SQLiteDatabase db = writableDatabase();
        db.beginTransaction();
        for (Building building : buildings) {
            ContentValues values = buildingContentValues(building);
            values.put(PlaceColumn.CHANGED_STATUS, ChangedStatus.UNCHANGED);
            boolean updated = updateByContentValues(db, values);
            if (!updated)
                saveByContentValues(db, values);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    private ContentValues buildingContentValues(Building building) {
        ContentValues values = new ContentValues();
        values.put(BuildingColumn.ID, building.getId().toString());
        values.put(BuildingColumn.NAME, building.getName());
        values.put(BuildingColumn.PLACE_ID, building.getPlaceId().toString());
        if (building.getLocation() != null) {
            values.put(BuildingColumn.LATITUDE, building.getLocation().getLatitude());
            values.put(BuildingColumn.LONGITUDE, building.getLocation().getLongitude());
        }
        if (building.getUpdateBy() != null) {
            values.put(BuildingColumn.UPDATE_BY, building.getUpdateBy());
        }
        values.put(BuildingColumn.UPDATE_TIME, building.getUpdateTimestamp().toString());
        return values;
    }

    private boolean saveByContentValues(SQLiteDatabase db, ContentValues building) {
        return db.insert(TABLE_NAME, null, building) != ERROR_INSERT_ID;
    }

    private SQLiteDatabase writableDatabase() {
        return new SurveyLiteDatabase(context).getWritableDatabase();
    }

    @Override
    public List<Building> getAdd() {
        Cursor buildingCursor = readableDatabase().query(TABLE_NAME, BuildingColumn.wildcard(),
                BuildingColumn.CHANGED_STATUS + "=?",
                new String[]{String.valueOf(ChangedStatus.ADD)},
                null, null, null);
        return getBuildingList(buildingCursor);
    }

    @Override
    public List<Building> getChanged() {
        Cursor buildingCursor = readableDatabase().query(TABLE_NAME, BuildingColumn.wildcard(),
                BuildingColumn.CHANGED_STATUS + "=?", new String[]{String.valueOf(ChangedStatus.CHANGED)},
                null, null, null);
        return getBuildingList(buildingCursor);
    }

    @Override
    public boolean markUnchanged(Building data) {
        ContentValues values = new ContentValues();
        values.put(BuildingColumn.ID, data.getId().toString());
        values.put(BuildingColumn.CHANGED_STATUS, ChangedStatus.UNCHANGED);
        return updateByContentValues(values);
    }

    private boolean updateByContentValues(ContentValues place) {
        SQLiteDatabase db = writableDatabase();
        boolean isSuccess = updateByContentValues(db, place);
        db.close();
        return isSuccess;
    }
}
