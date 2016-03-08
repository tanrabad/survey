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
import th.or.nectec.tanrabad.domain.organization.OrganizationRepository;
import th.or.nectec.tanrabad.entity.Organization;
import th.or.nectec.tanrabad.survey.utils.collection.CursorMapper;

import java.util.List;

public class DbOrganizationRepository extends DbRepository implements OrganizationRepository {

    public static final String TABLE_NAME = "organization";

    public DbOrganizationRepository(Context context) {
        super(context);
    }

    @Override
    public Organization findById(int organizationId) {
        SQLiteDatabase db = readableDatabase();
        Cursor cursor = db.query(TABLE_NAME, OrganizationColumn.wildcard(),
                OrganizationColumn.ID + "=?", new String[]{String.valueOf(organizationId)}, null, null, null);
        return getOrganization(cursor);
    }

    private Organization getOrganization(Cursor cursor) {
        if (cursor.moveToFirst()) {
            Organization organization = getMapper(cursor).map(cursor);
            cursor.close();
            return organization;
        } else {
            cursor.close();
            return null;
        }
    }

    private CursorMapper<Organization> getMapper(Cursor cursor) {
        return new OrganizationCursorMapper(cursor);
    }

    @Override
    public boolean save(Organization organization) {
        ContentValues values = orgContentValues(organization);
        return saveByContentValues(writableDatabase(), values);
    }

    private ContentValues orgContentValues(Organization organization) {
        ContentValues values = new ContentValues();
        values.put(OrganizationColumn.ID, organization.getOrganizationId());
        values.put(OrganizationColumn.NAME, organization.getName());
        values.put(OrganizationColumn.ADDRESS, organization.getAddress());
        values.put(OrganizationColumn.SUBDISTRICT_CODE, organization.getSubdistrictCode());
        values.put(OrganizationColumn.HEALTH_REGION_CODE, organization.getHealthRegionCode());
        return values;
    }

    private boolean saveByContentValues(SQLiteDatabase db, ContentValues organization) {
        return db.insert(TABLE_NAME, null, organization) != ERROR_INSERT_ID;
    }

    @Override
    public boolean update(Organization organization) {
        ContentValues values = orgContentValues(organization);
        return updateByContentValues(writableDatabase(), values);
    }

    private boolean updateByContentValues(SQLiteDatabase db, ContentValues organization) {
        return db.update(
                TABLE_NAME, organization, OrganizationColumn.ID + "=?",
                new String[]{organization.getAsString(OrganizationColumn.ID)}) > 0;
    }

    @Override
    public void updateOrInsert(List<Organization> organizations) {
    }
}