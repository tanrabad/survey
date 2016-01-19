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
import th.or.nectec.tanrabad.domain.place.PlaceRepository;
import th.or.nectec.tanrabad.entity.Location;
import th.or.nectec.tanrabad.entity.LocationEntity;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.survey.repository.InMemoryAddressRepository;
import th.or.nectec.tanrabad.survey.repository.StubUserRepository;
import th.or.nectec.tanrabad.survey.utils.collection.CursorList;
import th.or.nectec.tanrabad.survey.utils.collection.CursorMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DbPlaceRepository implements PlaceRepository {

    public static final String TABLE_NAME = "place";
    public static final int ERROR_INSERT_ID = -1;
    private final Context context;


    public DbPlaceRepository(Context context) {
        this.context = context;
    }

    @Override
    public List<Place> findPlaces() {
        SQLiteDatabase db = new SurveyLiteDatabase(context).getReadableDatabase();
        Cursor placeCursor = db.query(TABLE_NAME, PlaceColumn.wildcard(),
                null, null, null, null, null);
        return new CursorList<>(placeCursor, getMapper(placeCursor));
    }

    @Override
    public Place findPlaceByUUID(UUID placeUUID) {
        SQLiteDatabase db = new SurveyLiteDatabase(context).getReadableDatabase();
        Cursor placeCursor = db.query(TABLE_NAME, PlaceColumn.wildcard(),
                PlaceColumn.ID + "=?", new String[]{placeUUID.toString()}, null, null, null);
        return getPlace(placeCursor);
    }

    private Place getPlace(Cursor cursor) {
        if (cursor.moveToFirst()) {
            Place building = getMapper(cursor).map(cursor);
            cursor.close();
            return building;
        } else {
            cursor.close();
            return null;
        }
    }

    private CursorMapper<Place> getMapper(Cursor cursor) {
        return new PlaceCursorMapper(cursor, new StubUserRepository(), InMemoryAddressRepository.getInstance());
    }

    @Override
    public List<Place> findPlacesWithPlaceTypeFilter(int placeType) {
        SQLiteDatabase db = new SurveyLiteDatabase(context).getReadableDatabase();
        Cursor placeCursor = db.query(TABLE_NAME + "INNER JOIN place_subtype using(place_id)", PlaceColumn.wildcard(),
                PlaceColumn.TYPE_ID + "=?", new String[]{String.valueOf(placeType)}, null, null, null);
        return new CursorList<>(placeCursor, getMapper(placeCursor));
    }

    @Override
    public boolean save(Place place) {
        return saveByContentValues(new SurveyLiteDatabase(context).getWritableDatabase(), placeContentValues(place));
    }

    private boolean saveByContentValues(SQLiteDatabase db, ContentValues place){
        return db.insert(TABLE_NAME, null, place) != ERROR_INSERT_ID;
    }

    @Override
    public boolean update(Place place) {
        return updateByContentValues(new SurveyLiteDatabase(context).getWritableDatabase(), placeContentValues(place));
    }

    private boolean updateByContentValues(SQLiteDatabase db, ContentValues place) {
        return db.update(TABLE_NAME, place, PlaceColumn.ID + "=?", new String[]{place.getAsString(PlaceColumn.ID)}) > 0;
    }

    @Override
    public List<LocationEntity> findInBoundaryLocation(Location minimumLocation, Location maximumLocation) {
        ArrayList<LocationEntity> filterPlaces = new ArrayList<>();
        for (LocationEntity eachPlace : findPlaces()) {
            final Location location = eachPlace.getLocation();
            if (isLessThanOrEqualMaximumLocation(minimumLocation, location) && isMoreThanOrEqualMinimumLocation(maximumLocation, location))
                filterPlaces.add(eachPlace);
        }
        return filterPlaces.isEmpty() ? null : filterPlaces;
    }

    @Override
    public List<LocationEntity> findTrimmedInBoundaryLocation(Location insideMinimumLocation, Location outsideMinimumLocation,
                                                              Location insideMaximumLocation, Location outsideMaximumLocation) {
        List<LocationEntity> filterPlaces = findInBoundaryLocation(outsideMinimumLocation, outsideMaximumLocation);
        for (int i = 0; i < 4; i++) {
            switch (i) {
                case 0:
                    trim(insideMaximumLocation, outsideMaximumLocation, filterPlaces);
                    break;
                case 1:
                    Location topLeftSouthEastMiniBoundary = new Location(insideMaximumLocation.getLatitude(), insideMinimumLocation.getLongitude());
                    Location bottomRightSouthEastMiniBoundary = new Location(outsideMaximumLocation.getLatitude(), outsideMinimumLocation.getLongitude());
                    trim(topLeftSouthEastMiniBoundary, bottomRightSouthEastMiniBoundary, filterPlaces);
                    break;
                case 2:
                    trim(outsideMinimumLocation, insideMinimumLocation, filterPlaces);
                    break;
                case 3:
                    Location topLeftNorthWestMiniBoundary = new Location(outsideMinimumLocation.getLatitude(), outsideMaximumLocation.getLongitude());
                    Location bottomRightNorthWestMiniBoundary = new Location(insideMinimumLocation.getLatitude(), insideMaximumLocation.getLongitude());
                    trim(topLeftNorthWestMiniBoundary, bottomRightNorthWestMiniBoundary, filterPlaces);
                    break;
            }
        }
        return filterPlaces.isEmpty() ? null : filterPlaces;
    }

    @Override
    public List<Place> findByName(String placeName) {
        SQLiteDatabase db = new SurveyLiteDatabase(context).getReadableDatabase();
        Cursor placeCursor = db.query(TABLE_NAME, PlaceColumn.wildcard(),
                PlaceColumn.NAME + "=?", new String[]{placeName}, null, null, null);
        return new CursorList<>(placeCursor, getMapper(placeCursor));
    }

    @Override
    public void updateOrInsert(List<Place> buildings) {
        SQLiteDatabase db = new SurveyLiteDatabase(context).getWritableDatabase();
        db.beginTransaction();
        for (Place building : buildings) {
            ContentValues values = placeContentValues(building);
            values.put(PlaceColumn.SYNC_STATUS, SyncStatus.SYNCED);
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
        values.put(PlaceColumn.SUBDISTRICT_CODE, place.getAddress().getAddressCode());
        if(place.getLocation() != null) {
            values.put(PlaceColumn.LATITUDE, place.getLocation().getLatitude());
            values.put(PlaceColumn.LONGITUDE, place.getLocation().getLongitude());
        }
        if(place.getUpdateBy() != null) {
            values.put(PlaceColumn.UPDATE_BY, place.getUpdateBy().getUsername());
        }
        values.put(PlaceColumn.UPDATE_TIME, place.getUpdateTimestamp().toString());
        values.put(PlaceColumn.SYNC_STATUS, SyncStatus.NOT_SYNC);
        return values;
    }

    private void trim(Location insideMaximumLocation, Location outsideMaximumLocation, List<LocationEntity> filterPlaces) {
        List<LocationEntity> trimmedLocation = findInBoundaryLocation(insideMaximumLocation, outsideMaximumLocation);
        filterPlaces.removeAll(trimmedLocation);
    }

    private boolean isLessThanOrEqualMaximumLocation(Location maximumLocation, Location location) {
        return location.getLatitude() <= maximumLocation.getLatitude() && location.getLongitude() <= maximumLocation.getLongitude();
    }

    private boolean isMoreThanOrEqualMinimumLocation(Location minimumLocation, Location location) {
        return location.getLatitude() >= minimumLocation.getLatitude() && location.getLongitude() >= minimumLocation.getLongitude();
    }
}
