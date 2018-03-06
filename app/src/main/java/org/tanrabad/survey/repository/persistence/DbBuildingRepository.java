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
import java.util.List;
import java.util.UUID;
import org.tanrabad.survey.domain.building.BuildingRepository;
import org.tanrabad.survey.domain.place.PlaceRepository;
import org.tanrabad.survey.entity.Building;
import org.tanrabad.survey.repository.BrokerPlaceRepository;
import org.tanrabad.survey.utils.collection.CursorList;
import org.tanrabad.survey.utils.collection.CursorMapper;

public class DbBuildingRepository extends DbRepository implements BuildingRepository, ChangedRepository<Building> {

    public static final String TABLE_NAME = "building";
    private PlaceRepository placeRepository;

    public DbBuildingRepository(Context context) {
        this(context, BrokerPlaceRepository.getInstance());
    }

    public DbBuildingRepository(Context context, PlaceRepository placeRepository) {
        super(context);
        this.placeRepository = placeRepository;
    }

    @Override
    public List<Building> findByPlaceUuid(UUID placeUuid) {
        SQLiteDatabase db = readableDatabase();
        Cursor buildingCursor = db.query(TABLE_NAME, BuildingColumn.wildcard(),
                BuildingColumn.PLACE_ID + "=?", new String[]{placeUuid.toString()}, null, null, BuildingColumn.NAME);
        List<Building> building = getBuildingList(buildingCursor);
        db.close();
        return building;
    }

    @Override
    public List<Building> findByPlaceUuidAndBuildingName(UUID placeUuid, String buildingName) {
        SQLiteDatabase db = readableDatabase();
        Cursor cursor = db.query(TABLE_NAME, BuildingColumn.wildcard(),
                BuildingColumn.PLACE_ID + "=? AND " + BuildingColumn.NAME + " LIKE ?",
                new String[]{placeUuid.toString(), "%" + buildingName + "%"}, null, null, BuildingColumn.NAME);
        List<Building> buildings = getBuildingList(cursor);
        db.close();
        return buildings;
    }

    @Override
    public Building findByUuid(UUID uuid) {
        SQLiteDatabase db = readableDatabase();
        Cursor cursor = db.query(TABLE_NAME, BuildingColumn.wildcard(),
                BuildingColumn.ID + "=?", new String[]{uuid.toString()}, null, null, null);
        Building building = getBuilding(cursor);
        db.close();
        return building;
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
        SQLiteDatabase db = writableDatabase();
        boolean success = saveByContentValues(db, values);
        db.close();
        return success;
    }

    @Override
    public boolean update(Building building) {
        ContentValues values = buildingContentValues(building);
        values.put(PlaceColumn.CHANGED_STATUS, getAddOrChangedStatus(building));
        SQLiteDatabase db = writableDatabase();
        boolean success = updateByContentValues(db, values);
        db.close();
        return success;
    }

    @Override
    public boolean delete(Building data) {
        SQLiteDatabase db = writableDatabase();
        int deleteCount = db.delete(
                TABLE_NAME, BuildingColumn.ID + "=?", new String[]{data.getId().toString()});
        db.close();
        return deleteCount > 0;
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

    private int getAddOrChangedStatus(Building building) {
        SQLiteDatabase db = readableDatabase();
        Cursor placeCursor = db.query(TABLE_NAME, new String[]{BuildingColumn.CHANGED_STATUS},
                BuildingColumn.ID + "=?", new String[]{building.getId().toString()}, null, null, null);
        int status = ChangedStatus.CHANGED;
        if (placeCursor.moveToNext()) {
            if (placeCursor.getInt(0) == ChangedStatus.ADD)
                status = ChangedStatus.ADD;
            else
                status = ChangedStatus.CHANGED;
            placeCursor.close();
        }
        db.close();
        return status;
    }

    private boolean updateByContentValues(SQLiteDatabase db, ContentValues place) {
        return db.update(TABLE_NAME, place, BuildingColumn.ID + "=?",
                new String[]{place.getAsString(BuildingColumn.ID)}) > 0;
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

    @Override
    public List<Building> getAdd() {
        SQLiteDatabase db = readableDatabase();
        Cursor buildingCursor = db.query(TABLE_NAME, BuildingColumn.wildcard(),
                BuildingColumn.CHANGED_STATUS + "=?",
                new String[]{String.valueOf(ChangedStatus.ADD)},
                null, null, null);
        List<Building> buildings = getBuildingList(buildingCursor);
        db.close();
        return buildings;
    }

    @Override
    public List<Building> getChanged() {
        SQLiteDatabase db = readableDatabase();
        Cursor buildingCursor = db.query(TABLE_NAME, BuildingColumn.wildcard(),
                BuildingColumn.CHANGED_STATUS + "=?", new String[]{String.valueOf(ChangedStatus.CHANGED)},
                null, null, null);
        List<Building> buildings = getBuildingList(buildingCursor);
        db.close();
        return buildings;
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
