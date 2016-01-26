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

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import th.or.nectec.tanrabad.domain.UserRepository;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.entity.User;
import th.or.nectec.tanrabad.entity.field.Location;
import th.or.nectec.tanrabad.survey.utils.time.ThaiDateTimeConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class DbPlaceRepositoryTest {

    @Rule
    public SurveyDbTestRule dbTestRule = new SurveyDbTestRule();
    private UserRepository userRepository;

    @Before
    public void setup() {
        User user = stubUser();
        userRepository = Mockito.mock(UserRepository.class);
        Mockito.when(userRepository.findByUsername("dpc-user")).thenReturn(user);
    }

    private User stubUser() {
        return User.fromUsername("dpc-user");
    }

    DateTime updateTime = DateTime.now();

    @Test
    public void testSave() throws Exception {
        Place place = getPlace();
        Context context = InstrumentationRegistry.getTargetContext();
        DbPlaceRepository dbPlaceRepository = new DbPlaceRepository(context);
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
        assertEquals(place.getType(), PlaceTypeMapper.getInstance().findBySubType(cursor.getInt(cursor.getColumnIndex(PlaceColumn.SUBTYPE_ID))));
        assertEquals(place.getSubType(), cursor.getInt(cursor.getColumnIndex(PlaceColumn.SUBTYPE_ID)));
        assertEquals(stubUser().getUsername(), cursor.getString(cursor.getColumnIndex(PlaceColumn.UPDATE_BY)));
        assertEquals(updateTime, ThaiDateTimeConverter.convert(cursor.getString(cursor.getColumnIndex(PlaceColumn.UPDATE_TIME))));
        assertEquals(ChangedStatus.ADD, cursor.getInt(cursor.getColumnIndex(PlaceColumn.CHANGED_STATUS)));
        cursor.close();
    }

    @Test
    public void testInsertOrUpdate() throws Exception {
        Place place = getPlace();
        Context context = InstrumentationRegistry.getTargetContext();
        DbPlaceRepository dbPlaceRepository = new DbPlaceRepository(context);
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
        assertEquals(updateTime, ThaiDateTimeConverter.convert(cursor.getString(cursor.getColumnIndex(PlaceColumn.UPDATE_TIME))));
        assertEquals(ChangedStatus.UNCHANGED, cursor.getInt(cursor.getColumnIndex(PlaceColumn.CHANGED_STATUS)));
        cursor.close();
    }

    @Test
    public void testUpdate() throws Exception {
        Place place = new Place(UUID.fromString("abc01db8-7207-8a65-152f-ad208cb99b5e"), "หมู่บ้านทดสอบ");
        place.setSubdistrictCode("120202");
        place.setSubType(PlaceTypeMapper.ชุมชนแออัด);
        place.setType(Place.TYPE_VILLAGE_COMMUNITY);
        place.setLocation(new Location(10.200000f, 100.100000f));
        place.setUpdateBy(stubUser());
        place.setUpdateTimestamp(updateTime.toString());

        Context context = InstrumentationRegistry.getTargetContext();
        DbPlaceRepository dbPlaceRepository = new DbPlaceRepository(context);
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
        assertEquals(updateTime, ThaiDateTimeConverter.convert(cursor.getString(cursor.getColumnIndex(PlaceColumn.UPDATE_TIME))));
        assertEquals(ChangedStatus.CHANGED, cursor.getInt(cursor.getColumnIndex(PlaceColumn.CHANGED_STATUS)));
        cursor.close();
    }

    @Test
    public void testSaveAndUpdate() throws Exception {
        Place place = getPlace();
        Context context = InstrumentationRegistry.getTargetContext();
        DbPlaceRepository dbPlaceRepository = new DbPlaceRepository(context);
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
        assertEquals(updateTime, ThaiDateTimeConverter.convert(cursor.getString(cursor.getColumnIndex(PlaceColumn.UPDATE_TIME))));
        assertEquals(ChangedStatus.ADD, cursor.getInt(cursor.getColumnIndex(PlaceColumn.CHANGED_STATUS)));
        cursor.close();
    }

    @Test
    public void testFindByUUID() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        DbPlaceRepository dbPlaceRepository = new DbPlaceRepository(context, userRepository);

        Place place = dbPlaceRepository.findByUUID(UUID.fromString("abc01db8-7207-8a65-152f-ad208cb99b5e"));

        assertEquals("abc01db8-7207-8a65-152f-ad208cb99b5e", place.getId().toString());
        assertEquals("หมู่บ้านทดสอบ", place.getName());
        assertEquals("120202", place.getSubdistrictCode());
        assertEquals("dpc-user", place.getUpdateBy());
    }

    @Test
    public void testFindByPlaceType() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        DbPlaceRepository dbPlaceRepository = new DbPlaceRepository(context, userRepository);

        List<Place> placeList = dbPlaceRepository.findByPlaceType(Place.TYPE_VILLAGE_COMMUNITY);
        Place place = placeList.get(0);

        assertEquals(3, placeList.size());
        assertEquals("abc01db8-7207-8a65-152f-ad208cb99b5e", place.getId().toString());
        assertEquals("หมู่บ้านทดสอบ", place.getName());
        assertEquals("120202", place.getSubdistrictCode());
        assertEquals("dpc-user", place.getUpdateBy());
    }

    @Test
    public void testFindAllPlace() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        DbPlaceRepository dbPlaceRepository = new DbPlaceRepository(context, userRepository);

        List<Place> placeList = dbPlaceRepository.find();
        Place place = placeList.get(0);

        assertEquals(10, placeList.size());
        assertEquals("abc01db8-7207-8a65-152f-ad208cb99b5e", place.getId().toString());
        assertEquals("หมู่บ้านทดสอบ", place.getName());
        assertEquals("120202", place.getSubdistrictCode());
        assertEquals("dpc-user", place.getUpdateBy());
    }

    @NonNull
    private Place getPlace() {
        Place place = new Place(UUID.fromString("abc01db8-7207-8a65-152f-ad208cb99b5f"), "หมู่บ้านทดสอบ");
        place.setSubdistrictCode("120202");
        place.setSubType(PlaceTypeMapper.ชุมชนแออัด);
        place.setType(Place.TYPE_VILLAGE_COMMUNITY);
        place.setLocation(new Location(10.200000f, 100.100000f));
        place.setUpdateBy(stubUser());
        place.setUpdateTimestamp(updateTime.toString());
        return place;
    }
}