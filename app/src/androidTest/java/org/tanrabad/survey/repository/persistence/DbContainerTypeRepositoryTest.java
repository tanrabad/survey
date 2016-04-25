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
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.tanrabad.survey.base.SurveyDbTestRule;
import org.tanrabad.survey.entity.lookup.ContainerType;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class DbContainerTypeRepositoryTest {

    @Rule
    public SurveyDbTestRule dbTestRule = new SurveyDbTestRule();

    @Test
    public void testSave() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        DbContainerTypeRepository dbContainerTypeRepository = new DbContainerTypeRepository(context);
        boolean success = dbContainerTypeRepository.save(new ContainerType(0, "น้ำดื่ม"));

        SQLiteDatabase db = SurveyLiteDatabase.getInstance(context).getReadableDatabase();
        Cursor cursor = db.query(DbContainerTypeRepository.TABLE_NAME,
                ContainerTypeColumn.wildcard(),
                ContainerTypeColumn.ID + "=?",
                new String[]{String.valueOf(0)},
                null, null, null);

        assertEquals(true, success);
        assertEquals(true, cursor.moveToFirst());
        assertEquals(0, cursor.getInt(cursor.getColumnIndex(ContainerTypeColumn.ID)));
        assertEquals("น้ำดื่ม", cursor.getString(cursor.getColumnIndex(ContainerTypeColumn.NAME)));
        cursor.close();
    }

    @Test
    public void testUpdate() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        DbContainerTypeRepository dbContainerTypeRepository = new DbContainerTypeRepository(context);
        boolean success = dbContainerTypeRepository.update(new ContainerType(1, "แจกัน"));

        SQLiteDatabase db = SurveyLiteDatabase.getInstance(context).getReadableDatabase();
        Cursor cursor = db.query(DbContainerTypeRepository.TABLE_NAME,
                ContainerTypeColumn.wildcard(),
                ContainerTypeColumn.ID + "=?",
                new String[]{String.valueOf(1)},
                null, null, null);

        assertEquals(true, success);
        assertEquals(true, cursor.moveToFirst());
        assertEquals(1, cursor.getCount());
        assertEquals("แจกัน", cursor.getString(cursor.getColumnIndex(ContainerTypeColumn.NAME)));
        cursor.close();
    }

    @Test
    public void testFindAllContainerType() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        DbContainerTypeRepository dbContainerTypeRepository = new DbContainerTypeRepository(context);
        List<ContainerType> containerTypeList = dbContainerTypeRepository.find();

        assertEquals(10, containerTypeList.size());
        ContainerType containerType = containerTypeList.get(0);
        assertEquals(1, containerType.getId());
        assertEquals("น้ำใช้", containerType.getName());
        ContainerType containerType10 = containerTypeList.get(9);
        assertEquals(10, containerType10.getId());
        assertEquals("อื่นๆ (ที่ใช้ประโยชน์)", containerType10.getName());
    }
}
