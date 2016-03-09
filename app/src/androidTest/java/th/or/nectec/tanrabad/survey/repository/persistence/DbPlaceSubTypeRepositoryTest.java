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
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import th.or.nectec.tanrabad.entity.lookup.PlaceSubType;
import th.or.nectec.tanrabad.survey.base.SurveyDbTestRule;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class DbPlaceSubTypeRepositoryTest {

    @Rule
    public SurveyDbTestRule dbTestRule = new SurveyDbTestRule();

    Context context = InstrumentationRegistry.getTargetContext();
    DbPlaceSubTypeRepository dbPlaceSubTypeRepository = new DbPlaceSubTypeRepository(context);
    SQLiteDatabase db = SurveyLiteDatabase.getInstance(context).getReadableDatabase();

    @Test
    public void testSave() throws Exception {
        boolean success = dbPlaceSubTypeRepository.save(new PlaceSubType(0, "ไม่ระบุ", 1));

        Cursor cursor = db.query(DbPlaceSubTypeRepository.TABLE_NAME,
                PlaceSubTypeColumn.wildcard(),
                PlaceSubTypeColumn.ID + "=?",
                new String[]{String.valueOf(0)},
                null, null, null);

        assertEquals(true, success);
        assertEquals(true, cursor.moveToFirst());
        assertEquals(0, cursor.getInt(cursor.getColumnIndex(PlaceSubTypeColumn.ID)));
        assertEquals("ไม่ระบุ", cursor.getString(cursor.getColumnIndex(PlaceSubTypeColumn.NAME)));
        assertEquals(1, cursor.getInt(cursor.getColumnIndex(PlaceSubTypeColumn.TYPE_ID)));
        cursor.close();
    }

    @Test
    public void testUpdate() throws Exception {
        boolean success = dbPlaceSubTypeRepository.update(new PlaceSubType(1, "สำนักงานสาธารณสุขจังหวัด555", 4));

        Cursor cursor = db.query(DbPlaceSubTypeRepository.TABLE_NAME,
                PlaceSubTypeColumn.wildcard(),
                PlaceSubTypeColumn.ID + "=?",
                new String[]{String.valueOf(1)},
                null, null, null);

        assertEquals(true, success);
        assertEquals(true, cursor.moveToFirst());
        assertEquals(1, cursor.getCount());
        assertEquals(1, cursor.getInt(cursor.getColumnIndex(PlaceSubTypeColumn.ID)));
        assertEquals("สำนักงานสาธารณสุขจังหวัด555", cursor.getString(cursor.getColumnIndex(PlaceSubTypeColumn.NAME)));
        assertEquals(4, cursor.getInt(cursor.getColumnIndex(PlaceSubTypeColumn.TYPE_ID)));
        cursor.close();
    }

    @Test
    public void testFindAllPlaceType() throws Exception {
        List<PlaceSubType> placeSubTypeList = dbPlaceSubTypeRepository.find();

        assertEquals(17, placeSubTypeList.size());
        PlaceSubType placeType0 = placeSubTypeList.get(0);
        assertEquals(1, placeType0.getId());
        assertEquals("สำนักงานสาธารณสุขจังหวัด", placeType0.getName());
        PlaceSubType placeSubType16 = placeSubTypeList.get(16);
        assertEquals(17, placeSubType16.getId());
        assertEquals("โรงงาน", placeSubType16.getName());
    }

    @Test
    public void testFindById() throws Exception {
        PlaceSubType placeSubType = dbPlaceSubTypeRepository.findById(13);

        assertEquals(13, placeSubType.getId());
        assertEquals("วัด", placeSubType.getName());
        assertEquals(2, placeSubType.getPlaceTypeId());
    }

    @Test
    public void testFindByPlaceTypeId() throws Exception {
        List<PlaceSubType> placeSubTypeList = dbPlaceSubTypeRepository.findByPlaceTypeId(4);

        assertEquals(9, placeSubTypeList.size());
        PlaceSubType placeType0 = placeSubTypeList.get(0);
        assertEquals(1, placeType0.getId());
        assertEquals("สำนักงานสาธารณสุขจังหวัด", placeType0.getName());
        PlaceSubType placeSubType16 = placeSubTypeList.get(8);
        assertEquals(9, placeSubType16.getId());
        assertEquals("ศูนย์วิชาการ", placeSubType16.getName());
    }
}
