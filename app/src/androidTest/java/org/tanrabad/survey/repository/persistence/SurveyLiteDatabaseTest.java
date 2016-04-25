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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import org.junit.Rule;
import org.junit.Test;
import org.tanrabad.survey.base.SurveyDbTestRule;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class SurveyLiteDatabaseTest {

    @Rule
    public SurveyDbTestRule dbTestRule = new SurveyDbTestRule();

    @Test
    public void testDatabaseProperties() throws Exception {
        SQLiteDatabase db = dbTestRule.getReadable();
        assertEquals(SurveyLiteDatabase.DB_NAME, new File(db.getPath()).getName());
        assertEquals(SurveyLiteDatabase.DB_VERSION, db.getVersion());
    }

    @Test
    public void testGetReadable() throws Exception {
        SQLiteDatabase db = dbTestRule.getReadable();
        Cursor province = db.query("province", new String[]{"province_code", "name"}, null, null, null, null, null);
        Cursor subdistricts = db.query(
                "subdistrict", new String[]{"subdistrict_code", "name", "district_code"}, null, null, null, null, null);

        assertEquals(52, subdistricts.getCount());
        assertEquals(true, province.moveToFirst());
        assertEquals(1, province.getCount());
        assertEquals("12", province.getString(0));
        assertEquals("นนทบุรี", province.getString(1));

        province.close();
        subdistricts.close();
    }

    @Test
    public void testWritable() throws Exception {
        long code = dbTestRule.getWritable().insert("province", null, getBangkokProvince());

        assertEquals(true, code != -1);
    }

    private ContentValues getBangkokProvince() {
        ContentValues cv = new ContentValues();
        cv.put("province_code", "01");
        cv.put("name", "กรุงเทพมหานคร");
        return cv;
    }

    @Test
    public void testIntegrity() throws Exception {
        assertTrue(dbTestRule.getReadable().isDatabaseIntegrityOk());
    }
}
