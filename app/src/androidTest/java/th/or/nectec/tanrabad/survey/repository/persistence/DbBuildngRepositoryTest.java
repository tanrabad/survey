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
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.entity.Location;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.entity.User;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class DbBuildngRepositoryTest {

    @Rule
    public SurveyDbTestRule dbTestRule = new SurveyDbTestRule();

    @Ignore
    @Test
    public void testSave() throws Exception {
        Place place = Place.withName("National Science Park");
        User updateBy = User.fromUsername("blaze");
        Building building = Building.withName("No. 1/1");
        building.setPlace(place);
        building.setLocation(new Location(10.200000f, 100.100000f));
        building.setUpdateBy(updateBy);
        Context context = InstrumentationRegistry.getTargetContext();
        DbBuildngRepository dbBuildngRepository = new DbBuildngRepository(context);
        boolean success = dbBuildngRepository.save(building);

        SQLiteDatabase db = new SurveyLiteDatabase(context).getReadableDatabase();
        Cursor cursor = db.query(DbBuildngRepository.TABLE_NAME,
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

        cursor.close();
    }
}