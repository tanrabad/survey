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

import th.or.nectec.tanrabad.entity.Organization;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class DbOrganizationRepositoryTest {

    @Rule
    public SurveyDbTestRule dbTestRule = new SurveyDbTestRule();

    @Test
    public void testSave() throws Exception {
        Organization organization = new Organization(101, "สคร.13");
        organization.setAddress("22/7");
        organization.setHealthRegionCode("dpc-13");
        organization.setSubdistrictCode("120102");


        Context context = InstrumentationRegistry.getTargetContext();
        DbOrganizationRepository repository = new DbOrganizationRepository(context);
        boolean success = repository.save(organization);

        SQLiteDatabase db = SurveyLiteDatabase.getInstance(context).getReadableDatabase();
        Cursor cursor = db.query(DbOrganizationRepository.TABLE_NAME,
                OrganizationColumn.wildcard(),
                OrganizationColumn.ID + "=?",
                new String[]{String.valueOf(organization.getOrganizationId())},
                null, null, null);

        assertEquals(true, success);
        assertEquals(true, cursor.moveToFirst());
        assertEquals(1, cursor.getCount());
        assertEquals(101, cursor.getInt(cursor.getColumnIndex(OrganizationColumn.ID)));
        assertEquals("สคร.13", cursor.getString(cursor.getColumnIndex(OrganizationColumn.NAME)));
        assertEquals("22/7", cursor.getString(cursor.getColumnIndex(OrganizationColumn.ADDRESS)));
        assertEquals("120102", cursor.getString(cursor.getColumnIndex(OrganizationColumn.SUBDISTRICT_CODE)));
        assertEquals("dpc-13", cursor.getString(cursor.getColumnIndex(OrganizationColumn.HEALTH_REGION_CODE)));
        cursor.close();
    }

    @Test
    public void testUpdate() throws Exception {
        Organization organization = stubOrganization();
        organization.setAddress("30/9");

        Context context = InstrumentationRegistry.getTargetContext();
        DbOrganizationRepository repository = new DbOrganizationRepository(context);
        boolean success = repository.update(organization);

        SQLiteDatabase db = SurveyLiteDatabase.getInstance(context).getReadableDatabase();
        Cursor cursor = db.query(DbOrganizationRepository.TABLE_NAME,
                OrganizationColumn.wildcard(),
                OrganizationColumn.ID + "=?",
                new String[]{String.valueOf(organization.getOrganizationId())},
                null, null, null);

        assertEquals(true, success);
        assertEquals(true, cursor.moveToFirst());
        assertEquals(1, cursor.getCount());
        assertEquals(100, cursor.getInt(cursor.getColumnIndex(OrganizationColumn.ID)));
        assertEquals("สคร.13", cursor.getString(cursor.getColumnIndex(OrganizationColumn.NAME)));
        assertEquals("30/9", cursor.getString(cursor.getColumnIndex(OrganizationColumn.ADDRESS)));
        assertEquals("120102", cursor.getString(cursor.getColumnIndex(OrganizationColumn.SUBDISTRICT_CODE)));
        assertEquals("dpc-13", cursor.getString(cursor.getColumnIndex(OrganizationColumn.HEALTH_REGION_CODE)));
        cursor.close();
    }

    private static Organization stubOrganization() {
        Organization organization = new Organization(100, "สคร.13");
        organization.setAddress("22/7");
        organization.setHealthRegionCode("dpc-13");
        organization.setSubdistrictCode("120102");
        return organization;
    }

    @Test
    public void testFindById() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        DbOrganizationRepository repository = new DbOrganizationRepository(context);
        Organization organization = repository.findById(100);

        assertEquals(100, organization.getOrganizationId());
        assertEquals("กรมควบคุมโรค", organization.getName());
        assertEquals("120105", organization.getSubdistrictCode());
        assertEquals("1", organization.getHealthRegionCode());
    }
}