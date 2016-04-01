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
import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.joda.time.DateTime;
import th.or.nectec.tanrabad.domain.building.BuildingRepository;
import th.or.nectec.tanrabad.domain.building.BuildingWithSurveyStatus;
import th.or.nectec.tanrabad.domain.place.PlaceRepository;
import th.or.nectec.tanrabad.domain.survey.ContainerTypeRepository;
import th.or.nectec.tanrabad.domain.survey.SurveyRepository;
import th.or.nectec.tanrabad.domain.user.UserRepository;
import th.or.nectec.tanrabad.entity.*;
import th.or.nectec.tanrabad.survey.TanrabadApp;
import th.or.nectec.tanrabad.survey.repository.*;
import th.or.nectec.tanrabad.survey.utils.collection.CursorList;
import th.or.nectec.tanrabad.survey.utils.collection.CursorMapper;
import th.or.nectec.tanrabad.survey.utils.time.ThaiDateTimeConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DbSurveyRepository extends DbRepository implements SurveyRepository, ChangedRepository<Survey> {

    public static final int INDOOR_CONTAINER_LOCATION = 1;
    public static final int OUTDOOR_CONTAINER_LOCATION = 2;
    public static String TABLE_NAME = "survey";
    public static String DETAIL_TABLE_NAME = "survey_detail";
    private UserRepository userRepository;
    private PlaceRepository placeRepository;
    private BuildingRepository buildingRepository;
    private ContainerTypeRepository containerTypeRepository;

    public DbSurveyRepository(Context context) {
        this(context, BrokerUserRepository.getInstance(),
                BrokerPlaceRepository.getInstance(),
                BrokerBuildingRepository.getInstance(),
                BrokerContainerTypeRepository.getInstance());
    }

    public DbSurveyRepository(Context context,
                              UserRepository userRepository,
                              PlaceRepository placeRepository,
                              BuildingRepository buildingRepository,
                              ContainerTypeRepository containerTypeRepository) {
        super(context);
        this.userRepository = userRepository;
        this.placeRepository = placeRepository;
        this.buildingRepository = buildingRepository;
        this.containerTypeRepository = containerTypeRepository;
    }

    @Override
    public boolean save(Survey survey) {
        ContentValues values = surveyContentValues(survey);
        values.put(SurveyColumn.CHANGED_STATUS, ChangedStatus.ADD);
        SQLiteDatabase db = writableDatabase();
        db.beginTransaction();
        boolean surveySaveSuccess;
        try {
            saveByContentValues(db, values);
            saveSurveyDetail(db, survey, INDOOR_CONTAINER_LOCATION);
            saveSurveyDetail(db, survey, OUTDOOR_CONTAINER_LOCATION);
            db.setTransactionSuccessful();
            surveySaveSuccess = true;
        } catch (SurveyRepositoryException exception) {
            TanrabadApp.log(exception);
            surveySaveSuccess = false;
        } finally {
            db.endTransaction();
        }
        return surveySaveSuccess;
    }

    @Override
    public boolean update(Survey survey) {
        ContentValues values = surveyContentValues(survey);
        values.put(SurveyColumn.CHANGED_STATUS, getAddOrChangedStatus(survey));
        SQLiteDatabase db = writableDatabase();
        boolean surveyUpdateSuccess;
        db.beginTransaction();
        try {
            updateByContentValues(db, values);
            updateOrInsertDetails(db, survey, INDOOR_CONTAINER_LOCATION);
            updateOrInsertDetails(db, survey, OUTDOOR_CONTAINER_LOCATION);
            surveyUpdateSuccess = true;
            db.setTransactionSuccessful();
        } catch (SurveyRepositoryException exception) {
            TanrabadApp.log(exception);
            surveyUpdateSuccess = false;
        } finally {
            db.endTransaction();
        }
        return surveyUpdateSuccess;
    }

    @Override
    public boolean delete(Survey data) {
        int delete = writableDatabase().delete(TABLE_NAME, "survey_id=?", new String[]{data.getId().toString()});
        return delete == 1;
    }

    @Override
    public void updateOrInsert(List<Survey> surveys) {
    }

    private int getAddOrChangedStatus(Survey survey) {
        Cursor placeCursor = readableDatabase().query(TABLE_NAME,
                new String[]{SurveyColumn.CHANGED_STATUS},
                SurveyColumn.ID + "=?",
                new String[]{survey.getId().toString()},
                null, null, null);
        int changedStatus = ChangedStatus.CHANGED;
        if (placeCursor.moveToNext()) {
            if (placeCursor.getInt(0) == ChangedStatus.ADD)
                changedStatus = ChangedStatus.ADD;
            else
                changedStatus = ChangedStatus.CHANGED;
        }
        placeCursor.close();
        return changedStatus;
    }

    private boolean updateByContentValues(SQLiteDatabase db, ContentValues survey) {
        int update = db.update(TABLE_NAME,
                survey,
                SurveyColumn.ID + "=?",
                new String[]{survey.getAsString(SurveyColumn.ID)});
        return update > 0;
    }

    private void updateOrInsertDetails(SQLiteDatabase db, Survey survey, int location) {
        List<SurveyDetail> details = getSurveyDetails(survey, location);
        if (details == null) {
            return;
        }
        for (SurveyDetail eachDetail : details) {
            if (!updateSurveyDetail(db, survey.getId(), location, eachDetail)) {
                boolean isInsertSuccess = saveSurveyDetail(db,
                        survey.getId(),
                        location,
                        eachDetail);
                if (!isInsertSuccess)
                    throw new SurveyRepositoryException("Cannot insert or update survey detail.");
            }
        }
    }

    private boolean updateSurveyDetail(SQLiteDatabase db, UUID id, int containerLocation, SurveyDetail surveyDetail) {
        int updateCount = db.update(DETAIL_TABLE_NAME,
                detailContentValues(id, containerLocation, surveyDetail),
                SurveyDetailColumn.ID + " =?",
                new String[]{surveyDetail.getId().toString()});
        return updateCount == 1;
    }

    private ContentValues surveyContentValues(Survey survey) {
        ContentValues values = new ContentValues();
        values.put(SurveyColumn.ID, survey.getId().toString());
        values.put(SurveyColumn.BUILDING_ID, survey.getSurveyBuilding().getId().toString());
        values.put(SurveyColumn.PERSON_COUNT, survey.getResidentCount());
        if (survey.getLocation() != null) {
            values.put(SurveyColumn.LATITUDE, survey.getLocation().getLatitude());
            values.put(SurveyColumn.LONGITUDE, survey.getLocation().getLongitude());
        }
        if (survey.getUser() != null) {
            values.put(SurveyColumn.SURVEYOR, survey.getUser().getUsername());
        }
        values.put(SurveyColumn.CREATE_TIME, survey.getStartTimestamp().toString());
        values.put(SurveyColumn.UPDATE_TIME, survey.getFinishTimestamp().toString());
        return values;
    }

    private boolean saveByContentValues(SQLiteDatabase db, ContentValues survey) {
        if (db.insert(TABLE_NAME, null, survey) == ERROR_INSERT_ID)
            throw new SurveyRepositoryException("Cannot insert survey data.");
        return true;
    }

    private boolean saveSurveyDetail(SQLiteDatabase db, Survey survey, int location) {
        List<SurveyDetail> details = getSurveyDetails(survey, location);
        if (details == null) {
            return true;
        }
        for (SurveyDetail eachDetail : details) {
            boolean isSuccess = saveSurveyDetail(db, survey.getId(), location, eachDetail);
            if (!isSuccess) {
                throw new SurveyRepositoryException("Cannot insert survey detail data. " + eachDetail.toString());
            }
        }
        return true;
    }

    private List<SurveyDetail> getSurveyDetails(Survey survey, int location) {
        switch (location) {
            case INDOOR_CONTAINER_LOCATION:
                return survey.getIndoorDetail();
            case OUTDOOR_CONTAINER_LOCATION:
                return survey.getOutdoorDetail();
            default:
                throw new IllegalArgumentException("location argument out of range");
        }
    }

    private boolean saveSurveyDetail(SQLiteDatabase db,
                                     UUID surveyId,
                                     int containerLocation,
                                     SurveyDetail surveyDetail) {
        ContentValues values = detailContentValues(surveyId, containerLocation, surveyDetail);
        return db.insert(DETAIL_TABLE_NAME, null, values) != ERROR_INSERT_ID;
    }

    private ContentValues detailContentValues(UUID surveyId, int containerLocationId, SurveyDetail surveyDetail) {
        ContentValues values = new ContentValues();
        values.put(SurveyDetailColumn.ID, surveyDetail.getId().toString());
        values.put(SurveyDetailColumn.SURVEY_ID, surveyId.toString());
        values.put(SurveyDetailColumn.CONTAINER_LOCATION_ID, containerLocationId);
        values.put(SurveyDetailColumn.CONTAINER_TYPE_ID, surveyDetail.getContainerType().getId());
        values.put(SurveyDetailColumn.CONTAINER_COUNT, surveyDetail.getTotalContainer());
        values.put(SurveyDetailColumn.CONTAINER_HAVE_LARVA, surveyDetail.getFoundLarvaContainer());
        return values;
    }

    @Override
    public Survey findByBuildingAndUserIn7Day(Building building, User user) {
        Cursor cursor = readableDatabase().query(TABLE_NAME,
                SurveyColumn.wildcard(),
                SurveyColumn.BUILDING_ID + "=? "
                        + "AND " + surveyWithInWeekQueryCondition()
                        + "AND " + SurveyColumn.SURVEYOR + "=?",
                new String[]{building.getId().toString(), user.getUsername()},
                null, null, null);
        return getSurvey(cursor);
    }

    @Override
    public List<Survey> findByPlaceAndUserIn7Days(Place place, User user) {
        SQLiteDatabase db = readableDatabase();
        String[] columns = new String[]{SurveyColumn.ID,
                SurveyColumn.BUILDING_ID,
                SurveyColumn.PERSON_COUNT,
                SurveyColumn.SURVEYOR,
                TABLE_NAME + "." + SurveyColumn.LATITUDE, TABLE_NAME + "." + SurveyColumn.LONGITUDE,
                SurveyColumn.CREATE_TIME,
                TABLE_NAME + "." + SurveyColumn.UPDATE_TIME,
                TABLE_NAME + "." + SurveyColumn.CHANGED_STATUS};
        Cursor cursor = db.query(TABLE_NAME + " INNER JOIN building USING(building_id)",
                columns,
                BuildingColumn.PLACE_ID + "=? "
                        + "AND " + surveyWithInWeekQueryCondition()
                        + "AND " + SurveyColumn.SURVEYOR + "=?",
                new String[]{place.getId().toString(), user.getUsername()},
                null, null, SurveyColumn.CREATE_TIME + " DESC");
        return new CursorList<>(cursor, getSurveyMapper(cursor));
    }

    @Override
    public List<BuildingWithSurveyStatus> findSurveyBuilding(Place place, User user) {
        String[] columns = buildingSurveyColumn();
        Cursor cursor = readableDatabase().query(DbBuildingRepository.TABLE_NAME
                        + " LEFT JOIN " + "(SELECT survey_id, building_id FROM survey WHERE "
                        + surveyWithInWeekQueryCondition() + "AND surveyor='" + user.getUsername() + "') AS survey "
                        + "ON survey.building_id = building.building_id",
                columns,
                BuildingColumn.PLACE_ID + "=?",
                new String[]{place.getId().toString()},
                null, null, null);
        return mapSurveyBuildingStatus(cursor);
    }

    @Override
    public List<BuildingWithSurveyStatus> findSurveyBuildingByBuildingName(
            Place place, User user, String buildingName) {
        String[] columns = buildingSurveyColumn();
        Cursor cursor = readableDatabase().query(
                DbBuildingRepository.TABLE_NAME + " LEFT JOIN " + "(SELECT survey_id, building_id FROM survey WHERE "
                        + surveyWithInWeekQueryCondition() + "AND surveyor='" + user.getUsername() + "') AS survey "
                        + "ON survey.building_id = building.building_id",
                columns,
                BuildingColumn.PLACE_ID + "=? AND " + BuildingColumn.NAME + " LIKE ?",
                new String[]{place.getId().toString(), "%" + buildingName + "%"},
                null, null, null);
        return mapSurveyBuildingStatus(cursor);
    }

    @Override
    public List<SurveyDetail> findSurveyDetail(UUID surveyId, int containerLocationId) {
        SQLiteDatabase db = readableDatabase();
        Cursor cursor = db.query(DETAIL_TABLE_NAME,
                SurveyDetailColumn.wildcard(),
                SurveyDetailColumn.SURVEY_ID + "=? AND " + SurveyDetailColumn.CONTAINER_LOCATION_ID + "=?",
                new String[]{surveyId.toString(), String.valueOf(containerLocationId)},
                null, null, null);
        List<SurveyDetail> detailList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            CursorMapper<SurveyDetail> mapper = getSurveyDetailMapper(cursor);
            do {
                SurveyDetail detail = mapper.map(cursor);
                detailList.add(detail);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return detailList;
    }

    private CursorMapper<SurveyDetail> getSurveyDetailMapper(Cursor cursor) {
        return new SurveyDetailCursorMapper(cursor, containerTypeRepository);
    }

    @Override
    public List<Place> findByUserIn7Days(User user) {
        String[] columns = new String[]{
                DbPlaceRepository.TABLE_NAME + "." + PlaceColumn.ID,
                DbPlaceRepository.TABLE_NAME + "." + PlaceColumn.NAME,
                PlaceColumn.SUBDISTRICT_CODE,
                DbPlaceRepository.TABLE_NAME + "." + PlaceColumn.LATITUDE,
                DbPlaceRepository.TABLE_NAME + "." + PlaceColumn.LONGITUDE,
                DbPlaceRepository.TABLE_NAME + "." + PlaceColumn.SUBTYPE_ID,
                DbPlaceRepository.TABLE_NAME + "." + PlaceColumn.UPDATE_TIME,
                DbPlaceRepository.TABLE_NAME + "." + PlaceColumn.UPDATE_BY,
                DbPlaceRepository.TABLE_NAME + "." + PlaceColumn.CHANGED_STATUS};
        Cursor cursor = readableDatabase().query(
                TABLE_NAME + " INNER JOIN building USING(building_id) INNER JOIN place USING(place_id)",
                columns,
                SurveyColumn.SURVEYOR + "=?" + "AND " + surveyWithInWeekQueryCondition(),
                new String[]{user.getUsername()},
                DbPlaceRepository.TABLE_NAME + "." + PlaceColumn.ID,
                null,
                TABLE_NAME + "." + SurveyColumn.UPDATE_TIME + " DESC");
        return new CursorList<>(cursor, getPlaceSurveyMapper(cursor));
    }

    private CursorMapper<Place> getPlaceSurveyMapper(Cursor cursor) {
        return new PlaceCursorMapper(cursor);
    }

    @NonNull
    private String[] buildingSurveyColumn() {
        return new String[]{
                DbBuildingRepository.TABLE_NAME + "." + BuildingColumn.ID,
                BuildingColumn.NAME,
                BuildingColumn.PLACE_ID,
                DbBuildingRepository.TABLE_NAME + "." + SurveyColumn.LATITUDE,
                DbBuildingRepository.TABLE_NAME + "." + SurveyColumn.LONGITUDE,
                DbBuildingRepository.TABLE_NAME + "." + SurveyColumn.UPDATE_TIME,
                BuildingColumn.UPDATE_BY,
                DbBuildingRepository.TABLE_NAME + "." + SurveyColumn.CHANGED_STATUS,
                SurveyColumn.ID};
    }

    @NonNull
    private List<BuildingWithSurveyStatus> mapSurveyBuildingStatus(Cursor cursor) {
        List<BuildingWithSurveyStatus> surveyBuilding = new ArrayList<>();
        if (cursor.moveToFirst()) {
            CursorMapper<Building> mapper = getBuildingSurveyMapper(cursor);
            do {
                boolean isSurvey = !TextUtils.isEmpty(cursor.getString(cursor.getColumnIndex(SurveyColumn.ID)));
                BuildingWithSurveyStatus buildingWithSurveyStatus =
                        new BuildingWithSurveyStatus(mapper.map(cursor), isSurvey);
                surveyBuilding.add(buildingWithSurveyStatus);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return surveyBuilding;
    }

    private CursorMapper<Building> getBuildingSurveyMapper(Cursor cursor) {
        return new BuildingCursorMapper(cursor, placeRepository);
    }

    private String surveyWithInWeekQueryCondition() {
        DateTime dateTime = ThaiDateTimeConverter.convert(new DateTime().toString());
        return "date(" + DbSurveyRepository.TABLE_NAME + "." + SurveyColumn.CREATE_TIME + ")"
                + " BETWEEN date('" + dateTime.minusDays(7) + "') " + "AND date('" + dateTime + "') ";
    }

    private Survey getSurvey(Cursor cursor) {
        if (cursor.moveToFirst()) {
            Survey place = getSurveyMapper(cursor).map(cursor);
            cursor.close();
            return place;
        } else {
            cursor.close();
            return null;
        }
    }

    private CursorMapper<Survey> getSurveyMapper(Cursor cursor) {
        return new SurveyCursorMapper(cursor, userRepository, buildingRepository, this);
    }

    @Override
    public List<Survey> getAdd() {
        Cursor placeCursor = readableDatabase().query(TABLE_NAME, SurveyColumn.wildcard(),
                SurveyColumn.CHANGED_STATUS + "=?", new String[]{String.valueOf(ChangedStatus.ADD)}, null, null, null);
        return new CursorList<>(placeCursor, getSurveyMapper(placeCursor));
    }

    @Override
    public List<Survey> getChanged() {
        Cursor placeCursor = readableDatabase().query(TABLE_NAME,
                SurveyColumn.wildcard(),
                SurveyColumn.CHANGED_STATUS + "=?",
                new String[]{String.valueOf(ChangedStatus.CHANGED)},
                null, null, null);
        return new CursorList<>(placeCursor, getSurveyMapper(placeCursor));
    }

    @Override
    public boolean markUnchanged(Survey data) {
        ContentValues values = new ContentValues();
        values.put(SurveyColumn.ID, data.getId().toString());
        values.put(SurveyColumn.CHANGED_STATUS, ChangedStatus.UNCHANGED);
        return updateByContentValues(values);
    }

    private boolean updateByContentValues(ContentValues survey) {
        SQLiteDatabase db = writableDatabase();
        return updateByContentValues(db, survey);
    }
}
