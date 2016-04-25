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

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.tanrabad.survey.base.SurveyDbTestRule;
import org.tanrabad.survey.domain.place.PlaceRepository;
import org.tanrabad.survey.domain.user.UserRepository;
import org.tanrabad.survey.entity.Building;
import org.tanrabad.survey.entity.Place;
import org.tanrabad.survey.entity.User;
import org.tanrabad.survey.entity.field.Location;
import org.tanrabad.survey.utils.time.ThaiDateTimeConverter;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class DbBuildingRepositoryTest {

    private final Context context = InstrumentationRegistry.getTargetContext();
    @Rule
    public SurveyDbTestRule dbTestRule = new SurveyDbTestRule();
    private PlaceRepository placeRepository;

    @Before
    public void setup() {
        Place place = stubPlace();
        placeRepository = Mockito.mock(PlaceRepository.class);
        Mockito.when(placeRepository.findByUuid(UUID.fromString("abc01db8-7207-8a65-152f-ad208cb99b5e")))
                .thenReturn(place);
        User user = stubUser();
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        Mockito.when(userRepository.findByUsername("dpc-user")).thenReturn(user);
    }

    private Place stubPlace() {
        return new Place(UUID.fromString("abc01db8-7207-8a65-152f-ad208cb99b5e"), "หมู่บ้านทดสอบ");
    }

    private User stubUser() {
        return User.fromUsername("dpc-user");
    }

    @Test
    public void testSave() throws Exception {
        Place place = stubPlace();
        User updateBy = stubUser();
        Building building = Building.withName("No. 1/1");
        building.setPlace(place);
        building.setLocation(new Location(10.200000f, 100.100000f));
        building.setUpdateBy(updateBy);
        DateTime updateTime = DateTime.now();
        building.setUpdateTimestamp(updateTime.toString());
        DbBuildingRepository dbBuildngRepository = new DbBuildingRepository(context);
        boolean saveResult = dbBuildngRepository.save(building);

        SQLiteDatabase db = SurveyLiteDatabase.getInstance(context).getReadableDatabase();
        Cursor cursor = db.query(DbBuildingRepository.TABLE_NAME,
                BuildingColumn.wildcard(),
                BuildingColumn.ID + "=?",
                new String[]{building.getId().toString()},
                null, null, null);

        assertEquals(true, saveResult);
        assertEquals(true, cursor.moveToFirst());
        assertEquals(1, cursor.getCount());
        assertEquals(building.getId().toString(), cursor.getString(cursor.getColumnIndex(BuildingColumn.ID)));
        assertEquals(building.getName(), cursor.getString(cursor.getColumnIndex(BuildingColumn.NAME)));
        assertEquals(place.getId().toString(), cursor.getString(cursor.getColumnIndex(BuildingColumn.PLACE_ID)));
        assertEquals(updateBy.getUsername(), cursor.getString(cursor.getColumnIndex(BuildingColumn.UPDATE_BY)));
        assertEquals(updateTime, ThaiDateTimeConverter.convert(
                cursor.getString(cursor.getColumnIndex(PlaceColumn.UPDATE_TIME))));
        assertEquals(ChangedStatus.ADD, cursor.getInt(cursor.getColumnIndex(PlaceColumn.CHANGED_STATUS)));
        cursor.close();
    }

    @Test
    public void testUpdate() throws Exception {
        Place place = stubPlace();
        User updateBy = stubUser();
        Building building = new Building(UUID.fromString("00001db8-7207-8a65-152f-ad208cb99b01"), "2aa");
        building.setPlace(place);
        building.setLocation(new Location(10.200000f, 100.100000f));
        building.setUpdateBy(updateBy);
        DateTime updateTime = DateTime.now();
        building.setUpdateTimestamp(updateTime.toString());
        DbBuildingRepository dbBuildngRepository = new DbBuildingRepository(context);
        boolean success = dbBuildngRepository.update(building);

        SQLiteDatabase db = SurveyLiteDatabase.getInstance(context).getReadableDatabase();
        Cursor cursor = db.query(DbBuildingRepository.TABLE_NAME,
                BuildingColumn.wildcard(),
                BuildingColumn.ID + "=?",
                new String[]{building.getId().toString()},
                null, null, null);

        assertEquals(true, success);
        assertEquals(true, cursor.moveToFirst());
        assertEquals(1, cursor.getCount());
        assertEquals(building.getId().toString(), cursor.getString(cursor.getColumnIndex(BuildingColumn.ID)));
        assertEquals(building.getName(), cursor.getString(cursor.getColumnIndex(BuildingColumn.NAME)));
        assertEquals(place.getId().toString(), cursor.getString(cursor.getColumnIndex(BuildingColumn.PLACE_ID)));
        assertEquals(updateBy.getUsername(), cursor.getString(cursor.getColumnIndex(BuildingColumn.UPDATE_BY)));
        assertEquals(updateTime, ThaiDateTimeConverter.convert(
                cursor.getString(cursor.getColumnIndex(PlaceColumn.UPDATE_TIME))));
        assertEquals(ChangedStatus.CHANGED, cursor.getInt(cursor.getColumnIndex(PlaceColumn.CHANGED_STATUS)));
        cursor.close();
    }

    @Test
    public void testSaveAndUpdate() throws Exception {
        Place place = stubPlace();
        User updateBy = stubUser();
        Building building = Building.withName("No. 1/1");
        building.setPlace(place);
        building.setLocation(new Location(10.200000f, 100.100000f));
        building.setUpdateBy(updateBy);
        DateTime updateTime = DateTime.now();
        building.setUpdateTimestamp(updateTime.toString());
        DbBuildingRepository dbBuildngRepository = new DbBuildingRepository(context);
        dbBuildngRepository.save(building);
        building.setName("No. 1/2");
        boolean success = dbBuildngRepository.update(building);

        SQLiteDatabase db = SurveyLiteDatabase.getInstance(context).getReadableDatabase();
        Cursor cursor = db.query(DbBuildingRepository.TABLE_NAME,
                BuildingColumn.wildcard(),
                BuildingColumn.ID + "=?",
                new String[]{building.getId().toString()},
                null, null, null);

        assertEquals(true, success);
        assertEquals(true, cursor.moveToFirst());
        assertEquals(1, cursor.getCount());
        assertEquals(building.getId().toString(), cursor.getString(cursor.getColumnIndex(BuildingColumn.ID)));
        assertEquals(building.getName(), cursor.getString(cursor.getColumnIndex(BuildingColumn.NAME)));
        assertEquals(place.getId().toString(), cursor.getString(cursor.getColumnIndex(BuildingColumn.PLACE_ID)));
        assertEquals(updateBy.getUsername(), cursor.getString(cursor.getColumnIndex(BuildingColumn.UPDATE_BY)));
        assertEquals(updateTime, ThaiDateTimeConverter.convert(
                cursor.getString(cursor.getColumnIndex(PlaceColumn.UPDATE_TIME))));
        assertEquals(ChangedStatus.ADD, cursor.getInt(cursor.getColumnIndex(PlaceColumn.CHANGED_STATUS)));
        cursor.close();
    }

    @Test
    public void testFindByPlaceUuid() throws Exception {
        DbBuildingRepository repository = new DbBuildingRepository(context, placeRepository);

        List<Building> buildingList = repository
                .findByPlaceUuid(UUID.fromString("abc01db8-7207-8a65-152f-ad208cb99b5e"));
        Building building = buildingList.get(0);

        assertEquals(1, buildingList.size());
        assertEquals("00001db8-7207-8a65-152f-ad208cb99b01", building.getId().toString());
        assertEquals("23/2", building.getName());
        assertEquals(stubPlace().getId(), building.getPlace().getId());
        assertEquals("dpc-user", building.getUpdateBy());
        assertEquals("2015-12-24T12:05:19.626+07:00", building.getUpdateTimestamp().toString());
    }

    @Test
    public void testFindByPlaceUuidAndBuildingName() throws Exception {
        DbBuildingRepository dbBuildingRepository = new DbBuildingRepository(context, placeRepository);
        final Place place = stubPlace();

        List<Building> buildingList = dbBuildingRepository.findByPlaceUuidAndBuildingName(
                UUID.fromString("abc01db8-7207-8a65-152f-ad208cb99b5e"), "23/2");
        Building building = buildingList.get(0);

        assertEquals(1, buildingList.size());
        assertEquals("00001db8-7207-8a65-152f-ad208cb99b01", building.getId().toString());
        assertEquals("23/2", building.getName());
        assertEquals(place.getId(), building.getPlace().getId());
        assertEquals("dpc-user", building.getUpdateBy());
        assertEquals("2015-12-24T12:05:19.626+07:00", building.getUpdateTimestamp().toString());
    }

    @Test
    public void testFindByBuildingUuid() throws Exception {
        Place place = stubPlace();
        DbBuildingRepository dbBuildingRepository = new DbBuildingRepository(context, placeRepository);

        Building building = dbBuildingRepository.findByUuid(UUID.fromString("00001db8-7207-8a65-152f-ad208cb99b01"));

        assertEquals("00001db8-7207-8a65-152f-ad208cb99b01", building.getId().toString());
        assertEquals("23/2", building.getName());
        assertEquals(place.getId(), building.getPlace().getId());
        assertEquals("dpc-user", building.getUpdateBy());
        assertEquals("2015-12-24T12:05:19.626+07:00", building.getUpdateTimestamp().toString());
    }
}
