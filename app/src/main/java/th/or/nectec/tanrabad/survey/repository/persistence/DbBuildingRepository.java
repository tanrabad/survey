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
import th.or.nectec.tanrabad.domain.building.BuildingRepository;
import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.survey.repository.InMemoryPlaceRepository;
import th.or.nectec.tanrabad.survey.repository.StubUserRepository;
import th.or.nectec.tanrabad.survey.utils.collection.CursorList;
import th.or.nectec.tanrabad.survey.utils.collection.CursorMapper;

import java.util.List;
import java.util.UUID;

public class DbBuildingRepository implements BuildingRepository {

    public static final String TABLE_NAME = "building";
    public static final int ERROR_INSERT_ID = -1;
    private final Context context;


    public DbBuildingRepository(Context context) {
        this.context = context;
    }

    @Override
    public List<Building> findBuildingInPlace(UUID placeUuid) {
        SQLiteDatabase db = new SurveyLiteDatabase(context).getReadableDatabase();
        Cursor buildingCursor = db.query(TABLE_NAME, BuildingColumn.wildcard(),
                BuildingColumn.PLACE_ID + "=?", new String[]{placeUuid.toString()}, null, null, null);
        return new CursorList<>(buildingCursor, getMapper(buildingCursor));
    }


    @Override
    public Building findBuildingByName(String buildingName) {
        SQLiteDatabase db = new SurveyLiteDatabase(context).getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, BuildingColumn.wildcard(),
                BuildingColumn.NAME + "=?", new String[]{buildingName}, null, null, null);
        return getBuilding(cursor);
    }

    @Override
    public Building findBuildingByUUID(UUID uuid) {
        SQLiteDatabase db = new SurveyLiteDatabase(context).getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, BuildingColumn.wildcard(),
                BuildingColumn.ID + "=?", new String[]{uuid.toString()}, null, null, null);
        return getBuilding(cursor);
    }

    @Override
    public boolean save(Building building) {
        SQLiteDatabase db = new SurveyLiteDatabase(context).getWritableDatabase();
        return db.insert(TABLE_NAME, null, buildingContentValues(building)) != ERROR_INSERT_ID;
    }

    @Override
    public boolean update(Building building) {
        SQLiteDatabase db = new SurveyLiteDatabase(context).getWritableDatabase();
        return db.update(TABLE_NAME, buildingContentValues(building), BuildingColumn.ID + "=?", new String[]{building.getId().toString()}) > 0;
    }

    private ContentValues buildingContentValues(Building building) {
        ContentValues values = new ContentValues();
        values.put(BuildingColumn.ID, building.getId().toString());
        values.put(BuildingColumn.NAME, building.getName());
        values.put(BuildingColumn.PLACE_ID, building.getPlaceId().toString());
        values.put(BuildingColumn.LATITUDE, building.getLocation().getLatitude());
        values.put(BuildingColumn.LONGITUDE, building.getLocation().getLongitude());
        values.put(BuildingColumn.SYNC_STATUS, 0);
        values.put(BuildingColumn.UPDATE_BY, building.getUpdateBy().getUsername());
        values.put(BuildingColumn.UPDATE_TIME, building.getUpdateTimestamp().toString());
        return values;
    }

    @Override
    public void updateOrInsert(List<Building> buildings) {
        for (Building building : buildings) {
            boolean updated = update(building);
            if (!updated)
                save(building);
        }
    }

    @Override
    public List<Building> searchBuildingInPlaceByName(UUID placeUUID, String buildingName) {
        SQLiteDatabase db = new SurveyLiteDatabase(context).getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, BuildingColumn.wildcard(),
                BuildingColumn.PLACE_ID + "=? AND " + BuildingColumn.NAME + " LIKE '%?%'", new String[]{placeUUID.toString(), buildingName}, null, null, null);
        return new CursorList<>(cursor, getMapper(cursor));
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

    private CursorMapper<Building> getMapper(Cursor cursor) {
        return new BuildingCursorMapper(cursor, new StubUserRepository(), InMemoryPlaceRepository.getInstance());
    }


}
