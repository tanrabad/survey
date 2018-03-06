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
import android.support.annotation.NonNull;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.joda.time.DateTime;
import org.tanrabad.survey.BuildConfig;
import org.tanrabad.survey.TanrabadApp;
import org.tanrabad.survey.domain.building.BuildingRepository;
import org.tanrabad.survey.domain.building.BuildingWithSurveyStatus;
import org.tanrabad.survey.domain.place.PlaceRepository;
import org.tanrabad.survey.domain.survey.ContainerTypeRepository;
import org.tanrabad.survey.domain.survey.SurveyRepository;
import org.tanrabad.survey.domain.user.UserRepository;
import org.tanrabad.survey.entity.Building;
import org.tanrabad.survey.entity.Place;
import org.tanrabad.survey.entity.Survey;
import org.tanrabad.survey.entity.SurveyDetail;
import org.tanrabad.survey.entity.User;
import org.tanrabad.survey.repository.BrokerBuildingRepository;
import org.tanrabad.survey.repository.BrokerContainerTypeRepository;
import org.tanrabad.survey.repository.BrokerPlaceRepository;
import org.tanrabad.survey.repository.BrokerUserRepository;
import org.tanrabad.survey.repository.SurveyRepositoryException;
import org.tanrabad.survey.utils.collection.CursorList;
import org.tanrabad.survey.utils.collection.CursorMapper;
import org.tanrabad.survey.utils.time.ThaiDateTimeConverter;
import org.tanrabad.survey.utils.tool.FabricTools;

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
        boolean surveySaveSuccess = false;
        try {
            surveySaveSuccess = save(survey, values);
        } catch (IllegalStateException excepted) {
            FabricTools.getInstance(getContext()).log(excepted);
        }
        return surveySaveSuccess;
    }

    private boolean save(Survey survey, ContentValues values) {
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
        db.close();
        return surveySaveSuccess;
    }

    @Override
    public boolean update(Survey survey) {
        ContentValues values = surveyContentValues(survey);
        values.put(SurveyColumn.CHANGED_STATUS, getAddOrChangedStatus(survey));
        boolean surveyUpdateSuccess = false;
        try {
            surveyUpdateSuccess = update(survey, values);
        } catch (IllegalStateException excepted) {
            FabricTools.getInstance(getContext()).log(excepted);
        }
        return surveyUpdateSuccess;
    }

    private boolean update(Survey survey, ContentValues values) {
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
        db.close();
        return surveyUpdateSuccess;
    }

    @Override
    public boolean delete(Survey data) {
        SQLiteDatabase db = writableDatabase();
        int delete = db.delete(TABLE_NAME, "survey_id=?", new String[]{data.getId().toString()});
        db.close();
        return delete == 1;
    }

    @Override
    public void updateOrInsert(List<Survey> surveys) {
    }

    private int getAddOrChangedStatus(Survey survey) {
        SQLiteDatabase db = readableDatabase();
        Cursor placeCursor = db.query(TABLE_NAME,
            new String[]{SurveyColumn.CHANGED_STATUS},
            SurveyColumn.ID + "=?",
            new String[]{survey.getId().toString()},
            null, null, null);
        int changedStatus = ChangedStatus.CHANGED;
        try {
            if (placeCursor.moveToNext()) {
                if (placeCursor.getInt(0) == ChangedStatus.ADD)
                    changedStatus = ChangedStatus.ADD;
                else
                    changedStatus = ChangedStatus.CHANGED;
            }
            placeCursor.close();
        } catch (IllegalStateException excepted) {
            FabricTools.getInstance(getContext()).log(excepted);
        }
        db.close();
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
                boolean isSuccess = saveSurveyDetail(db, survey.getId(), location, eachDetail);
                if (!isSuccess)
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
    public Survey findRecent(Building building, User user) {
        SQLiteDatabase db = readableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
            SurveyColumn.wildcard(),
            SurveyColumn.BUILDING_ID + "=? "
                + "AND " + surveyWithRangeCondition()
                + "AND " + SurveyColumn.SURVEYOR + "=?",
            new String[]{building.getId().toString(), user.getUsername()},
            null, null, null);
        Survey survey = getSurvey(cursor);
        db.close();
        return survey;
    }

    @Override
    public List<Survey> findRecent(Place place, User user) {
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
                + "AND " + surveyWithRangeCondition()
                + "AND " + SurveyColumn.SURVEYOR + "=?",
            new String[]{place.getId().toString(), user.getUsername()},
            null, null, SurveyColumn.CREATE_TIME + " DESC");
        CursorList<Survey> surveys = new CursorList<>(cursor, getSurveyMapper(cursor));
        db.close();
        return surveys;
    }

    @Override
    public List<BuildingWithSurveyStatus> findSurveyBuilding(Place place, User user) {
        String[] columns = buildingSurveyColumn();
        SQLiteDatabase db = readableDatabase();
        Cursor cursor = db.query(DbBuildingRepository.TABLE_NAME
                + " LEFT JOIN " + "(SELECT survey_id, building_id FROM survey WHERE "
                + surveyWithRangeCondition() + "AND surveyor='" + user.getUsername() + "') AS survey "
                + "ON survey.building_id = building.building_id",
            columns,
            BuildingColumn.PLACE_ID + "=?",
            new String[]{place.getId().toString()},
            null, null, null);
        List<BuildingWithSurveyStatus> building = mapSurveyBuildingStatus(cursor);
        db.close();
        return building;
    }

    @Override
    public List<BuildingWithSurveyStatus> findSurveyBuildingByBuildingName(
        Place place, User user, String buildingName) {
        String[] columns = buildingSurveyColumn();
        SQLiteDatabase db = readableDatabase();
        Cursor cursor = db.query(
            DbBuildingRepository.TABLE_NAME + " LEFT JOIN " + "(SELECT survey_id, building_id FROM survey WHERE "
                + surveyWithRangeCondition() + "AND surveyor='" + user.getUsername() + "') AS survey "
                + "ON survey.building_id = building.building_id",
            columns,
            BuildingColumn.PLACE_ID + "=? AND " + BuildingColumn.NAME + " LIKE ?",
            new String[]{place.getId().toString(), "%" + buildingName + "%"},
            null, null, null);
        List<BuildingWithSurveyStatus> building = mapSurveyBuildingStatus(cursor);
        db.close();
        return building;
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
        try {
            if (cursor.moveToFirst()) {
                CursorMapper<SurveyDetail> mapper = getSurveyDetailMapper(cursor);
                do {
                    SurveyDetail detail = mapper.map(cursor);
                    detailList.add(detail);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (IllegalStateException excepted) {
            FabricTools.getInstance(getContext()).log(excepted);
        }
        db.close();
        return detailList;
    }

    private CursorMapper<SurveyDetail> getSurveyDetailMapper(Cursor cursor) {
        return new SurveyDetailCursorMapper(cursor, containerTypeRepository);
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
        try {
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
        } catch (IllegalStateException excepted) {
            FabricTools.getInstance(getContext()).log(excepted);
        }
        return surveyBuilding;
    }

    private CursorMapper<Building> getBuildingSurveyMapper(Cursor cursor) {
        return new BuildingCursorMapper(cursor, placeRepository);
    }

    private String surveyWithRangeCondition() {
        DateTime dateTime = ThaiDateTimeConverter.convert(new DateTime().toString());
        return "date(" + DbSurveyRepository.TABLE_NAME + "." + SurveyColumn.CREATE_TIME + ")"
            + " BETWEEN date('" + dateTime.minusDays(BuildConfig.SURVEY_RANGE_DAY) + "') "
            + "AND date('" + dateTime + "') ";
    }

    private Survey getSurvey(Cursor cursor) {
        try {
            if (cursor.moveToFirst()) {
                Survey place = getSurveyMapper(cursor).map(cursor);
                cursor.close();
                return place;
            } else {
                cursor.close();
                return null;
            }
        } catch (IllegalStateException except) {
            FabricTools.getInstance(getContext()).log(except);
            return null;
        }
    }

    private CursorMapper<Survey> getSurveyMapper(Cursor cursor) {
        return new SurveyCursorMapper(cursor, userRepository, buildingRepository, this);
    }

    @Override
    public List<Survey> getAdd() {
        SQLiteDatabase db = readableDatabase();
        Cursor placeCursor = db.query(TABLE_NAME, SurveyColumn.wildcard(),
            SurveyColumn.CHANGED_STATUS + "=?", new String[]{String.valueOf(ChangedStatus.ADD)}, null, null, null);
        CursorList<Survey> surveys = new CursorList<>(placeCursor, getSurveyMapper(placeCursor));
        db.close();
        return surveys;
    }

    @Override
    public List<Survey> getChanged() {
        SQLiteDatabase db = readableDatabase();
        Cursor placeCursor = db.query(TABLE_NAME,
            SurveyColumn.wildcard(),
            SurveyColumn.CHANGED_STATUS + "=?",
            new String[]{String.valueOf(ChangedStatus.CHANGED)},
            null, null, null);
        CursorList<Survey> surveys = new CursorList<>(placeCursor, getSurveyMapper(placeCursor));
        db.close();
        return surveys;
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
        boolean isSuccess = updateByContentValues(db, survey);
        db.close();
        return isSuccess;
    }
}
