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
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.tanrabad.survey.base.SurveyDbTestRule;
import org.tanrabad.survey.domain.user.UserRepository;
import org.tanrabad.survey.entity.Building;
import org.tanrabad.survey.entity.Place;
import org.tanrabad.survey.entity.User;
import org.tanrabad.survey.entity.field.Location;
import org.tanrabad.survey.entity.lookup.PlaceSubType;
import org.tanrabad.survey.entity.lookup.PlaceType;
import org.tanrabad.survey.repository.BrokerPlaceSubTypeRepository;
import org.tanrabad.survey.utils.time.ThaiDateTimeConverter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class DbPlaceRepositoryTest {

    private final DateTime updateTime = DateTime.now();
    @Rule
    public SurveyDbTestRule dbTestRule = new SurveyDbTestRule();
    private Context context;
    private DbPlaceRepository dbPlaceRepository;

    @Before
    public void setup() {
        User user = stubUser();
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        Mockito.when(userRepository.findByUsername("dpc-user")).thenReturn(user);

        context = InstrumentationRegistry.getTargetContext();
        dbPlaceRepository = new DbPlaceRepository(context);
    }

    private User stubUser() {
        return User.fromUsername("dpc-user");
    }

    @Test
    public void testSave() throws Exception {
        Place place = getPlace();

        boolean success = dbPlaceRepository.save(place);

        SQLiteDatabase db = new SurveyLiteDatabase(context).getReadableDatabase();
        Cursor cursor = db.query(DbPlaceRepository.TABLE_NAME,
                PlaceColumn.wildcard(),
                PlaceColumn.ID + "=?",
                new String[]{place.getId().toString()},
                null, null, null);

        assertEquals(true, success);
        assertEquals(true, cursor.moveToFirst());
        assertEquals(1, cursor.getCount());
        assertEquals(place.getId().toString(), cursor.getString(cursor.getColumnIndex(PlaceColumn.ID)));
        assertEquals(place.getName(), cursor.getString(cursor.getColumnIndex(PlaceColumn.NAME)));
        assertEquals(place.getType(), BrokerPlaceSubTypeRepository.getInstance()
                .findById(cursor.getInt(cursor.getColumnIndex(PlaceColumn.SUBTYPE_ID)))
                .getPlaceTypeId());
        assertEquals(place.getSubType(), cursor.getInt(cursor.getColumnIndex(PlaceColumn.SUBTYPE_ID)));
        assertEquals(stubUser().getUsername(), cursor.getString(cursor.getColumnIndex(PlaceColumn.UPDATE_BY)));
        assertEquals(updateTime, ThaiDateTimeConverter.convert(
                cursor.getString(cursor.getColumnIndex(PlaceColumn.UPDATE_TIME))));
        assertEquals(ChangedStatus.ADD, cursor.getInt(cursor.getColumnIndex(PlaceColumn.CHANGED_STATUS)));
        cursor.close();
    }

    @NonNull
    private Place getPlace() {
        Place place = new Place(UUID.fromString("abc01db8-7207-8a65-152f-ad208cb99b5f"), "หมู่บ้านทดสอบ");
        place.setSubdistrictCode("120202");
        place.setSubType(PlaceSubType.ชุมชนแออัด);
        place.setType(PlaceType.VILLAGE_COMMUNITY);
        place.setLocation(new Location(10.200000f, 100.100000f));
        place.setUpdateBy(stubUser());
        place.setUpdateTimestamp(updateTime.toString());
        return place;
    }

    @Test
    public void testInsertOrUpdate() throws Exception {
        Place place = getPlace();
        ArrayList<Place> places = new ArrayList<>();
        places.add(place);
        dbPlaceRepository.updateOrInsert(places);

        SQLiteDatabase db = new SurveyLiteDatabase(context).getReadableDatabase();
        Cursor cursor = db.query(DbPlaceRepository.TABLE_NAME,
                PlaceColumn.wildcard(),
                PlaceColumn.ID + "=?",
                new String[]{place.getId().toString()},
                null, null, null);

        assertEquals(true, cursor.moveToFirst());
        assertEquals(1, cursor.getCount());
        assertEquals(place.getId().toString(), cursor.getString(cursor.getColumnIndex(PlaceColumn.ID)));
        assertEquals(place.getName(), cursor.getString(cursor.getColumnIndex(PlaceColumn.NAME)));
        assertEquals(place.getSubType(), cursor.getInt(cursor.getColumnIndex(PlaceColumn.SUBTYPE_ID)));
        assertEquals(stubUser().getUsername(), cursor.getString(cursor.getColumnIndex(PlaceColumn.UPDATE_BY)));
        assertEquals(updateTime, ThaiDateTimeConverter.convert(
                cursor.getString(cursor.getColumnIndex(PlaceColumn.UPDATE_TIME))));
        assertEquals(ChangedStatus.UNCHANGED, cursor.getInt(cursor.getColumnIndex(PlaceColumn.CHANGED_STATUS)));
        cursor.close();
    }

    @Test
    public void testUpdate() throws Exception {
        Place place = new Place(UUID.fromString("abc01db8-7207-8a65-152f-ad208cb99b5e"), "หมู่บ้านทดสอบ");
        place.setSubdistrictCode("120202");
        place.setSubType(PlaceSubType.ชุมชนแออัด);
        place.setType(PlaceType.VILLAGE_COMMUNITY);
        place.setLocation(new Location(10.200000f, 100.100000f));
        place.setUpdateBy(stubUser());
        place.setUpdateTimestamp(updateTime.toString());

        boolean success = dbPlaceRepository.update(place);

        SQLiteDatabase db = new SurveyLiteDatabase(context).getReadableDatabase();
        Cursor cursor = db.query(DbPlaceRepository.TABLE_NAME,
                PlaceColumn.wildcard(),
                PlaceColumn.ID + "=?",
                new String[]{place.getId().toString()},
                null, null, null);

        assertEquals(true, success);
        assertEquals(true, cursor.moveToFirst());
        assertEquals(1, cursor.getCount());
        assertEquals(place.getId().toString(), cursor.getString(cursor.getColumnIndex(PlaceColumn.ID)));
        assertEquals(place.getName(), cursor.getString(cursor.getColumnIndex(PlaceColumn.NAME)));
        assertEquals(place.getSubType(), cursor.getInt(cursor.getColumnIndex(PlaceColumn.SUBTYPE_ID)));
        assertEquals(stubUser().getUsername(), cursor.getString(cursor.getColumnIndex(PlaceColumn.UPDATE_BY)));
        assertEquals(updateTime, ThaiDateTimeConverter.convert(
                cursor.getString(cursor.getColumnIndex(PlaceColumn.UPDATE_TIME))));
        assertEquals(ChangedStatus.CHANGED, cursor.getInt(cursor.getColumnIndex(PlaceColumn.CHANGED_STATUS)));
        cursor.close();
    }

    @Test
    public void testSaveAndUpdate() throws Exception {
        Place place = getPlace();
        dbPlaceRepository.save(place);
        place.setName("หมู่บ้านทดสอบบบบบ");
        boolean success = dbPlaceRepository.update(place);

        SQLiteDatabase db = new SurveyLiteDatabase(context).getReadableDatabase();
        Cursor cursor = db.query(DbPlaceRepository.TABLE_NAME,
                PlaceColumn.wildcard(),
                PlaceColumn.ID + "=?",
                new String[]{place.getId().toString()},
                null, null, null);

        assertEquals(true, success);
        assertEquals(true, cursor.moveToFirst());
        assertEquals(1, cursor.getCount());
        assertEquals(place.getId().toString(), cursor.getString(cursor.getColumnIndex(PlaceColumn.ID)));
        assertEquals(place.getName(), cursor.getString(cursor.getColumnIndex(PlaceColumn.NAME)));
        assertEquals(place.getSubType(), cursor.getInt(cursor.getColumnIndex(PlaceColumn.SUBTYPE_ID)));
        assertEquals(stubUser().getUsername(), cursor.getString(cursor.getColumnIndex(PlaceColumn.UPDATE_BY)));
        assertEquals(updateTime, ThaiDateTimeConverter.convert(
                cursor.getString(cursor.getColumnIndex(PlaceColumn.UPDATE_TIME))));
        assertEquals(ChangedStatus.ADD, cursor.getInt(cursor.getColumnIndex(PlaceColumn.CHANGED_STATUS)));
        cursor.close();
    }

    @Test
    public void testFindByUuid() throws Exception {
        Place place = dbPlaceRepository.findByUuid(UUID.fromString("abc01db8-7207-8a65-152f-ad208cb99b5e"));

        assertEquals("abc01db8-7207-8a65-152f-ad208cb99b5e", place.getId().toString());
        assertEquals("หมู่บ้านทดสอบ", place.getName());
        assertEquals("120202", place.getSubdistrictCode());
        assertEquals("dpc-user", place.getUpdateBy());
    }

    @Test
    public void testFindByPlaceType() throws Exception {
        List<Place> placeList = dbPlaceRepository.findByPlaceType(PlaceType.VILLAGE_COMMUNITY);
        Place place = placeList.get(0);

        assertEquals(3, placeList.size());
        assertEquals("e5ce769e-f397-4409-bec2-818f7bd02464", place.getId().toString());
        assertEquals("ชุมชนกอล์ฟวิว", place.getName());
        assertEquals("120202", place.getSubdistrictCode());
        assertEquals("dpc-user", place.getUpdateBy());
    }

    @Test
    public void testFindAllPlace() throws Exception {
        List<Place> placeList = dbPlaceRepository.find();
        Place place = placeList.get(0);

        assertEquals(10, placeList.size());
        assertEquals("e5ce769e-f397-4409-bec2-818f7bd02464", place.getId().toString());
        assertEquals("ชุมชนกอล์ฟวิว", place.getName());
        assertEquals("120202", place.getSubdistrictCode());
        assertEquals("dpc-user", place.getUpdateBy());
    }

    @Test
    public void testDeletePlaceThenBuildingShouldDeleteCascade() throws Exception {
        Place place = new Place(UUID.fromString("935b9aeb-6522-461e-994f-f9e9006c4a33"), "หมู่บ้านพาลาสเซตโต้");

        dbPlaceRepository.delete(place);

        List<Building> buildings = new DbBuildingRepository(context, dbPlaceRepository).findByPlaceUuid(place.getId());
        assertNull(buildings);
    }
}
