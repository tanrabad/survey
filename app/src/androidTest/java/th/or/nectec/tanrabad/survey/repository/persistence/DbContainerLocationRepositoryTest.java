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
import th.or.nectec.tanrabad.entity.lookup.ContainerLocation;
import th.or.nectec.tanrabad.survey.base.SurveyDbTestRule;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class DbContainerLocationRepositoryTest {

    @Rule
    public SurveyDbTestRule dbTestRule = new SurveyDbTestRule();

    @Test
    public void testSave() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        DbContainerLocationRepository repository = new DbContainerLocationRepository(context);
        boolean success = repository.save(new ContainerLocation(0, "ภายในอาคาร"));

        SQLiteDatabase db = SurveyLiteDatabase.getInstance(context).getReadableDatabase();
        Cursor cursor = db.query(DbContainerLocationRepository.TABLE_NAME,
                ContainerLocationColumn.wildcard(),
                ContainerLocationColumn.ID + "=?",
                new String[]{String.valueOf(0)},
                null, null, null);

        assertEquals(true, success);
        assertEquals(true, cursor.moveToFirst());
        assertEquals(0, cursor.getInt(cursor.getColumnIndex(ContainerLocationColumn.ID)));
        assertEquals("ภายในอาคาร", cursor.getString(cursor.getColumnIndex(ContainerLocationColumn.NAME)));
        cursor.close();
    }

    @Test
    public void testUpdate() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        DbContainerLocationRepository repository = new DbContainerLocationRepository(context);
        boolean success = repository.update(new ContainerLocation(1, "ภายในอาคาร 555"));

        SQLiteDatabase db = SurveyLiteDatabase.getInstance(context).getReadableDatabase();
        Cursor cursor = db.query(DbContainerLocationRepository.TABLE_NAME,
                ContainerLocationColumn.wildcard(),
                ContainerLocationColumn.ID + "=?",
                new String[]{String.valueOf(1)},
                null, null, null);

        assertEquals(true, success);
        assertEquals(true, cursor.moveToFirst());
        assertEquals(1, cursor.getCount());
        assertEquals("ภายในอาคาร 555", cursor.getString(cursor.getColumnIndex(ContainerTypeColumn.NAME)));
        cursor.close();
    }

    @Test
    public void testFindAllContainerType() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        DbContainerLocationRepository repository = new DbContainerLocationRepository(context);
        List<ContainerLocation> containerLocationList = repository.find();

        assertEquals(2, containerLocationList.size());
        ContainerLocation containerType = containerLocationList.get(0);
        assertEquals(1, containerType.id);
        assertEquals("ภายในอาคาร", containerType.name);
        ContainerLocation containerType10 = containerLocationList.get(1);
        assertEquals(2, containerType10.id);
        assertEquals("ภายนอกอาคาร", containerType10.name);
    }

}